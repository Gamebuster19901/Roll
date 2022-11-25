package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class AbilityArgumentType implements ArgumentType<Ability> {

	public static final AbilityArgumentType ANY_ABILITY = AbilityArgumentType.of(Ability.values());
	
	public Ability[] abilities;
	
	private AbilityArgumentType(Ability[] abilities) {
		this.abilities = abilities;
	}

	@Override
	public <S> Ability parse(S context, StringReader reader) throws CommandSyntaxException {
		String lookingFor = Commands.readString(reader);
		for(Ability ability : abilities) {
			if(ability.name().equalsIgnoreCase(lookingFor) || ability.shortHand.equalsIgnoreCase(lookingFor)) {
				return ability;
			}
		}
		throw ParseExceptions.INVALID_ABILITY.create(lookingFor);
	}

	private static AbilityArgumentType of(Ability[] abilities) {
		if(abilities.length > 0) {
			return new AbilityArgumentType(abilities);
		}
		throw new IllegalArgumentException("Argument must accept at least one ability!");
	}

}
