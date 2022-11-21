package com.gamebuster19901.roll.bot.game.character;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.stat.FixedStats;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.TriFunction;

public class FixedPlayerCharacterStats extends PCStats implements PlayerCharacterStats {
	
	private FixedStats stats;
	
	public FixedPlayerCharacterStats(FixedStats stats) {
		this.stats = stats;
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		return stats.getStat(layer, stat, type);
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		stats.addStat(value, func);
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		stats.setVariables(layer, expression);
	}

	@Override
	public boolean hasStat(GameLayer layer, Stat stat) {
		return stats.hasStat(layer, stat);
	}

	@Override
	public boolean hasStatAt(GameLayer layer, Stat stat) {
		return stats.hasStatAt(layer, stat);
	}
	
}
