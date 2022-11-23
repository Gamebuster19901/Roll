package com.gamebuster19901.roll.bot.database;

import static com.gamebuster19901.roll.bot.database.Table.*;

import java.util.Arrays;
import java.util.HashSet;

public enum Column implements Columns {

	//MULTIPLE TABLES:
	ALL_COLUMNS("*", Table.values()),
	GENERATED_KEY("GENERATED_KEY", Table.values()), //Used for getting generated key
	NAME("name", CAMPAIGNS, CHARACTERS),
	
	DISCORD_ID("discordID", CHARACTERS, CLASSES, THEMES, PLAYERS, RACES),
	OWNER_ID("ownerID", CAMPAIGNS),
	
	/*
	 * Campaigns
	 */
	CAMPAIGN_ID("campaignID", CAMPAIGNS, CAMPAIGN_PLAYERS),
	
	
	/*
	 * Characters
	 */
	CHARACTER_ID("characterID", CHARACTERS),
	
	/*
	 * Classes
	 */
	CLASS_ID("classID", CLASSES),
	
	
	
	/*
	 * Players
	 */
	//CURRENT_CHARACTER("currentCharacter"),
	PLAYER_ID("playerID", CAMPAIGN_PLAYERS),
	
	/*
	 * Races
	 */
	RACE_ID("raceID", RACES),
	
	/*
	 * THEMES
	 */
	DIE_COLOR("dieColor", THEMES),
	TEXT_COLOR("textColor", THEMES),
	GOOD_COLOR("goodColor", THEMES),
	BAD_COLOR("badColor", THEMES),
	DEBUG("Debug", THEMES)
	
	;
	
	private final String name;
	private final HashSet<Table> validTables = new HashSet<Table>();
	
	private Column(String name, Table...tables ) {
		if(tables == null || tables.length == 0) {
			throw new VerifyError("Column `" + name + "` is not part of any table!");
		}
		this.name = name;
		validTables.addAll(Arrays.asList(tables));
	}
	
	public String toString() {
		return name;
	}
	
	public boolean isInTable(Table table) {
		return validTables.contains(table);
	}
	
	public static Column getColumn(String dbName) {
		for(Column column : values()) {
			if(column.name.equals(dbName)) {
				return column;
			}
		}
		throw new IllegalArgumentException("No column with name `" + dbName + "`");
	}
	
	@Override
	public String asString() {
		return toString();
	}

	@Override
	public Column[] getColumns() {
		return new Column[]{this};
	}

}
