package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.embed.StatEmbedPage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class StatPageArgumentType implements ArgumentType<StatEmbedPage> {
	
	public static final StatPageArgumentType ARGUMENT = new StatPageArgumentType();
	
	private StatPageArgumentType() {}
	
	@Override
	public <S> StatEmbedPage parse(S context, StringReader reader) throws CommandSyntaxException {
		return StatEmbedPage.valueOf(reader.readString());
	}

}
