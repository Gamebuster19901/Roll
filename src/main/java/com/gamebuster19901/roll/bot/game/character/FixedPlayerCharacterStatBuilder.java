package com.gamebuster19901.roll.bot.game.character;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.stat.FixedStatBuilder;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.TriFunction;

public class FixedPlayerCharacterStatBuilder extends PlayerCharacterStatBuilder implements PlayerCharacterStats {

	private final FixedStatBuilder fixedStatBuilder;
	
	public FixedPlayerCharacterStatBuilder() {
		FixedStatBuilder fixedStatBuilder = new FixedStatBuilder();
		fixedStatBuilder.requiredStats.add(Stat.Owner);
		fixedStatBuilder.requiredStats.add(Stat.ID);
		this.fixedStatBuilder = fixedStatBuilder;
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		return fixedStatBuilder.getStat(layer, stat, type);
	}
	
	@Override
	public boolean hasStat(GameLayer layer, Stat stat) {
		return fixedStatBuilder.hasStat(layer, stat);
	}

	@Override
	public boolean hasStatAt(GameLayer layer, Stat stat) {
		return fixedStatBuilder.hasStat(layer, stat);
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		fixedStatBuilder.addStat(value, func);
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		fixedStatBuilder.setVariables(layer, expression);
	}

	public void validate() {
		fixedStatBuilder.validate();
	}

	@Override
	public FixedPlayerCharacterStats build() {
		return new FixedPlayerCharacterStats(fixedStatBuilder.build());
	}

}
