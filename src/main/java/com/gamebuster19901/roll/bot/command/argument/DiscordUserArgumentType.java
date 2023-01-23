package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.entities.User;

public class DiscordUserArgumentType implements ArgumentType<User>{
	
	@Override
	public <S> User parse(S context, StringReader reader) throws CommandSyntaxException {
		String s = Commands.readString(reader);
		long id;
		try {
			if(s.length() > 3) {
				if(s.startsWith("<@")) {
					id = Long.parseLong(s.substring(2, s.length() - 1));
				}
				else {
					id = Long.parseLong(s);
				}
				return Main.discordBot.jda.retrieveUserById(id).complete();
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		throw ParseExceptions.NONEXISTANT.create("Discord User", s);
	}
	
}
