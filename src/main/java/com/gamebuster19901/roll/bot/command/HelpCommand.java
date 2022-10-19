package com.gamebuster19901.roll.bot.command;

import java.awt.Color;

import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;

class HelpCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("help").executes((context) -> {
			return sendHelpInfo(context.getSource());
		}));
	}
	
	@SuppressWarnings("rawtypes")
	private static int sendHelpInfo(CommandContext context) {
		EmbedBuilder embed = context.getEmbed();
		embed.appendDescription("For help, see [INSERT URL HERE]");
		embed.setColor(Color.WHITE);
		context.sendMessage(embed);
		return 1;
	}
	
}
