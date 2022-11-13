package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.gson.GSerializable;

public interface Statistic extends GSerializable {

	public Stat getStat();
	
}
