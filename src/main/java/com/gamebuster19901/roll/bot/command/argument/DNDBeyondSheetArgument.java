package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.game.beyond.character.DNDBeyondWebCharacterSheet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DNDBeyondSheetArgument implements ArgumentType<DNDBeyondWebCharacterSheet>{

	public static final DNDBeyondSheetArgument DND_BEYOND_WEB_SHEET_TYPE = new DNDBeyondSheetArgument();
	
	public static final String DND_BEYOND_SHEET = "https://www.dndbeyond.com/characters/";
	
	private DNDBeyondSheetArgument() {}
	
	@Override
	public <S> DNDBeyondWebCharacterSheet parse(S context, StringReader reader) throws CommandSyntaxException {
		String sheet = Commands.readString(reader);
		if(!sheet.startsWith("https://")) {
			sheet = "https://" + sheet;
		}
		if(sheet.startsWith(DND_BEYOND_SHEET)) {
			new DNDBeyondWebCharacterSheet(sheet);
		}
		throw new IllegalArgumentException("Not a DNDBeyond character sheet url: " + sheet);
	}

}
