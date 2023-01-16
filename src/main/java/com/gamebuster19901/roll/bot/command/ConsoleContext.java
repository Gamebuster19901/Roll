package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.user.ConsoleUser;

public class ConsoleContext extends CommandContext<ConsoleUser>{

	public static final ConsoleContext INSTANCE = new ConsoleContext();
	
	private ConsoleContext() {
		super(ConsoleUser.INSTANCE);
	}
	
}