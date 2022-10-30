package com.gamebuster19901.roll.bot.game.beyond.character;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.roll.bot.command.argument.DNDBeyondPDFArgument;
import com.gamebuster19901.roll.bot.game.character.FixedPlayerCharacterStatBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerBuilder;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.pdf.PDFText;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.entities.User;

public class DNDBeyondPDFPlayerBuilder extends PlayerBuilder {

	public static final Pattern PDF_OBJECT_PATTERN = Pattern.compile("\\d* \\d* obj.*?endobj", Pattern.DOTALL);
	private static final long maxID = Long.MAX_VALUE / 2;
	
	long characterID = -1;
	
	public DNDBeyondPDFPlayerBuilder(User owner, String sheet) throws CommandSyntaxException {
		super(owner, getStats(sheet));
		characterID = Long.parseLong(sheet.substring(sheet.lastIndexOf('_') + 1).replace(DNDBeyondPDFArgument.PDF, ""));
	}
	
	private static FixedPlayerCharacterStatBuilder getStats(String charSheet) {
		FixedPlayerCharacterStatBuilder statBuilder = new FixedPlayerCharacterStatBuilder();
		try {
			if(!charSheet.startsWith("https://")) {
				charSheet = charSheet + "https://";
			}

			URL url = new URL(charSheet);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			Matcher matcher = PDF_OBJECT_PATTERN.matcher(new String(is.readAllBytes()));
			
			int count = 0;
			while(matcher.find()) {
				count++;
				String result = matcher.toMatchResult().group();
				if(result.contains("\n/FT/Tx\n")) {
					PDFText text = new PDFText(result);
					DNDBeyondPDFValue val = DNDBeyondPDFValue.getValueType(text);
					if(val == null) {
						System.out.println("I don't know how to handle a " + text.getName() + " value, ignoring");
					}
					else {
						switch(val) {
							case CHARACTER_NAME:
								statBuilder.addStat(new StatValue(val.getStat(), StatSource.of(GameLayer.CHOSEN, "The name you chose for your character."), val.parse(text)));
								break;
							case DEFENSES:
							case SAVE_MODS:
							case SENSES:
							case MOVEMENT:
							case HIT_DICE_REMAINING:
							case PROFICIENCIES_AND_LANGUAGES:
							case SEX:
							case SIZE:
							case HEIGHT:
							case WEIGHT:
							case FAITH:
							case SKIN:
							case EYES:
							case HAIR:
							case PASSIVE_PERCEPTION:
							case PASSIVE_INSIGHT:
							case PASSIVE_INVESTIGATION:
								System.out.println(text.getName() + " not implemented yet, ignoring");
								break;
							default:
								statBuilder.addStat(new StatValue(val.getStat(), StatSource.of(GameLayer.CHOSEN, "D&D Beyond imported value"), val.parse(text)));
						}
					}
					System.out.println(text.getName() + ": " + text);
				}
			}
			
			long id = Long.parseLong(charSheet.substring(charSheet.lastIndexOf('_') + 1).replace(DNDBeyondPDFArgument.PDF, ""));
			if(id <= maxID) {
				statBuilder.addStat(new StatValue(Stat.ID, StatSource.of(GameLayer.DATABASE, "D&D Beyond character id"), id));
			}
			else {
				throw new IllegalStateException("D&D Beyond character id (" + id + "exceeded maximum expected value?! This is a critical issue! Report immediately!");
			}

		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return statBuilder;
	}

	public long getID() {
		return characterID;
	}
	
}
