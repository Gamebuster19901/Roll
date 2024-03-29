package com.gamebuster19901.roll.bot.game;

public enum DieType {
	other(0),
	modifier(1, 0, 0, 220),
	stat(1, 0, 0, 220),
	//d2(2),
	d4(4, 0, 15, 120),
	d6(6, 0, 0, 120),
	d8(8, 0, 0, 120),
	d10(10, 0, 0, 120),
	d12(12, 0, 0, 120),
	d20(20, 0, 0, 120),
	d100(100);
	
	int sides;
	int maxTextSize;
	int offsetX;
	int offsetY;
	
	DieType(int sides) {
		this(sides, 0, 0, 256);
	}
	
	DieType(int sides, int textOffsetX, int textOffsetY, int maxTextSize) {
		this.sides = sides;
		this.maxTextSize = maxTextSize;
		this.offsetX = textOffsetX;
		this.offsetY = textOffsetY;
	}
	
	public int getTextSize() {
		return maxTextSize;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public int getSides() {
		return sides;
	}
	
	public static DieType getDieType(int i) {
		for(DieType type : values()) {
			if(i == type.sides) {
				return type;
			}
		}
		return other;
	}

	public static DieType[] getStandardDiceTypes() {
		return new DieType[] {d4, d6, d8, d10, d12, d20, d100};
	}
	
}
