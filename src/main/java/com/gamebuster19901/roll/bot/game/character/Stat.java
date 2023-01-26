package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statistic;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;

public class Stat implements Statistic {

	public static final Stat Name = new Stat("Name");
	public static final Stat HP = new Stat("HP");
	public static final Stat Max_HP = new Stat("Max HP");
	public static final Stat Temp_HP = new Stat("Temp HP");
	public static final Stat AC = new Stat("AC");
	public static final Stat EXP = new Stat("EXP");
	public static final Stat Proficiency = new Stat("Proficiency");
	public static final Stat Initiative = new Stat("Initiative");
	public static final Stat Proficiency_Bonus = new Stat("Proficiency Bonus");
	public static final Stat Hit_Dice = new Stat("Hit Dice");
	public static final Stat Copper = new Stat("Copper");
	public static final Stat Silver = new Stat("Silver");
	public static final Stat Electrum = new Stat("Electrum");
	public static final Stat Gold = new Stat("Gold");
	public static final Stat Platinum = new Stat("Platinum");
	
	/*
	 * Player character only stats:
	 */
	public static final Stat Owner = new Stat("Owner");
	public static final Stat ID = new Stat("ID");
	
	String name;
	String suggestion;
	

	public Stat(String name) {
		this(name, name);
	}
	
	@SuppressWarnings("null")
	public Stat(String name, String suggestion) {
		if(name == null) {
			name.getBytes(); //intentionally throw NPE
		}
		if(suggestion == null) {
			suggestion.getBytes(); //intentionally throw NPE
		}
		this.name = name;
		this.suggestion = suggestion;
	}

	public String getName() {
		return name;
	}
	
	public String getSimpleName() {
		return  name;
	}
	
	public String getSuggestion() {
		return suggestion;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Stat) {
			return getName().equalsIgnoreCase(((Stat) o).getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.toLowerCase().hashCode();
	}
	
	@Deprecated
	public static Stat of(Skill skill) {
		return new SkillStat(skill.getName());
	}
	
	@Deprecated
	public static Stat modOf(Skill skill) {
		return new SkillStat(skill.getName());
	}
	
	@Deprecated
	public static Stat of(Ability ability) {
		return new AbilityScoreStat(ability.name());
	}
	
	@Deprecated
	public static Stat modOf(Ability ability) {
		return new AbilityModStat(ability.name() + " Mod");
	}
	
	public static Stat fromUserInput(String stat) {
		if(stat.equalsIgnoreCase("hp")) {
			return HP;
		}
		if(stat.equalsIgnoreCase("prof") || stat.equalsIgnoreCase("proficiency")) {
			return Proficiency;
		}
		if(stat.equalsIgnoreCase("init") || stat.equalsIgnoreCase("initiative")) {
			return Initiative;
		}
		if(stat.equalsIgnoreCase("ac")) {
			return AC;
		}
		if(stat.equalsIgnoreCase("xp") || stat.equalsIgnoreCase("exp")) {
			return EXP;
		}
		Ability ability = Ability.getIfAbility(stat);
		if(ability != null) {
			return ability.getModStat();
		}
		Skill skill = Skill.getIfSkill(stat);
		if(skill != null) {
			return skill.getStat();
		}
		return new Stat(stat);
	}

	@Override
	public Stat getStat() {
		return this;
	}
	
	public String toString() {
		return name;
	}
	
	public boolean isSpeedStat() {
		return this instanceof MovementType.MovementStat;
	}
	
	public boolean isSkillStat() {
		return this instanceof SkillStat;
	}
	
	public boolean isAbilityScoreStat() {
		return this instanceof AbilityScoreStat;
	}
	
	public boolean isAbilityModifierStat() {
		return this instanceof AbilityModStat;
	}
	
	private static final class AbilityScoreStat extends Stat {
		public AbilityScoreStat(String name) {
			super(name, name);
		}
		
		@Override
		public String getSimpleName() {
			return name + " Score";
		}
	}
	
	public static final class AbilityModStat extends Stat {
		public AbilityModStat(String name) {
			super(name, name);
		}
		
		@Override
		public String getSimpleName() {
			return name + " Mod";
		}
	}
	
	private static final class SkillStat extends Stat {
		public SkillStat(String name) {
			super(name);
		}
	}
}
