package com.gamebuster19901.roll.bot.game.coinage;

public enum Coin {

	COPPER(1),
	SILVER(10),
	ELECTRUM(50),
	GOLD(100),
	PLATINUM(1000);

	private final int multiplier;
	
	Coin(int multiplier) {
		this.multiplier = multiplier;
	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
}
