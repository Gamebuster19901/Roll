package com.gamebuster19901.excite.bot.graphics;

import java.util.concurrent.ThreadLocalRandom;

public enum Rotation {
	North,
	NorthEast,
	East,
	SouthEast,
	South,
	SouthWest,
	West,
	NorthWest;
	
	public static Rotation getRotation(int i) {
		return values()[i % values().length];
	}
	
	public static Rotation randRotation() {
		return getRotation(ThreadLocalRandom.current().nextInt(values().length));
	}
	
}
