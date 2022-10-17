package com.gamebuster19901.roll.bot;

import java.util.ArrayList;
import java.util.List;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.interaction.Interactions;
import com.gamebuster19901.roll.util.StacktraceUtil;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class EventReceiver extends ListenerAdapter {

	EventReceiver(){}
	
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
		} catch (CommandSyntaxException ex) {
			context.sendMessage(StacktraceUtil.getStackTrace(ex));
		}
	}
	
	@Override
	public void onGuildReady(GuildReadyEvent e) {
		List<CommandData> commands = new ArrayList<>();
		Commands.DISPATCHER.getDispatcher().getRoot().getChildren().forEach((command) -> {
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
