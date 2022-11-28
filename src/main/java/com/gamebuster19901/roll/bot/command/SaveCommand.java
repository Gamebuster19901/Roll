package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.SaveArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.SaveType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class SaveCommand {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		
		dispatcher.register(Commands.global("save")
			.then(Commands.argument("save", SaveArgumentType.ANY_SAVE)
				.executes((context) -> {
					return rollSave(context.getSource(), context.getArgument("save", SaveType.class));
				})
			)
		);
		
	}
	
	
	static int rollSave(CommandContext<IReplyCallback> c, SaveType save) {
		if(hasActiveCharacter(c)) {
			Statted character = Users.getActiveCharacter(c.getAuthor());
			Dice dice;
			if(save != SaveType.DEATH_SAVE) {
				dice = new Dice("d20+" + save.getName() + " Saving Throw");
			}
			else {
				dice = new Dice("d20");
			}
			RollCommand.roll(c, save.getName() + " Saving Throw", dice, character);
		}
		return 1;
	}
	
	private static boolean hasActiveCharacter(CommandContext<IReplyCallback> c) {
		boolean ret = Users.hasActiveCharacter(c.getAuthor());
		if(ret == false) {
			c.getEvent(IReplyCallback.class).reply("You cannot roll a save, you have no active character!").queue();
		}
		return ret;
	}
	
}
