package com.gamebuster19901.roll.bot.game.stat;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.Stat;

public abstract class Stats implements Statted {
	
	public Stats(String name) {
		addStat(new StatValue<String>(Stat.Name, StatSource.of(GameLayer.CHOSEN, name), name));
	}
	
	public String getName() {
		return getStat(GameLayer.EFFECT, Stat.Name, String.class);
	}

}
