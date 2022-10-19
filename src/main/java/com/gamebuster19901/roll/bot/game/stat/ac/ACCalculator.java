package com.gamebuster19901.roll.bot.game.stat.ac;

import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;

public interface ACCalculator extends Comparable<ACCalculator> {

	public static StatSource BASE_AC_STAT_SOURCE = new StatSource() {

		@Override
		public String toString() {
			return "Basic Rules";
		}
		
		@Override
		public String getStatSourceDescription() {
			return "AC = 10 + Dex\n\npg. 9, pg. 45";
		}

		@Override
		public GameLayer getGameLayer() {
			return GameLayer.GAME_RULE;
		}
		
	};
	
	public abstract int getBaseAC();
	
	public StatSource getStatSource();
	
	@Override
	public default int compareTo(ACCalculator other) {
		return ((Integer)getBaseAC()).compareTo(other.getBaseAC());
	}
	
	public default boolean isBetterThan(ACCalculator other) {
		return compareTo(other) > 0;
	}
	
}
