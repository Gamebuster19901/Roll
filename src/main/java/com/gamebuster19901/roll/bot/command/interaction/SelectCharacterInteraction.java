package com.gamebuster19901.roll.bot.command.interaction;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.StattedArgumentType;
import com.gamebuster19901.roll.bot.command.embed.StatEmbedBuilder;
import com.gamebuster19901.roll.bot.command.embed.StatInteractionBuilder;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class SelectCharacterInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("selectcharacter")
			.then(Commands.argument("character", StattedArgumentType.CHARACTER)
				.executes((context) -> {
					PlayerCharacter character = context.getArgument("character", PlayerCharacter.class);
					ComponentInteraction e = (ComponentInteraction) context.getSource().getEvent(ComponentInteraction.class);
					MessageEditCallbackAction edit = e.deferEdit();
					ReplyCallbackAction reply = e.deferReply(true);
					if(character.getOwner().getIdLong() == context.getSource().getAuthor().getIdLong()) {
						edit.queue();
						MessageEditBuilder editBuilder = new MessageEditBuilder();
						editBuilder.setContent("You are now playing as " + character.getName() + ".").setEmbeds(new StatEmbedBuilder(character, editBuilder).getEmbed()).setComponents(new StatInteractionBuilder(character).getComponents());
						e.getHook().editOriginal(editBuilder.build()).queue();
					}
					else {
						reply.setContent("You do not own " + character.getName()).queue();
					}
					return 1;
				})
			)
		);
	}
	
}
