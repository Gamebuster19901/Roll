package com.gamebuster19901.roll.bot.command.interaction;

import java.util.Collections;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.StattedArgumentType;
import com.gamebuster19901.roll.bot.command.embed.StatEmbedBuilder;
import com.gamebuster19901.roll.bot.command.embed.StatInteractionBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class SelectCharacterInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("selectcharacter")
				.then(Commands.literal("null")
						.executes((context) -> {
							CommandContext c = context.getSource();
							ComponentInteraction e = (ComponentInteraction) c.getEvent(ComponentInteraction.class);
							e.deferEdit().queue();
							Users.setActiveCharacter(context.getSource().getAuthor(), null);
							MessageEditBuilder editBuilder = new MessageEditBuilder();
							editBuilder.setComponents(Collections.EMPTY_SET);
							editBuilder.setContent(c.getAuthor().getAsMention() + ", you no longer actively controlling a character.");
							editBuilder.setEmbeds(Collections.EMPTY_SET);
							e.getHook().editOriginal(editBuilder.build()).queue();
							return 1;
						})
					)
			.then(Commands.argument("character", StattedArgumentType.CHARACTER)
				.executes((context) -> {
					PlayerCharacter character = context.getArgument("character", PlayerCharacter.class);
					CommandContext c = context.getSource();
					ComponentInteraction e = (ComponentInteraction) c.getEvent(ComponentInteraction.class);
					MessageEditCallbackAction edit = e.deferEdit();
					ReplyCallbackAction reply = e.deferReply(true);
					if(character.getOwner().getIdLong() == c.getAuthor().getIdLong()) {
						edit.queue();
						MessageEditBuilder editBuilder = new MessageEditBuilder();
						try {
							Users.setActiveCharacter(context.getSource().getAuthor(), character);
							editBuilder.setContent(c.getAuthor().getAsMention() + ", you are now playing as " + character.getName() + ".").setEmbeds(new StatEmbedBuilder(character, editBuilder).getEmbed()).setComponents(new StatInteractionBuilder(character).getComponents());
							e.getHook().editOriginal(editBuilder.build()).queue();
						}
						catch (Throwable t) {
							editBuilder.setContent(c.getAuthor().getAsMention() + ", could not set your active character to " + character.getName() + " due to an error: " + t.getMessage()).setEmbeds(new StatEmbedBuilder(character, editBuilder).getEmbed()).setComponents(new StatInteractionBuilder(character).getComponents());
						}
					}
					else {
						reply.setContent(c.getAuthor().getAsMention() + ", you do not own " + character.getName()).queue();
					}
					return 1;
				})
			)
		);
	}
	
}
