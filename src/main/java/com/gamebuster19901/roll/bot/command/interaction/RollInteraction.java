package com.gamebuster19901.roll.bot.command.interaction;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.RollCommand;
import com.gamebuster19901.roll.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.roll.bot.command.argument.StattedArgumentType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.mojang.brigadier.CommandDispatcher;

class RollInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("roll")
			.then(Commands.argument("statted", StattedArgumentType.CHARACTER)
				.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
					.executes((context) -> {
						Statted statted = context.getArgument("statted", Statted.class);
						return RollCommand.roll(context, statted);
					})	
				)	
			)
		);
	}
	
	
	
}
