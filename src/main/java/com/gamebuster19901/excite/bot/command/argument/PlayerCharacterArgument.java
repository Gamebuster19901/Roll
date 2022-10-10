package com.gamebuster19901.excite.bot.command.argument;

import com.gamebuster19901.excite.bot.command.Commands;
import com.gamebuster19901.excite.bot.game.beyond.character.DNDBeyondPDFPlayerBuilder;
import com.gamebuster19901.excite.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class PlayerCharacterArgument implements ArgumentType<PlayerCharacter>{

	public static final String DND_BEYOND_PDF = "https://www.dndbeyond.com/sheet-pdfs/";
	public static final String PDF = ".pdf";
	
	@Override
	public <S> PlayerCharacter parse(S context, StringReader reader) throws CommandSyntaxException {
		String character = Commands.readString(reader);
		if(!character.startsWith("https://")) {
			character = "https://" + character;
		}
		if(character.startsWith(DND_BEYOND_PDF) && character.endsWith(PDF)) {
			return new DNDBeyondPDFPlayerBuilder(character).build();
		}
		throw new Error();
	}
	
}
