package com.gamebuster19901.roll.gson;

public interface GSerializable {

	public default String getClassName() {
		return getClass().getTypeName();
	}
	
}
