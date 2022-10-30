package com.gamebuster19901.roll.bot.game.character;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.stat.FixedStatBuilder;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.TriFunction;

public class FixedPlayerCharacterStatBuilder extends PlayerCharacterStatBuilder<FixedPlayerCharacterStats> implements PlayerCharacterStats {

	private final FixedStatBuilder fixedStats;
	
	public FixedPlayerCharacterStatBuilder() {
		FixedStatBuilder fixedStats = new FixedStatBuilder();
		fixedStats.requiredStats.add(Stat.Owner);
		fixedStats.requiredStats.add(Stat.ID);
		this.fixedStats = fixedStats;
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		return fixedStats.getStat(layer, stat, type);
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		fixedStats.addStat(value, func);
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		fixedStats.setVariables(layer, expression);
	}

	@Override
	public void validate() {
		fixedStats.validate();
	}

	@Override
	public FixedPlayerCharacterStats build() {
		return new FixedPlayerCharacterStats(fixedStats.build());
	}

}
