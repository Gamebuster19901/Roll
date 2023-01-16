package com.gamebuster19901.roll.bot.game.item;

public class ItemCategory {

	public String name;
	
	public ItemCategory(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
	
	public boolean equals(ItemCategory category) {
		return this.name.equals(category.name);
	}
	
}
