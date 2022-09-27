package com.gamebuster19901.excite.bot.game;

import java.util.concurrent.ThreadLocalRandom;

import com.gamebuster19901.excite.bot.graphics.Rotation;
import com.gamebuster19901.excite.bot.graphics.Spin;

public class Die {

	protected final int sides;
	protected final int value;
	protected final Rotation rotation = Rotation.randRotation();
	protected final Spin spin = Spin.randSpin();
	
	public Die(int sides) {
		if(sides < 1) {
			throw new IllegalArgumentException("Die must have 1 or more sides!");
		}
		this.sides = sides;
		this.value = ThreadLocalRandom.current().nextInt(1, sides + 1);
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return "[" + value + "]d"+sides;
	}
	
}
