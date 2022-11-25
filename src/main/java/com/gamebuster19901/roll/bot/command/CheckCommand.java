package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.AbilityArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

class CheckCommand {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		
		dispatcher.register(Commands.global("check")
			.then(Commands.argument("ability", AbilityArgumentType.ANY_ABILITY)
				.executes((context) -> {
					return rollCheck(context.getSource(), context.getArgument("ability", Ability.class));
				})
			)
		);
		
	}
	
	
	private static int rollCheck(CommandContext<IReplyCallback> c, Ability ability) {
		if(PlayerCharacter.hasActiveCharacter(c.getAuthor())) {
			Statted character = PlayerCharacter.getActiveCharacter(c.getAuthor());
			Dice dice = new Dice("d20+" + ability.shortHand);
			RollCommand.roll(c, ability.name() + " check", dice, character);
		}
		return 1;
	}
	
	
}
