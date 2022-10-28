package com.gamebuster19901.roll.bot.game.stat;

public interface StatBuilder<S extends Stats> {

	public void validate();
	
	public S build();
	
}
