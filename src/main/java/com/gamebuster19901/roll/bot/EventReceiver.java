package com.gamebuster19901.roll.bot;

import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.DiscordUserArgumentBuilder.DiscordUserCommandNode;
import com.gamebuster19901.roll.bot.command.argument.DiscordUserArgumentType;
import com.gamebuster19901.roll.bot.command.argument.GlobalNode;
import com.gamebuster19901.roll.bot.command.argument.SubCommandArgumentBuilder.SubCommandNode;
import com.gamebuster19901.roll.bot.command.interaction.Interactions;
import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.util.StacktraceUtil;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.Command.Subcommand;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.AutoCompleteCallbackAction;

public class EventReceiver extends ListenerAdapter {

	private final Sub sub = new Sub();
	
	EventReceiver(){}
	
	@Override
	public void onGenericEvent(GenericEvent ge) {
		if(ge instanceof GenericInteractionCreateEvent) {
			GenericInteractionCreateEvent e = (GenericInteractionCreateEvent) ge;
			try {
				if(!Table.existsWhere(Table.PLAYERS, new Comparison(Column.DISCORD_ID, Comparator.EQUALS, e.getUser().getIdLong()))) {
					Insertion.insertInto(Table.PLAYERS).setColumns(Column.DISCORD_ID).to(e.getUser().getIdLong()).prepare().execute();
				}
			}
			catch(Throwable t) {
				Throwable t2 = t;
				while(t2 != null) {
					if(t instanceof ConnectionIsClosedException || t instanceof CommunicationsException || t instanceof CJCommunicationsException || t instanceof SQLNonTransientConnectionException) {
						Main.recoverDB();
						break;
					}
					t2 = t2.getCause();
				}
				if(e.getInteraction() instanceof IReplyCallback) {
					new CommandContext(e.getInteraction()).replyMessage(StacktraceUtil.getStackTrace(t));
				}
			}
		}
		sub.onEvent(ge);
	}
	
	private static final class Sub extends ListenerAdapter {
		@Override
		public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
			StringBuilder c = new StringBuilder(e.getName());
			for(OptionMapping arg : e.getOptions()) {
				c.append(' ');
				c.append(arg.getAsString());
			}
			System.out.println(c);
			CommandContext context = new CommandContext(e);
			try {
				Commands.DISPATCHER.getDispatcher().execute(c.toString() , context);
			} catch (Throwable t) {
				if(t.getMessage() != null && !t.getMessage().isBlank()) {
					context.sendMessage(t.getMessage());
				}
				else {
					context.sendMessage(t.toString());
				}
				if(!(t instanceof CommandSyntaxException)) {
					throw new RuntimeException(t);
				}
			}
		}
		
		@Override
		public void onButtonInteraction(ButtonInteractionEvent e) {
			try {
				Interactions.DISPATCHER.execute(e);
			} catch (CommandSyntaxException e1) {
				e1.printStackTrace();
			}
		}
		
		@Override
		public void onGuildReady(GuildReadyEvent e) {
			List<CommandData> commands = new ArrayList<>();
			Commands.DISPATCHER.getDispatcher().getRoot().getChildren().forEach((command) -> {
				System.out.println(command + " " + (command.getChildren().size() > 1));
				if(!Main.discordBot.isDev()) {
					if(command instanceof GlobalNode) {
						return; //Don't register global commands as guild commands if we're not in a dev environment
					}
				}
				SlashCommandData data = net.dv8tion.jda.api.interactions.commands.build.Commands.slash(command.getName(), command.getUsageText());
				
				CommandNode<CommandContext>[] children = command.getChildren().toArray(new CommandNode[] {});
				
				if(children.length > 0) {
					for(int i = 0; i < children.length; i++) {
						CommandNode node = children[i];
						SuggestionProvider suggestions = null;
						
						if(node instanceof SubCommandNode) {
							data.addSubcommands(new SubcommandData(node.getName(), node.getUsageText()));
						}
						else if(node instanceof DiscordUserCommandNode) {
							data.addOption(OptionType.USER, node.getName(), node.getUsageText(), false, true);
						}
						else if(node instanceof LiteralCommandNode) {
							data.addOption(OptionType.STRING, node.getName(), node.getUsageText(), false, true);
						}
						else if(node instanceof ArgumentCommandNode) {
							data.addOption(OptionType.STRING, node.getName(), node.getUsageText(), false, true);
						}
						else {
							data.addOption(OptionType.STRING, node.getName(), node.getUsageText(), false, true);
						}
						

					}
				}
				
				commands.add(data);
				
			});
			
			Interactions.DISPATCHER.getDispatcher(); //initialize interactions
			
			e.getGuild().updateCommands().addCommands(commands).queue();
		}
		
		@Override
		public void onModalInteraction(ModalInteractionEvent e) {
			try {
				Interactions.execute(e);
			} catch (CommandSyntaxException e1) {
				e1.printStackTrace();
			}
		}
		
		@Override
		public void onStringSelectInteraction(StringSelectInteractionEvent e) {
			try {
				Interactions.execute(e);
			} catch (CommandSyntaxException e1) {
				e1.printStackTrace();
			}
		}
		
		@Override
		public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent e) {
			String command = getCommand(e);
			System.err.println(command);
			ParseResults<CommandContext> parseResults = Commands.DISPATCHER.getDispatcher().parse(command, new CommandContext<CommandAutoCompleteInteractionEvent>(e));
			List<Suggestion> suggestions;
			try {
				suggestions = Commands.DISPATCHER.getDispatcher().getCompletionSuggestions(parseResults, command.length()).get().getList();
			} catch (InterruptedException | ExecutionException ex) {
				ex.printStackTrace();
				return;
			}
			if(suggestions.size() > 25) {
				suggestions = suggestions.subList(0, 25);
			}
			AutoCompleteCallbackAction action = e.replyChoices();
			for(Suggestion suggestion : suggestions) {
				if(suggestion.getTooltip() != null) {
					System.out.println("Tooltip is \"" + suggestion.getTooltip() + "\"");
					action.addChoice(suggestion.getTooltip().getString(), suggestion.getTooltip().getString());
				}
				else {
					action.addChoice(suggestion.getText(), suggestion.getText());
				}
			}
			action.queue();
		}
	}
	
	private static String getCommand(CommandInteractionPayload e) {
		StringBuilder b = new StringBuilder(e.getName());
		for(OptionMapping option : e.getOptions()) {
			b.append(' ');
			b.append(option.getAsString());
		}
		return b.toString();
	}
	
	private static int getDepth(CommandNode<CommandContext> node) {
		int maxDepth = 0;
		
		for(CommandNode child : node.getChildren()) {
			maxDepth = Math.max(maxDepth, getDepth(child));
		}
		
		return maxDepth + 1;
	}
	
}
