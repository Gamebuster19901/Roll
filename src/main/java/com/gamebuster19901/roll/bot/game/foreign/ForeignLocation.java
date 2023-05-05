package com.gamebuster19901.roll.bot.game.foreign;

public enum ForeignLocation {

	NOT_FOREIGN ((byte)0), //character was created with Roll, it is not from a foreign location
	DND_BEYOND  ((byte)1); //character was created with DND Beyond

	private byte id;
	
	ForeignLocation(byte id) {
		this.id = id;
	} 
	
	public byte getID() {
		return id;
	}
	
	public static ForeignLocation fromID(byte id) {
		if(id < 0 || id > values().length) {
			throw new IllegalArgumentException("" + id);
		}
		return values()[id];
	}
	
}
