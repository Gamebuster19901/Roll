package com.gamebuster19901.roll.bot.command.interaction;

import java.sql.SQLException;
import java.util.Collections;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.DNDBeyondPDFArgument;
import com.gamebuster19901.roll.bot.game.beyond.character.DNDBeyondPDFPlayerBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.util.ThreadService;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class CharacterImportInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("characterImport")
			.then(Commands.argument("pdf", DNDBeyondPDFArgument.DND_BEYOND_PDF_TYPE)
				.executes((context) -> {
					DNDBeyondPDFPlayerBuilder builder = context.getArgument("pdf", DNDBeyondPDFPlayerBuilder.class);
					long id = builder.getID();
					
					ThreadService.run("Character Importer(" + id + ")" , new CharacterOverwriteThread(){
						
						@Override
						public void run() {
							CommandContext<?> c = context.getSource();
							ModalInteractionEvent e = c.getEvent(ModalInteractionEvent.class);
							e.getHook();
							MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
							if(PlayerCharacter.exists(id)) {
								if(PlayerCharacter.getOwner(id).getIdLong() == context.getSource().getAuthor().getIdLong()) {
									messageBuilder.setContent("You already have a character with ID `" + id + "`, would you like to overwrite the previous character?");
									messageBuilder.setActionRow(Button.primary("rejectOverwrite " + id, "No"), Button.danger("acceptOverwrite " + id, "Yes"));
									
									MessageEditBuilder rejectBuilder = new MessageEditBuilder();
									String rejectMessage = "Refusing to overwrite character `" + id+ "`.";
									
									MessageCreateData askMessage = messageBuilder.build();
									InteractionHook askReply = e.reply(askMessage).setEphemeral(true).complete();
									boolean interrupted = false;
									synchronized(Thread.currentThread()) {
										try {
											Thread.currentThread().wait(10000);
										} catch (InterruptedException e1) {
											interrupted = true;
											rejectMessage = rejectMessage + " Not acknowledged before thread was interrupted. The bot was likely shut down before you could acknowledge.";
										}
									}
									if(!interrupted) {
										if(this.rejected) {
											rejectMessage = rejectMessage + " You rejected the character overwrite.";
										}
										else if(!this.accepted) {
											rejectMessage = rejectMessage + " You did not accept the character overwrite within 10 seconds";
										}
									}
									rejectBuilder.setContent(rejectMessage);
									rejectBuilder.setComponents(Collections.emptyList());
									System.out.println("editing message");
									askReply.editOriginal(rejectBuilder.build()).complete();
								}
								else {
									c.replyMessage("Refusing to overwrite character `" + id+ "`. You do not own that character.");
								}
							}
							else {
								try {
									addCharacterToDatabase(e, builder);
								} catch (SQLException e1) {
									throw new RuntimeException(e1);
								}
							}
						}
					});
					return 1;
				})	
			)
		);
	}
	
	public static void addCharacterToDatabase(GenericInteractionCreateEvent interaction, DNDBeyondPDFPlayerBuilder builder) throws SQLException {
		builder.build();
	}
	
}
