package com.gamebuster19901.roll.bot.game.stat;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.gson.Metamorphic;

public abstract @Metamorphic class Stats implements Statted {
	
	public String getName() {
		return getStat(GameLayer.EFFECT, Stat.Name, String.class);
	}

}
