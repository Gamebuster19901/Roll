package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.game.Dice;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DiceArgumentType implements ArgumentType<Dice> {

	public static final DiceArgumentType DICE_ARGUMENT_TYPE = new DiceArgumentType();
	
	private DiceArgumentType() {}
	
	@Override
	public <S> Dice parse(S context, StringReader reader) throws CommandSyntaxException {
		Dice dice = new Dice(reader);
		return dice;
	}
	
}
