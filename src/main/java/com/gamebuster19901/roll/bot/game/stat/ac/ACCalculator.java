package com.gamebuster19901.roll.bot.game.stat.ac;

public interface ACCalculator extends Comparable<ACCalculator>{

	public abstract int getAC();
	
	@Override
	public default int compareTo(ACCalculator other) {
		return ((Integer)getAC()).compareTo(other.getAC());
	}
	
	public default boolean isBetterThan(ACCalculator other) {
		return compareTo(other) > 0;
	}
	
}
