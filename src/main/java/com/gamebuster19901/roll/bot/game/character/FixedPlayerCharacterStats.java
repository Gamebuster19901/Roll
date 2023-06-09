package com.gamebuster19901.roll.bot.game.character;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.coinage.Coinage;
import com.gamebuster19901.roll.bot.game.foreign.ForeignLocation;
import com.gamebuster19901.roll.bot.game.stat.FixedStats;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import static com.gamebuster19901.roll.bot.game.stat.GameLayer.*;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.TriFunction;
import com.gamebuster19901.roll.util.URI;
import com.google.common.collect.ImmutableMap;

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

	@Override
	public int getCopper() {
		return getStat(Stat.Copper, int.class);
	}

	@Override
	public int getSilver() {
		return getStat(Stat.Silver, int.class);
	}

	@Override
	public int getElectrum() {
		return getStat(Stat.Electrum, int.class);
	}

	@Override
	public int getGold() {
		return getStat(Stat.Gold, int.class);
	}

	@Override
	public int getPlatinum() {
		return getStat(Stat.Platinum, int.class);
	}
	
	@Override
	public ForeignLocation getForeignLocation() {
		return getStat(DATABASE, "foreignLocation", ForeignLocation.class);
	}

	@Override
	public URI getForeignURI() {
		return getStat(DATABASE, "foreignURI", URI.class);
	}

	@Override
	public void addCoinage(Coinage... coinages) {
		//fixed stats cannot be altered
	}

	@Override
	public void removeCoinage(Coinage... coinages) {
		//fixed stats cannot be altered
	}

	@Override
	public void setCoinage(Coinage... coinages) {
		//fixed stats cannot be altered
	}

	@Override
	public ImmutableMap<Stat, StatValue<?>> getStats(GameLayer layer) {
		return stats.getStats(layer);
	}
	
}
