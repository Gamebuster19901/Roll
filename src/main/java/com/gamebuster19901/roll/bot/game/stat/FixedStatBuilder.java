package com.gamebuster19901.roll.bot.game.stat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.util.TriFunction;
import com.google.common.collect.ImmutableMap;

public class FixedStatBuilder extends Stats implements StatBuilder<FixedStats> {
	
	public transient final HashSet<Stat> requiredStats = new HashSet<Stat>();
	{
		requiredStats.addAll(Arrays.asList(new Stat[] {
			Stat.Name,
			Stat.AC,
			Stat.EXP,
			Stat.HP,
			Stat.Max_HP,
			Stat.Initiative,
			Stat.Proficiency_Bonus,
			MovementType.Walking.getStat()
		}));
		for(Ability ability : Ability.values()) {
			requiredStats.add(ability.getStat());
			requiredStats.add(ability.getModStat());
			requiredStats.add(ability.getProficiencyStat());
		}
		for(Skill skill : Skill.DEFAULT_SKILLS) {
			requiredStats.add(skill.getStat());
			requiredStats.add(skill.getProficiencyStat());
		}
		
	};
	
	public transient HashSet<StatValue<?>> defaultStats = new HashSet<StatValue<?>>();;
	{
		defaultStats.add(new StatValue<Integer>(MovementType.Burrowing.getStat(), StatSource.of(GameLayer.GAME_RULE, ""), 0));
		defaultStats.add(new StatValue<Integer>(MovementType.Climbing.getStat(), StatSource.of(GameLayer.GAME_RULE, ""), 0));
		defaultStats.add(new StatValue<Integer>(MovementType.Flying.getStat(), StatSource.of(GameLayer.GAME_RULE, ""), 0));
		defaultStats.add(new StatValue<Integer>(MovementType.Swimming.getStat(), StatSource.of(GameLayer.GAME_RULE, ""), 0));
	}
	HashMap<Stat, StatValue<?>> stats = new LinkedHashMap<>();

	@Override
	public int getHP(GameLayer layer) {
		return getStat(Stat.HP, int.class);
	}

	@Override
	public int getMaxHP(GameLayer layer) {
		return stats.get(Stat.Max_HP).getValueAs(int.class);
	}

	@Override
	public int getTempHP() {
		return stats.get(Stat.Temp_HP).getValueAs(int.class);
	}

	@Override
	public int getSpeed(GameLayer layer, MovementType movementType) {
		return stats.get(movementType.getStat()).getValueAs(int.class);
	}

	@Override
	public int getAC(GameLayer layer) {
		return stats.get(Stat.HP).getValueAs(int.class);
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		return stats.get(stat).getValueAs(type);
	}
	
	@Override
	public boolean hasStat(GameLayer layer, Stat stat) {
		return stats.containsKey(stat);
	}

	@Override
	public boolean hasStatAt(GameLayer layer, Stat stat) { //fixed stats do not implement layer system
		return stats.containsKey(stat);
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		stats.put(value.getStat(), value);
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		//NO-OP
	}

	@Override
	public void validate() {
		for(StatValue value : defaultStats) {
			if(!stats.containsKey(value.getStat())) {
				stats.put(value.getStat(), value);
			}
		}
		for(Stat requiredValue : requiredStats) {
			if(stats.get(requiredValue) == null) {
				throw new IllegalStateException("Stats are missing `" + requiredValue.getName() + "` value!");
			}
		}
	}

	@Override
	public FixedStats build() {
		validate();
		return new FixedStats(stats);
	}
	
	@Override
	public ImmutableMap<Stat, StatValue<?>> getStats() {
		return ImmutableMap.copyOf(stats);
	}
	
	private HashMap<MovementType, Integer> getSpeeds() {
		HashMap<MovementType, Integer> ret = new HashMap<>();
		for(MovementType m : MovementType.values()) {
			ret.put(m, stats.get(m.getStat()).getValueAs(Integer.class));
		}
		return ret;
	}
	
	private HashMap<Ability, Integer> getAbilityStats() {
		HashMap<Ability, Integer> ret = new HashMap<>();
		for(Ability a : Ability.values()) {
			ret.put(a, stats.get(a.getStat()).getValueAs(Integer.class));
		}
		return ret;
	}
	
	private HashMap<Ability, Integer> getStatMods() {
		HashMap<Ability, Integer> ret = new HashMap<>();
		for(Ability a : Ability.values()) {
			ret.put(a, stats.get(a.getModStat()).getValueAs(Integer.class));
		}
		return ret;
	}
	
	private HashMap<Ability, ProficiencyLevel> getStatProficiencies() {
		HashMap<Ability, ProficiencyLevel> ret = new HashMap<>();
		for(Ability a : Ability.values()) {
			ret.put(a, stats.get(a.getProficiencyStat()).getValueAs(ProficiencyLevel.class));
		}
		return ret;
	}
	
	private HashMap<Skill, Integer> getSkills() {
		HashMap<Skill, Integer> ret = new HashMap<>();
		for(Skill s : Skill.DEFAULT_SKILLS) {
			ret.put(s, stats.get(s.getStat()).getValueAs(Integer.class));
		}
		return ret;
	}
	
	private HashMap<Skill, ProficiencyLevel> getSkillProficiencies() {
		HashMap<Skill, ProficiencyLevel> ret = new HashMap<>();
		for(Skill s : Skill.DEFAULT_SKILLS) {
			ret.put(s, stats.get(s.getProficiencyStat()).getValueAs(ProficiencyLevel.class));
		}
		return ret;
	}

}
