package com.gamebuster19901.roll.bot.game.foreign.beyond.character;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.roll.util.URI;
import com.gamebuster19901.roll.bot.command.argument.DNDBeyondPDFArgument;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.character.FixedPlayerCharacterStatBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerBuilder;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.foreign.ForeignLocation;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;
import com.gamebuster19901.roll.util.pdf.PDFText;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.entities.User;

public class DNDBeyondPDFPlayerBuilder extends PlayerBuilder {

	public static final Pattern PDF_OBJECT_PATTERN = Pattern.compile("\\d* \\d* obj.*?endobj", Pattern.DOTALL);
	public static final Pattern MOVEMENT_PATTERN = Pattern.compile("(?<distance>\\d*)\\sft.\\s\\\\\\((?<type>\\w*)\\\\\\)"); // (?<distance>\d*)\sft.\s\\\((?<type>\w*)\\\)
	private static final long maxID = Long.MAX_VALUE / 2;
	
	private static final StatSource DND_BEYOND_CHAR_IMPORT = StatSource.of(GameLayer.CHOSEN, "D&D Beyond character imported value");
	
	long characterID = -1;
	
	public DNDBeyondPDFPlayerBuilder(User owner, String sheet) throws CommandSyntaxException {
		super(owner, getStats(owner, sheet));
		characterID = Long.parseLong(sheet.substring(sheet.lastIndexOf('_') + 1).replace(DNDBeyondPDFArgument.PDF, ""));
	}
	
	private static FixedPlayerCharacterStatBuilder getStats(User owner, String charSheet) {
		FixedPlayerCharacterStatBuilder statBuilder = new FixedPlayerCharacterStatBuilder();
		statBuilder.addStat(Stat.Owner, GameLayer.DATABASE, "D&D Beyond Character ID", owner.getIdLong());
		statBuilder.addStat(Stat.ForeignLocation, StatSource.DATABASE_SOURCE, ForeignLocation.DND_BEYOND.ordinal());
		try {
			if(!charSheet.startsWith("https://")) {
				charSheet = charSheet + "https://";
			}

			URL url = new URL(charSheet);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			Matcher matcher = PDF_OBJECT_PATTERN.matcher(new String(is.readAllBytes()));
			
			int maxHP = -1;
			int hp = -1;
			while(matcher.find()) {
				String result = matcher.toMatchResult().group();
				if(result.contains("\n/FT/Tx\n")) {
					PDFText text = new PDFText(result);
					DNDBeyondPDFValue val = DNDBeyondPDFValue.getValueType(text);

					if(val == null) {
						//System.out.println("I don't know how to handle a " + text.getName() + " value, ignoring");
					}
					else {
						switch(val) {
							case CHARACTER_NAME:
								statBuilder.addStat(val.getStat(), GameLayer.CHOSEN, "The name you chose for your character.", val.parse(text));
								break;
							case MOVEMENT:
								parseMovement(statBuilder, text);
								break;
							case MAX_HP:
								maxHP = (int) val.parse(text);
								break;
							case CURRENT_HP:
								hp = (int) val.parse(text);
								break;
							case DEFENSES:
							case SAVE_MODS:
							case SENSES:
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
								if(val.getStat() != null) {
									System.out.println("Adding " + val.getStat().getName());
									statBuilder.addStat(val.getStat(), GameLayer.CHOSEN, "D&D Beyond imported value", val.parse(text));
								}
								else {
									//System.out.println("Don't know how to add a " + text.getName() + " stat, ignoring");
								}
						}
					}
					System.out.println(text.getName() + ": " + text);
				}
			}
			
			if(hp < 0) {
				hp = maxHP;
			}
			
			statBuilder.addStat(Stat.Max_HP, DND_BEYOND_CHAR_IMPORT, maxHP);
			statBuilder.addStat(Stat.HP, DND_BEYOND_CHAR_IMPORT, hp);
			
			long id = Long.parseLong(charSheet.substring(charSheet.lastIndexOf('_') + 1).replace(DNDBeyondPDFArgument.PDF, ""));
			if(id <= maxID) {
				statBuilder.addStat(Stat.ID, DND_BEYOND_CHAR_IMPORT, id);
			}
			else {
				throw new IllegalStateException("D&D Beyond character id (" + id + ") exceeded maximum expected value?! This is a critical issue! Report immediately!");
			}
			statBuilder.addStat(Stat.URI, StatSource.DATABASE_SOURCE, new URI(url.toURI()));
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return statBuilder;
	}

	private static void parseMovement(FixedPlayerCharacterStatBuilder stats, PDFText text) {
		Matcher matcher = MOVEMENT_PATTERN.matcher(text.toString());
		matching:
		while(matcher.find()) {
			String speed = matcher.group("distance");
			String foundType = matcher.group("type");
			for(MovementType type : MovementType.values()) {
				if(type.name().equals(foundType)) {
					stats.addStat(type.getStat(), DND_BEYOND_CHAR_IMPORT, Integer.parseInt(speed));
					continue matching;
				}
			}
			throw new IllegalArgumentException("Unknown movement type " + foundType);
		}
		
	}

	public long getID() {
		return characterID;
	}
	
}
