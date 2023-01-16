package com.gamebuster19901.roll.bot.game.item.weapon;

import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.item.Item;

public interface Weapon extends Item {
	
	public Dice getDamage();
	
	public WeaponProperty getProperties();
	
}
