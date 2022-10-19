package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.game.beyond.character.DNDBeyondPDFPlayerBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DNDBeyondPDFArgument implements ArgumentType<DNDBeyondPDFPlayerBuilder>{

	public static final DNDBeyondPDFArgument DND_BEYOND_PDF_TYPE = new DNDBeyondPDFArgument();
	
	public static final String DND_BEYOND_PDF = "https://www.dndbeyond.com/sheet-pdfs/";
	public static final String PDF = ".pdf";
	
	private DNDBeyondPDFArgument() {}
	
	@Override
	public <S> DNDBeyondPDFPlayerBuilder parse(S context, StringReader reader) throws CommandSyntaxException {
		String character = Commands.readString(reader);
		if(!character.startsWith("https://")) {
			character = "https://" + character;
		}
		if(character.startsWith(DND_BEYOND_PDF) && character.endsWith(PDF)) {
			return new DNDBeyondPDFPlayerBuilder(character);
		}
		throw new Error(character);
	}
	
}
