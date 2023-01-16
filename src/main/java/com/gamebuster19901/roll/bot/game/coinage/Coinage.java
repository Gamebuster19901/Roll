package com.gamebuster19901.roll.bot.game.coinage;

public class Coinage {

	private final Coin type;
	private int amount;
	
	public Coinage(final Coin type) {
		this(type, 0);
	}
	
	public Coinage(final Coin type, final int amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public void add(int amount) {
		this.amount += amount;
	}
	
	public void subtract(int amount) {
		this.amount -= amount;
	}
	
	public Coin getType() {
		return type;
	}
	
	public int getAmount() {
		return amount;
	}

	public CoinPurse toPurse() {
		return CoinPurse.of(this);
	}
	
}
