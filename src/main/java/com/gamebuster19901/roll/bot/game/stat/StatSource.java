package com.gamebuster19901.roll.bot.game.stat;

public interface StatSource {
	
	public static final StatSource DATABASE_SOURCE = StatSource.of(GameLayer.DATABASE, " For internal database use. You shouldn't see this!");
	
	public GameLayer getGameLayer();
	
	public String toString();
	
	public String getStatSourceDescription();
	
	public static StatSource of(GameLayer layer, String desc) {
		return new StatSource() {

			@Override
			public GameLayer getGameLayer() {
				return layer;
			}

			@Override
			public String getStatSourceDescription() {
				return desc;
			}
			
			@Override
			public String toString() {
				return getStatSourceDescription();
			}
			
		};
	}
	
}
