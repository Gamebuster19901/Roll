package com.gamebuster19901.roll.bot.game.stat;

public class StatValue<T> {
	
	private final StatSource source;
	private final T value;
	
	public StatValue(StatSource source, T value) {
		this.source = source;
		this.value = value;
	}
	
	public StatSource getStatSource() {
		return source;
	}
	
	public T getValue() {
		return value;
	}
	
	public boolean isFromSource(StatSource source) {
		return value.equals(source);
	}
	
}
