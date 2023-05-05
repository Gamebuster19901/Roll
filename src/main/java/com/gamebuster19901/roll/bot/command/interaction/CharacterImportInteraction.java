package com.gamebuster19901.roll.bot.command.interaction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.DNDBeyondPDFArgument;
import com.gamebuster19901.roll.bot.command.embed.StatEmbedBuilder;
import com.gamebuster19901.roll.bot.command.embed.StatInteractionBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.foreign.beyond.character.DNDBeyondPDFPlayerBuilder;
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
					
					ThreadService.run(new ConfirmationThread(id){
						
						@Override
						public void run() {
							CommandContext<?> c = context.getSource();
							ModalInteractionEvent e = c.getEvent(ModalInteractionEvent.class);
							e.getHook();
							MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
							if(PlayerCharacter.exists(id)) {
								if(PlayerCharacter.getOwner(id).getIdLong() == context.getSource().getAuthor().getIdLong()) {
									String name = PlayerCharacter.getName(id);
									messageBuilder.setContent("You already have a character with ID `" + id + "`, would you like to overwrite `" + name + "`?");
									messageBuilder.setActionRow(Button.primary("overwrite " + id + " reject", "No"), Button.danger("overwrite " + id + " confirm", "Yes"));
									
									MessageEditBuilder responseBuilder = new MessageEditBuilder();
									String rejectMessage = "Refusing to overwrite `" + name + "`.";
									
									MessageCreateData askMessage = messageBuilder.build();
									InteractionHook askReply = e.reply(askMessage).setEphemeral(false).complete();
									boolean interrupted = false;
									boolean failed = false;
									synchronized(Thread.currentThread()) {
										try {
											Thread.currentThread().wait(10000);
										} catch (InterruptedException e1) {
											interrupted = true;
											rejectMessage = rejectMessage + " Not acknowledged before thread was interrupted. The bot was likely shut down before you could acknowledge.";
											failed = true;
										}
									}
									if(!interrupted) {
										if(this.rejected) {
											rejectMessage = rejectMessage + " You rejected the character overwrite.";
											failed = true;
										}
										else if(!this.accepted) {
											rejectMessage = rejectMessage + " You did not accept the character overwrite within 10 seconds";
											failed = true;
										}
									}
									if(failed) {
										responseBuilder.setContent(rejectMessage);
										responseBuilder.setComponents(Collections.emptyList());
										System.out.println("editing message");
										askReply.editOriginal(responseBuilder.build()).complete();
									}
									else {
										try {
											PlayerCharacter.deleteCharacter(id);
											PlayerCharacter character = addCharacterToDatabase(e, builder);
											askReply.editOriginal(responseBuilder.setContent("Overwrote " + character.getName()).setComponents(new StatInteractionBuilder(character).getComponents()).setEmbeds(new StatEmbedBuilder(character, responseBuilder).getEmbed()).build()).complete();
										} catch (SQLException | IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}
								else {
									c.replyMessage("Refusing to overwrite character `" + id+ "`. You do not own that character.");
								}
							}
							else {
								try {
									PlayerCharacter character = addCharacterToDatabase(e, builder);
									MessageCreateBuilder builder = new MessageCreateBuilder();
									e.reply(builder.setContent("Added `" + character.getName() + "` to your collection.").setEmbeds(new StatEmbedBuilder(character, builder).getEmbed()).build()).queue();
								} catch (SQLException | IOException e1) {
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
	
	private static PlayerCharacter addCharacterToDatabase(GenericInteractionCreateEvent interaction, DNDBeyondPDFPlayerBuilder builder) throws SQLException, IOException {
		return PlayerCharacter.addCharacterToDatabase(builder.build(), true);
	}
	
}
