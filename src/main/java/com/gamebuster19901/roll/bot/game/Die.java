package com.gamebuster19901.roll.bot.game;

public class Die {

	protected final int sides;
	protected final DieType dieType;
	
	public Die(int sides) {
		if(sides == 0) {
			throw new IllegalArgumentException("Die cannot have 0 sides!");
		}
		this.sides = sides;

		dieType = DieType.getDieType(Math.abs(sides));
	}
	
	public int getMaxValue() {
		if(sides > 0) {
			return sides;
		}
		else {
			return -1;
		}
	}
	
	public int getMinValue() {
		if(sides < 0) {
			return sides;
		}
		return 1;
	}
	
	public String toString() {
		return "d"+sides;
	}
	
	public DieType getDieType() {
		return dieType;
	}
	
	public int getSides() {
		return sides;
	}
	
}
