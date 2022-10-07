package com.gamebuster19901.excite.bot.game;

import java.util.ArrayList;

public class Roll {
	
	private final Dice dice;
	private final ArrayList<Die> result = new ArrayList<Die>();
	
	public Roll(Dice dice) {
		this.dice = dice;
		roll();
	}
	
	public void roll() {
		if(result.isEmpty()) {
			if(dice.die > 0) {
				for(int i = 0; i < Math.abs(dice.amount); i++) {
					if(dice.amount > 0) {
						result.add(new Die(dice.die));
					}
					else {
						result.add(new Die(-dice.die));
					}
				}
			}
			else if(dice.die == 0) {
				result.add(new Value(dice.amount));
			}
			if(dice.hasChild()) {
				Roll childRoll = new Roll(dice.child);
				result.addAll(childRoll.result);
			}
		}
		else {
			throw new IllegalStateException("Dice already rolled!");
		}
	}

	public Dice getDice() {
		return dice;
	}
	
	public boolean isSortable() {
		return dice.getAllDice().size() > 1;
	}
	
}
