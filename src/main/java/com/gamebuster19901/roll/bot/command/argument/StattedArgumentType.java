package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface StattedArgumentType extends ArgumentType<Statted> {

	public static final StattedArgumentType CHARACTER_NULLABLE = (context, reader) -> {
		long id = reader.readLong();
		if(PlayerCharacter.exists(id)) {
			return PlayerCharacter.deserialize(id);
		}
		return null;
	};
	
	public static final StattedArgumentType CHARACTER = (context, reader) -> {
		long id = reader.readLong();
		if(PlayerCharacter.exists(id)) {
			return PlayerCharacter.deserialize(id);
		}
		throw ParseExceptions.NONEXISTANT.create("Character", id);
	};
	
	@Override
	public abstract Statted parse(Object context, StringReader reader) throws CommandSyntaxException;

}
