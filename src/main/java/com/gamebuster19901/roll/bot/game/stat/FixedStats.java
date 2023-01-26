package com.gamebuster19901.roll.bot.game.stat;

import java.util.HashMap;
import java.util.Map;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.util.TriFunction;
import com.google.common.collect.ImmutableMap;

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
	
	final HashMap<Stat, StatValue<?>> values;

	public FixedStats(HashMap<Stat, StatValue<?>> stats) {
		values = stats;
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		try {
			return values.get(stat).getValueAs(type);
		}
		catch(Throwable t) {
			throw new RuntimeException("Issue retrieveing " + stat + " stat", t);
		}
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		//NO-OP
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		//NO-OP
	}

	@Override
	public boolean hasStat(GameLayer layer, Stat stat) {
		return values.containsKey(stat);
	}

	@Override
	public boolean hasStatAt(GameLayer layer, Stat stat) { //fixedStats doesn't implement layer system
		return hasStat(stat);
	}

	@Override
	public Map<Stat, StatValue<?>> getStats() {
		return ImmutableMap.copyOf(values);
	}

}
