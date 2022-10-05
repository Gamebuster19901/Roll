package com.gamebuster19901.excite.bot.game;

public class Value extends Die {

	public Value(int value) {
		super(value);
	}
	
	@Override
	public int getValue() {
		return sides;
	}

	public String toString() {
		if(getValue() > -1) {
			return "+" + getValue();
		}
		return ""+ getValue();
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
