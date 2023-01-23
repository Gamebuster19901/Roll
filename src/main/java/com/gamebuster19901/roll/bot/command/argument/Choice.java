package com.gamebuster19901.roll.bot.command.argument;

@FunctionalInterface
public interface Choice<T> {

	public T[] getChoices(Class<T> type);
	
}
