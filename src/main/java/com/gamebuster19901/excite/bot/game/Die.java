package com.gamebuster19901.excite.bot.game;

import java.util.concurrent.ThreadLocalRandom;

import com.gamebuster19901.excite.bot.graphics.dice.animation.Rotation;
import com.gamebuster19901.excite.bot.graphics.dice.animation.Spin;

public class Die {

	protected final int sides;
	protected final int value;
	protected final Rotation rotation = Rotation.randRotation();
	protected final Spin spin = Spin.randSpin();
	protected final DieType dieType;
	
	public Die(int sides) {
		if(sides == 0) {
			throw new IllegalArgumentException("Die cannot have 0 sides!");
		}
		this.sides = sides;
		if(sides > 0) {
			this.value = ThreadLocalRandom.current().nextInt(1, sides + 1);
		}
		else {
			this.value = ThreadLocalRandom.current().nextInt(sides, 0);
		}
		dieType = DieType.getDieType(Math.abs(sides));
	}
	
	public int getValue() {
		return value;
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
		return "[" + value + "]d"+sides;
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
