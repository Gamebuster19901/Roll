package com.gamebuster19901.excite.bot.game;

import com.gamebuster19901.excite.bot.graphics.dice.animation.Rotation;
import com.gamebuster19901.excite.bot.graphics.dice.animation.Spin;

public class Die {

	protected final int sides;
	protected final Rotation rotation = Rotation.randRotation();
	protected final Spin spin = Spin.randSpin();
	protected final DieType dieType;
	
	public Die(int sides) {
		if(sides == 0) {
			throw new IllegalArgumentException("Die cannot have 0 sides!");
		}
		this.sides = sides;

		dieType = DieType.getDieType(Math.abs(sides));
	}
	
	public int getMaxValue() {
		if(sides > 0) {
			return sides;
		}
		else {
			return -1;
		}
	}
	
	public int getMinValue() {
		if(sides < 0) {
			return sides;
		}
		return 1;
	}
	
	public String toString() {
		return "d"+sides;
	}
	
	public DieType getDieType() {
		return dieType;
	}
	
	public Spin getSpin() {
		return spin;
	}
	
	public int getSides() {
		return sides;
	}
	
}
