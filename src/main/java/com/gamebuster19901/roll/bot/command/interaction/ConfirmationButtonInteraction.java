package com.gamebuster19901.roll.bot.command.interaction;

import static com.gamebuster19901.roll.bot.command.interaction.ConfirmationThread.PREFIX;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.util.ThreadService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;

public class ConfirmationButtonInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("overwrite")
			.then(Commands.argument("id", LongArgumentType.longArg(0, Integer.MAX_VALUE / 2))
				.then(Commands.literal("confirm")
					.executes((context) -> {
						ConfirmationThread t = ThreadService.getThread(PREFIX + context.getArgument("id", Long.class), ConfirmationThread.class);
						if(t != null) {
							t.accept();
						}
						return 1;
					})
				)
			
				.then(Commands.literal("reject")
					.executes((context) -> {
						ConfirmationThread t = ThreadService.getThread(PREFIX + context.getArgument("id", Long.class), ConfirmationThread.class);
						if(t != null) {
							t.reject();
						}
						return 1;
					})
				)
			)
		);
	}
	
}
