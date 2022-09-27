package com.gamebuster19901.excite.bot.game;

public enum Ability implements Stat {

	Strength,
	Dexterity,
	Constitution,
	Intelligence,
	Wisdom,
	Charisma;

	@Override
	public String shortName() {
		return name().substring(0,2).toUpperCase();
	}

	@Override
	public String getName() {
		return name();
	}
	
}
