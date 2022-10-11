package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;

public class Proficiency extends Stat {

	Proficiency(String name) {
		super(name);
	}
	
	@Deprecated
	public static Proficiency of(Skill skill) {
		return new Proficiency(skill.getName() + " proficiency");
	}
	
	@Deprecated
	public static Proficiency of(Ability ability) {
		return new Proficiency(ability.name() + " proficiency");
	}
	
	/*
	
	@Deprecated
	public static Proficiency of(WeaponType weaponType) {
		
	}
	
	@Deprecated
	public static Proficiency of(Item item) {
		
	}
	
	*/

}
