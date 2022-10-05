package com.gamebuster19901.excite.bot.game.stat;

public enum Ability {

	STRENGTH("STR"),
	DEXTERITY("DEX"),
	CONSTITUTION("CON"),
	INTELLIGENCE("INT"),
	WISDOM("WIS"),
	CHARISMA("CHA");

	public final String shortHand;
	
	Ability(String shortHand) {
		this.shortHand = shortHand;
	}
	
	public static Ability getAbility(String ability) {
		for(Ability a : values()) {
			if(a.name().equalsIgnoreCase(ability) || a.shortHand.equalsIgnoreCase(ability)) {
				return a;
			}
		}
		throw new IllegalArgumentException("Unknown ability: " + ability);
	}
	
}
