package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.PlayerCharacterArgument;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.CommandDispatcher;

public class CharacterCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("character")
			.then(Commands.argument("character", new PlayerCharacterArgument())
				.executes((context) -> {
					PlayerCharacter playerCharacter = context.getArgument("character", PlayerCharacter.class);
					context.getSource().sendMessage("Your player is: " + playerCharacter.getName());
					return 1;
				})
			)
				
		);
	}
	
}
