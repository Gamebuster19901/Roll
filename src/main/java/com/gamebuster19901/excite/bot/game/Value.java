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
		return ""+ getValue();
	}
	
	@Override
	public DieType getDieType() {
		return DieType.modifier;
	}
	
}
