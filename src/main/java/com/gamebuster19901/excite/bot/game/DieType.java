package com.gamebuster19901.excite.bot.game;

public enum DieType {
	other(0),
	modifier(1),
	//d2(2),
	d4(4),
	d6(6),
	d8(8),
	d10(10),
	d12(12),
	d20(20),
	d100(100);
	
	int sides;
	
	DieType(int sides) {
		this.sides = sides;
	}
	
	public static DieType getDieType(int i) {
		for(DieType type : values()) {
			if(i == type.sides) {
				return type;
			}
		}
		return other;
	}

}
