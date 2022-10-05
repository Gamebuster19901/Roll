package com.gamebuster19901.excite.bot.game.stat;

import static com.gamebuster19901.excite.bot.game.stat.Ability.*;

public class Skill {
	
	public static final Skill ACROBATICS = new Skill("Acrobatics", DEXTERITY);
	public static final Skill ANIMAL_HANDLING = new Skill("Animal Handling", WISDOM);
	public static final Skill ARCANA = new Skill("Arcana", INTELLIGENCE);
	public static final Skill ATHLETICS = new Skill("Athletics", STRENGTH);
	public static final Skill DECEPTION = new Skill("Deception", CHARISMA);
	public static final Skill HISTORY = new Skill("History", INTELLIGENCE);
	public static final Skill INSIGHT = new Skill("Insight", WISDOM);
	public static final Skill INTIMIDATION = new Skill("Intimidation", CHARISMA);
	public static final Skill INVESTIGATION  = new Skill("Investigation", INTELLIGENCE);
	public static final Skill MEDICINE = new Skill("Medicine", WISDOM);
	public static final Skill NATURE = new Skill("Nature", INTELLIGENCE);
	public static final Skill PERCEPTION = new Skill("Perception", WISDOM);
	public static final Skill PERFORMANCE = new Skill("Performance", CHARISMA);
	public static final Skill PERSUASION = new Skill("Persuasion", CHARISMA);
	public static final Skill RELIGION = new Skill("Religion", INTELLIGENCE);
	public static final Skill SLIGHT_OF_HAND = new Skill("Slight of Hand", DEXTERITY);
	public static final Skill STEALTH = new Skill("Stealth", DEXTERITY);
	public static final Skill SURVIVAL = new Skill("Survival", WISDOM);
	
	public static final Skill[] DEFAULT_SKILLS = new Skill[] {
		ACROBATICS,
		ANIMAL_HANDLING,
		ARCANA,
		ATHLETICS,
		DECEPTION,
		HISTORY,
		INSIGHT,
		INTIMIDATION,
		INVESTIGATION,
		MEDICINE,
		NATURE,
		PERCEPTION,
		PERFORMANCE,
		PERSUASION,
		RELIGION,
		SLIGHT_OF_HAND,
		STEALTH,
		SURVIVAL
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
	
}
