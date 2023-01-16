package com.gamebuster19901.roll.bot.game.item.weapon;

public class WeaponProperty {
	
	public static final WeaponProperty LIGHT = new WeaponProperty("Light");
	public static final WeaponProperty TWO_HANDED = new WeaponProperty("Two Handed");
	public static final WeaponProperty HEAVY = new WeaponProperty("Heavy");
	public static final WeaponProperty SPECIAL = new WeaponProperty("Special");
	public static final WeaponProperty THROWN = new WeaponProperty("Thrown");
	public static final WeaponProperty FINESSE = new WeaponProperty("Finesse");
	public static final WeaponProperty VERSITILE = new WeaponProperty("Versitile");
	public static final WeaponProperty AMMUNITION = new WeaponProperty("Ammunition");
	public static final WeaponProperty LOADING = new WeaponProperty("Loading");
	
	public String name;
	
	public WeaponProperty(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
	
	public boolean equals(WeaponProperty property) {
		return this.name.equals(property.name);
	}
}
