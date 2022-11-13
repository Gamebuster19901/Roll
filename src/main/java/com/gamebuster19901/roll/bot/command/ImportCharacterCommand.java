package com.gamebuster19901.roll.bot.command;

import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

class ImportCharacterCommand {

	@SuppressWarnings("unchecked")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("importcharacter")
				.executes((context) -> {
					CommandContext<?> c = context.getSource();
					TextInput pdfURL = TextInput.create("pdfURL", "PDF URL", TextInputStyle.SHORT)
						.setPlaceholder("The URL of your character's DNDBeyond PDF file")
						.build();
					
					Modal modal = Modal.create("characterImport", "Character Import")
						.addActionRows(ActionRow.of(pdfURL)).build();
					
					c.getEvent(SlashCommandInteractionEvent.class).replyModal(modal).complete();
					//PlayerCharacter playerCharacter = context.getArgument("character", PlayerCharacter.class);
					//context.getSource().sendMessage("Your character is: " + playerCharacter.getName());
					return 1;
				})
			);
				
	}
	
}
