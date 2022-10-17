package com.gamebuster19901.roll.bot.command.interaction;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.DNDBeyondPDFArgument;
import com.mojang.brigadier.CommandDispatcher;

public class CharacterImportInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("characterImport")
			.then(Commands.argument("sheet", DNDBeyondSheetArgument.DND_BEYOND_SHEET_TYPE))
				.then(Commands.argument("pdf", DNDBeyondPDFArgument.DND_BEYOND_PDF_TYPE)
					.executes((context) -> {
						return 1;
					})	
				)
			
		);
	}
	
}
