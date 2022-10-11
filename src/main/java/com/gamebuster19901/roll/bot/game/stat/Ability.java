package com.gamebuster19901.roll.bot.game.stat;

import com.gamebuster19901.roll.bot.game.Proficientable;
import com.gamebuster19901.roll.bot.game.Statistic;
import com.gamebuster19901.roll.bot.game.character.Proficiency;
import com.gamebuster19901.roll.bot.game.character.Stat;

public enum Ability implements Statistic, Proficientable {

	Strength("Str"),
	Dexterity("Dex"),
	Constitution("Con"),
	Intelligence("Int"),
	Wisdom("Wis"),
	Charisma("Cha");

	public final String shortHand;
	
	Ability(String shortHand) {
		this.shortHand = shortHand;
	}
	
	public Stat getStat() {
		return Stat.of(this);
	}
	
	public Stat getModStat() {
		return Stat.modOf(this);
	}
	
	public Stat getProficiencyStat() {
		return Proficiency.of(this);
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
