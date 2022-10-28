package com.gamebuster19901.roll.bot.game.stat;

import java.util.HashMap;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.util.TriFunction;

/**
 * Character stats that are fixed and will never be changed by the game state.
 * 
 * Used for importing from a character sheet to allow for a very simple rolling implementation 
 * in discord without having to deal with complex realtime changes to character stats.
 * 
 * In order for these stats to change, the character must be reimported with an updated sheet.
 * 
 * This Stats implementation ignores the game state entirely, and will always use values that were
 * provided on the character sheet.
 * 
 * This implementation is not intended to handle character resources, such as hp tracking, inventory, spells lots, currency, etc.
 * any attempts to do so will result in undefined behavior.
 *
 */
public class FixedStats extends Stats {
	
	final int ac, maxHP, tempHP, exp, totalLevel, initiative, proficiencyBonus;
	final Dice hitDice;
	final HashMap<MovementType, Integer> speed;
	final HashMap<Ability, Integer> stats, statMods;
	final HashMap<Ability, ProficiencyLevel> statProficiencies;
	final HashMap<Skill, Integer> skills;
	final HashMap<Skill, ProficiencyLevel> skillProficiencies;
	
	public FixedStats( //yikes this is ugly
		String name, 
		int ac,
		int hp, 
		int maxHP, 
		int tempHP, 
		HashMap<MovementType, Integer> speed, 
		HashMap<Ability, Integer> stats, 
		HashMap<Ability, Integer> statMods,
		HashMap<Ability, ProficiencyLevel> statProficiencies, 
		HashMap<Skill, Integer> skills,
		HashMap<Skill, ProficiencyLevel> skillProficiencies,
		int exp,
		int totalLevel,
		int initiative,
		int proficiencyBonus,
		Dice hitDice	
	) {
		super(name);
		this.ac = ac;
		this.maxHP = maxHP;
		this.tempHP = tempHP;
		this.speed = speed;
		this.stats = stats;
		this.statMods = statMods;
		this.statProficiencies = statProficiencies;
		this.skills = skills;
		this.skillProficiencies = skillProficiencies;
		this.exp = exp;
		this.totalLevel = totalLevel;
		this.initiative = initiative;
		this.proficiencyBonus = proficiencyBonus;
		this.hitDice = hitDice;
	}

	@Override
	public int getHP(GameLayer layer) {
		return maxHP;
	}
	
	@Override
	public int getMaxHP(GameLayer layer) {
		return maxHP;
	}

	@Override
	public int getTempHP(GameLayer layer) {
		return tempHP;
	}

	@Override
	public int getSpeed(GameLayer layer, MovementType movementType) {
		Integer ret;
		ret = speed.get(movementType);
		return ret != null  && ret > -1? ret : 0;
	}

	@Override
	public int getAC(GameLayer layer) {
		return ac;
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		if(stat.isAbilityScoreStat()) {
			if(stat.isAbilityModifierStat()) {
				return (T) statMods.get(Ability.fromStat(stat));
			}
			return (T) stats.get(Ability.fromStat(stat));
		}
		if(stat.isSkillStat()) {
			return (T) skills.get(Skill.fromStat(stat));
		}
		if(stat.isSpeedStat()) {
			return (T) speed.get(MovementType.fromStat(stat));
		}
		if(stat.equals(Stat.AC)) {
			return (T)(Object)getAC();
		}
		if(stat.equals(Stat.HP)) {
			return (T)(Object)getMaxHP();
		}
		if(stat.equals(Stat.Temp_HP)) {
			return (T)(Object)getTempHP();
		}

		return null;
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		//NO-OP
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		//NO-OP
	}

}
