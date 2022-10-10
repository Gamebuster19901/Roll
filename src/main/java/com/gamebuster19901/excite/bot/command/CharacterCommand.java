package com.gamebuster19901.excite.bot.command;

import com.gamebuster19901.excite.bot.command.argument.PlayerCharacterArgument;
import com.mojang.brigadier.CommandDispatcher;

public class CharacterCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("character")
			.then(Commands.argument("character", new PlayerCharacterArgument())
				.executes((context) -> {
					return 1;
				})
			)
				
		);
	}
	
}
