package com.gamebuster19901.excite.bot.game.character;

public class Stat<T> {

	T stat;
	int value;
	
	public Stat(T stat, int value) {
		this.stat = stat;
		this.value = value;
	}
	
	public int get() {
		return value;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Stat) {
			return this.equals(((Stat)o).stat);
		}
		return false;
	}
	
}
