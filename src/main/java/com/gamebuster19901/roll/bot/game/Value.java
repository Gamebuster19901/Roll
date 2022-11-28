package com.gamebuster19901.roll.bot.game;

public class Value extends Die {

	public Value(int value) {
		this(value, null);
	}
	
	public Value(int value, String type) {
		super(value, type);
	}

	public String toString() {
		StringBuilder ret = new StringBuilder();
		if(sides > -1) {
			ret.append("+");
		}
		ret.append(sides);
		if(getValueType() != null) {
			ret.append(getValueType());
		}
		return ret.toString();
	}
	
	public int getMaxValue() {
		return sides;
	}
	
	public int getMinValue() {
		return sides;
	}
	
	public int roll(Statted statted) {
		return sides;
	}
	
	@Override
	public DieType getDieType() {
		return DieType.modifier;
	}
	
	@Override
	public Value invert() {
		return new Value(-getSides());
	}
	
}
