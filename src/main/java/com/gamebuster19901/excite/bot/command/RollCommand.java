package com.gamebuster19901.excite.bot.command;

import com.gamebuster19901.excite.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.excite.bot.game.Dice;
import com.mojang.brigadier.CommandDispatcher;

public class RollCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("roll")
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					CommandContext c = context.getSource();
					Dice dice = context.getArgument("dice", Dice.class);
					dice.roll();
					int result = dice.getValue();
					c.getEmbed().appendDescription(dice.toString());
					c.getEmbed().appendDescription("\n\nResult: " + result);
					c.sendMessage(c.getEmbed());
					System.out.println(result);
					return 1;
				})
			)
		);
				
	}
	
}
