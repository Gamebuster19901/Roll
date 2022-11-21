package com.gamebuster19901.roll.bot.game;

public class Value extends Die {

	public Value(int value) {
		super(value);
	}
	
	public Value(int value, DamageType type) {
		super(value, type);
	}

	public String toString() {
		if(sides > -1) {
			return "+" + sides;
		}
		return ""+ sides;
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
