package com.gamebuster19901.roll.gson;

public interface GSerializable {
	
	public static final int DATA_VERSION = 3;

	public default String getClassName() {
		return getClass().getTypeName();
	}
	
	public default int getDataVersion() {
		return DATA_VERSION;
	}
	
}
