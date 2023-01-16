package com.gamebuster19901.roll.bot.game.item.weapon;

public class RangedProperty extends WeaponProperty {

	private final int normalRange;
	private final int maxRange;
	
	public RangedProperty(int normalRange, int maxRange) {
		super("Ranged");
		this.normalRange = normalRange;
		this.maxRange = maxRange;
	}
	
	public int getNormalRage() {
		return normalRange;
	}
	
	public int getMaxRange() {
		return maxRange;
	}

	public String toString() {
		return name + " (" + normalRange + "/" + maxRange + ")";
	}
	
}
