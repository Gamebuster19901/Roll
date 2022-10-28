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
	
	public Stat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Stat) {
			return getName().equals(((Stat) o).getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
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
		return new AbilityStat(ability.name());
	}
	
	@Deprecated
	public static Stat modOf(Ability ability) {
		return new AbilityModStat(ability.name() + " Mod");
	}

	@Override
	public Stat getStat() {
		return this;
	}
	
	public boolean isSpeedStat() {
		return this instanceof MovementType.MovementStat;
	}
	
	public boolean isSkillStat() {
		return this instanceof SkillStat;
	}
	
	public boolean isAbilityScoreStat() {
		return this instanceof AbilityStat;
	}
	
	public boolean isAbilityModifierStat() {
		return this instanceof AbilityModStat;
	}
	
	private static final class AbilityStat extends Stat {
		public AbilityStat(String name) {
			super(name);
		}
	}
	
	public static final class AbilityModStat extends Stat {
		public AbilityModStat(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}
	}
	
	private static final class SkillStat extends Stat {
		public SkillStat(String name) {
			super(name);
		}
	}
}
