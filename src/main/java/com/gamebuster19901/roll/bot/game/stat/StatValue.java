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
		if(type.isEnum() && value instanceof String) {
			return (T) Enum.valueOf((Class)type, (String)value);
		}
		if(Number.class.isAssignableFrom(type) || type.isPrimitive()) {
			if(type == int.class || type == Integer.class) {
				return (T)(Integer)((Number)value).intValue();
			}
			if(type == long.class || type == Long.class) {
				return (T)(Long)((Number)value).longValue();
			}
			if(type == float.class || type == Float.class) {
				return (T)(Float)((Number)value).floatValue();
			}
			if(type == double.class || type == Double.class) {
				return (T)(Double)((Number)value).doubleValue();
			}
			if(type == short.class || type == Short.class) {
				return (T)(Short)((Number)value).shortValue();
			}
			if(type == byte.class || type == Byte.class) {
				return (T)(Float)((Number)value).floatValue();
			}
		}
		return (T) value;
	}
	
	public boolean isFromSource(StatSource source) {
		return value.equals(source);
	}
	
}
