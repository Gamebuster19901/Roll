package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;

public enum MovementType implements Statistic {
	Walking,
	Flying,
	Burrowing,
	Climbing,
	Swimming;
	
	public static final StatSource MOVEMENT_RULES = new StatSource() {

		@Override
		public GameLayer getGameLayer() {
			return GameLayer.GAME_RULE;
		}

		@Override
		public String getStatSourceDescription() {
			return "Basic Rules: pg. 66-67, pg 73-74, pg. 111";
		}};
	
	public Stat getStat() {
		return new MovementStat(this.name() + " speed");
	}
	
	public static MovementType fromStat(Stat stat) {
		if(stat.isSpeedStat()) {
			for(MovementType type : values()) {
				if (stat.getName().startsWith(type.name())) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("'" + stat.getName() + "' is not a known movement stat");
	}
	
	public static final class MovementStat extends Stat {
		private MovementStat(String name) {
			super(name);
		}
	}
}
