package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.Commands;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.entities.User;

public class DiscordUserArgumentType implements ArgumentType<User>{

	@Override
	public <S> User parse(S context, StringReader reader) throws CommandSyntaxException {
		String s = Commands.readString(reader);
		if(s.startsWith("<@")) {
			
		}
		if(Integer.parseInt(s))
	}

}
