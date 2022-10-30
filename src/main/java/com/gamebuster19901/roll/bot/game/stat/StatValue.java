package com.gamebuster19901.roll.bot.game.stat;

import org.jetbrains.annotations.NotNull;

import com.gamebuster19901.roll.bot.game.character.Stat;

public class StatValue<T> {
	
	private final Stat stat;
	private final StatSource source;
	private final T value;
	
	public StatValue(@NotNull Stat stat, @NotNull StatSource source, @NotNull T value) {
		this.stat = stat;
		this.source = source;
		this.value = value;
		
		//THROW NULLPOINTER IF ANY ARE NULL
		stat.toString();
		source.toString();
		value.toString();
	}
	
	public Stat getStat() {
		return stat;
	}
	
	public StatSource getStatSource() {
		return source;
	}
	
	public GameLayer getGameLayer() {
		return source.getGameLayer();
	}
	
	public T getValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValueAs(Class<T> type) {
		return (T) value;
	}
	
	public boolean isFromSource(StatSource source) {
		return value.equals(source);
	}
	
}
