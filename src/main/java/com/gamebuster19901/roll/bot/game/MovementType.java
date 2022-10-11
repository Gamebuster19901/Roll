package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.character.Stat;

public enum MovementType implements Statistic {
	Walking,
	Flying,
	Burrowing,
	Climbing,
	Swimming;
	
	public Stat getStat() {
		return new Stat(this.name() + " speed");
	}
}
