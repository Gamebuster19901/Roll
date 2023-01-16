package com.gamebuster19901.roll.bot.game.item;

import com.gamebuster19901.roll.bot.game.coinage.Coinage;

public interface Item {

	public String getName();
	
	public Coinage getCost();
	
	public double getWeight();
	
	public String getDescription();
	
	public Rarity getRarity();
	
	public boolean isMagical();
	
	boolean requiresAttunement();
	
}
