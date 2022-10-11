package com.gamebuster19901.roll.util;

public interface Identified {

	public long getID();
	
	public default boolean isKnown() {
		return true;
	}
	
}
