package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.character.Stat;

public class RollValue extends Value {

	private final boolean invert;
	private final String value;
	
	public RollValue(String value) {
		this(value, false);
	}
	
	public RollValue(String value, boolean invert) {
		super(0);
		this.value = value;
		this.invert = invert;
	}

	public String toString() {
		return value;
	}
	
	@Override
	public int getMaxValue(Statted statted) {
		if(statted != null && statted.hasStat(value)) {
			return statted.getStat(value, int.class);
		}
		return 0;
	}
	
	@Override
	public int getMinValue(Statted statted) {
		if(statted != null && statted.hasStat(value)) {
			return statted.getStat(value, int.class);
		}
		return 0;
	}
	
	public int roll(Statted statted) {
		if(statted == null || !statted.hasStat(value)) {
			if(statted != null) {
				System.out.println(statted.getName() + " does not have stat " + value);
			}
			else {
				System.out.println("No active character!");
			}
			return 0;
		}
			
		int stat = statted.getStat(value, int.class);
		if(isNegative()) {
			stat = -stat;
		}
		return stat;
	}
	
	public Stat getStat() {
		return Stat.fromUserInput(value);
	}
	
	@Override
	public DieType getDieType() {
		return DieType.stat;
	}
	
	public boolean isNegative() {
		return invert;
	}
	
	@Override
	public RollValue invert() {
		return new RollValue(value, !invert);
	}
	
}
