package com.gamebuster19901.roll.bot.game.beyond.character;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.roll.bot.game.character.PlayerBuilder;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.util.pdf.PDFText;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DNDBeyondPDFPlayerBuilder extends PlayerBuilder {

	public static final Pattern PDF_OBJECT_PATTERN = Pattern.compile("\\d* \\d* obj.*?endobj", Pattern.DOTALL);
	
	public DNDBeyondPDFPlayerBuilder(String charSheet) throws CommandSyntaxException {
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
								name = (String) val.parse(text);
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
								set(val.getStat(), val.parse(text));
						}
					}
					System.out.println(text.getName() + ": " + text);
				}
			}
			defaultStats.put(Stat.HP, get(Stat.Max_HP));
			if(get(Stat.Max_HP) == null) {
				throw new AssertionError("Max HP is null");
			}
			if(get(Stat.HP) == null) {
				throw new AssertionError("HP is null even though it was set");
			}
			System.out.println(count + " matches");
		}
		catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
