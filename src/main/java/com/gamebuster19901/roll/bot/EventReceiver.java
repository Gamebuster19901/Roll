package com.gamebuster19901.roll.bot;

import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.GlobalLiteralArgumentBuilder.GlobalLiteralCommandNode;
import com.gamebuster19901.roll.bot.command.interaction.Interactions;
import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.Database;
import com.gamebuster19901.roll.util.StacktraceUtil;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class EventReceiver extends ListenerAdapter {

	private final Sub sub = new Sub();
	
	EventReceiver(){}
	
	@Override
	public void onGenericEvent(GenericEvent ge) {
		if(ge instanceof GenericInteractionCreateEvent) {
			GenericInteractionCreateEvent e = (GenericInteractionCreateEvent) ge;
			try {
				if(!Table.existsWhere(Table.PLAYERS, new Comparison(Column.DISCORD_ID, Comparator.EQUALS, e.getUser().getIdLong()))) {
					Insertion.insertInto(Table.PLAYERS).setColumns(Column.DISCORD_ID).to(e.getUser().getIdLong()).prepare(Database.INSTANCE).execute();
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
				context.sendMessage(StacktraceUtil.getStackTrace(t));
				throw new RuntimeException(t);
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
				if(command instanceof GlobalLiteralCommandNode && !Main.discordBot.isDev()) {
					return; //Don't register global commands as guild commands if we're not in a dev environment
				}
				SlashCommandData data = net.dv8tion.jda.api.interactions.commands.build.Commands.slash(command.getName(), command.getUsageText());
				
				if(command.getChildren().size() > 0) {
					data.addOption(OptionType.STRING, "argument", "argument");
				}
				
				commands.add(data);
				System.out.println(command.getUsageText());
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
	}
	
}
