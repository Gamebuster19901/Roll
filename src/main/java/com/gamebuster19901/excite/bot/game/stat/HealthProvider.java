package com.gamebuster19901.excite.bot.game.stat;

import com.gamebuster19901.excite.bot.game.Roll;

public abstract class HealthProvider {

	public abstract int getMaxHP(Roll roll);
	
	public int getMaxPossibleHP(Roll roll) {
		return roll.getDice().getMaxValue();
	}
	
}
