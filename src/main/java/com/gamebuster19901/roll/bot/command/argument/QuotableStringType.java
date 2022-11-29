package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.Commands;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class QuotableStringType implements ArgumentType<String> {

	public static final QuotableStringType TYPE = new QuotableStringType();
	
	private QuotableStringType() {}
	
	@Override
	public <S> String parse(S context, StringReader reader) throws CommandSyntaxException {
		if(reader.peek() == '"') {
			return Commands.readQuotedString(reader);
		}
		return Commands.readString(reader);
	}

}
