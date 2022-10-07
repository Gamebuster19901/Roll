package com.gamebuster19901.excite.bot.game;

public class Value extends Die {

	public Value(int value) {
		super(value);
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
	
	@Override
	public DieType getDieType() {
		return DieType.modifier;
	}
	
}
