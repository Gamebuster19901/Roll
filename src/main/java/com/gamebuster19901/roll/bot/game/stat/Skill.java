package com.gamebuster19901.roll.bot.game.stat;

import static com.gamebuster19901.roll.bot.game.stat.Ability.*;

import com.gamebuster19901.roll.bot.game.Proficientable;
import com.gamebuster19901.roll.bot.game.Statistic;
import com.gamebuster19901.roll.bot.game.character.Proficiency;
import com.gamebuster19901.roll.bot.game.character.Stat;

public class Skill implements Statistic, Proficientable {
	
	public static final Skill Acrobatics = new Skill("Acrobatics", Dexterity);
	public static final Skill Animal_Handling = new Skill("Animal Handling", Wisdom);
	public static final Skill Arcana = new Skill("Arcana", Intelligence);
	public static final Skill Athletics = new Skill("Athletics", Strength);
	public static final Skill Deception = new Skill("Deception", Charisma);
	public static final Skill History = new Skill("History", Intelligence);
	public static final Skill Insight = new Skill("Insight", Wisdom);
	public static final Skill Intimidation = new Skill("Intimidation", Charisma);
	public static final Skill Investigation  = new Skill("Investigation", Intelligence);
	public static final Skill Medicine = new Skill("Medicine", Wisdom);
	public static final Skill Nature = new Skill("Nature", Intelligence);
	public static final Skill Perception = new Skill("Perception", Wisdom);
	public static final Skill Performance = new Skill("Performance", Charisma);
	public static final Skill Persuasion = new Skill("Persuasion", Charisma);
	public static final Skill Religion = new Skill("Religion", Intelligence);
	public static final Skill Sleight_of_Hand = new Skill("Slight of Hand", Dexterity);
	public static final Skill Stealth = new Skill("Stealth", Dexterity);
	public static final Skill Survival = new Skill("Survival", Wisdom);
	
	public static final Skill[] DEFAULT_SKILLS = new Skill[] {
		Acrobatics,
		Animal_Handling,
		Arcana,
		Athletics,
		Deception,
		History,
		Insight,
		Intimidation,
		Investigation,
		Medicine,
		Nature,
		Perception,
		Performance,
		Persuasion,
		Religion,
		Sleight_of_Hand,
		Stealth,
		Survival
	};
	
	final String name;
	final Ability ability;
	
	Skill(String name, Ability ability) {
		this.name = name;
		this.ability = ability;
	}
	
	Skill(String name) {
		this(name, null);
	}
	
	public String getName() {
		return name;
	}
	
	public Ability getAbility() {
		return ability;
	}
	
	public Stat getStat() {
		return Stat.of(this);
	}

	@Override
	public Stat getProficiencyStat() {
		return Proficiency.of(this);
	}
	
}
