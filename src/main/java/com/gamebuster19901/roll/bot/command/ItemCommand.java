package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.QuotableStringType;
import com.mojang.brigadier.CommandDispatcher;

public class ItemCommand {
	
	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("item").requires(
			(context) -> {
				return context.getAuthor().getIdLong() == 0;
			}
		)
		
		.then(
			Commands.argument("name", QuotableStringType.TYPE)
				.executes((context) -> {
					return 1;
				})
		));
	}
}

