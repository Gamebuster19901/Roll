package com.gamebuster19901.roll.bot.game.stat.ac;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.StatSource;

public class StandardACCalculator implements ACCalculator {

	private Statted entity;
	
	public StandardACCalculator(Statted entity) {
		this.entity = entity;
	}
	
	@Override
	public int getBaseAC() {
		return 10 + entity.getModifier(Ability.Dexterity);
	}

	@Override
	public StatSource getStatSource() {
		return ACCalculator.BASE_AC_STAT_SOURCE;
	}

}
