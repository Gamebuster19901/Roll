package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.AbilityArgumentType;
import com.gamebuster19901.roll.bot.command.argument.SkillArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.gamebuster19901.roll.bot.game.user.Users;
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
			.then(Commands.argument("skill", SkillArgumentType.ANY_SKILL)
				.executes((context) -> {
					return rollCheck(context.getSource(), context.getArgument("skill", Skill.class));
				})
			)
		);
		
	}
	
	
	static int rollCheck(CommandContext<IReplyCallback> c, Ability ability) {
		if(hasActiveCharacter(c)) {
			Statted character = Users.getActiveCharacter(c.getAuthor());
			Dice dice = new Dice("d20+" + ability.shortHand);
			RollCommand.roll(c, ability.name() + " check", dice, character);
		}
		return 1;
	}
	
	static int rollCheck(CommandContext<IReplyCallback> c, Skill skill) {
		if(hasActiveCharacter(c)) {
			Statted character = Users.getActiveCharacter(c.getAuthor());
			Dice dice = new Dice("d20+" + skill.getName());
			RollCommand.roll(c, skill.getName() + " check", dice, character);
		}
		return 1;
	}
	
	static boolean hasActiveCharacter(CommandContext<IReplyCallback> c) {
		boolean ret = Users.hasActiveCharacter(c.getAuthor());
		if(ret == false) {
			c.getEvent(IReplyCallback.class).reply("You cannot roll a check, you have no active character!").queue();
		}
		return ret;
	}
	
}
