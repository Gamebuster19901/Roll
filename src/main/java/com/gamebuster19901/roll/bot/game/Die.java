package com.gamebuster19901.roll.bot.game;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

public class Die {

	protected final int sides;
	protected final DieType dieType;
	protected final String valueType;
	
	public Die(int sides) {
		this(sides, null);
	}
	
	public Die(int sides, String valueType) {
		if(!(this instanceof Value)) {
			if(sides == 0) {
				throw new IllegalArgumentException("Die cannot have 0 sides!");
			}
		}
		this.sides = sides;

		this.dieType = DieType.getDieType(Math.abs(sides));
		this.valueType = valueType;
	}
	
	public int getMaxValue() {
		return getMaxValue(null);
	}
	
	public int getMinValue() {
		return getMinValue(null);
	}
	
	public int getMaxValue(Statted statted) {
		if(sides > 0) {
			return sides;
		}
		else {
			return -1;
		}
	}
	
	public int getMinValue(Statted statted) {
		if(sides < 0) {
			return sides;
		}
		return 1;
	}
	
	public int roll() {
		return roll(null);
	}
	
	public int roll(Statted statted) {
		if(!isNegative()) {
			return ThreadLocalRandom.current().nextInt(1, sides + 1);
		}
		else {
			return ThreadLocalRandom.current().nextInt(sides, 0);
		}
	}
	
	public String toString() {
		return "d"+sides;
	}
	
	public String toFullString() {
		if(valueType != null) {
			return toString() + " " + valueType;
		}
		return toString();
	}
	
	public DieType getDieType() {
		return dieType;
	}
	
	@Nullable
	public String getValueType() {
		return valueType;
	}
	
	public int getSides() {
		return sides;
	}
	public boolean isNegative() {
		return sides < 0;
	}
	
	public Die invert() {
		return new Die(-getSides(), getValueType());
	}
	
}
