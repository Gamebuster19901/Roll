package com.gamebuster19901.roll.gson.updaters;

public interface Updaters {

	public int version();
	public GUpdater[] getUpdaters();
	
	public default void doRangedConversions(int version) {
		
	}
	
}
