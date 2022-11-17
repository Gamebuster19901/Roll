package com.gamebuster19901.roll.bot.command.interaction;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.StatPageArgumentType;
import com.gamebuster19901.roll.bot.command.argument.StattedArgumentType;
import com.gamebuster19901.roll.bot.command.embed.StatEmbedBuilder;
import com.gamebuster19901.roll.bot.command.embed.StatEmbedPage;
import com.gamebuster19901.roll.bot.command.embed.StatInteractionBuilder;
import com.gamebuster19901.roll.bot.game.Statted;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class CharacterSheetInteraction {

	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("charsheet")
			.then(Commands.literal("page")
				.then(Commands.argument("statted", StattedArgumentType.CHARACTER_NULLABLE)
					.then(Commands.argument("page", StatPageArgumentType.ARGUMENT)
						.executes((context) -> {
							CommandContext<?> c = context.getSource();
							ComponentInteraction e = c.getEvent(ComponentInteraction.class);
							Message oldMessage = e.getMessage();
							MessageEditBuilder editBuilder = new MessageEditBuilder();
							Statted statted = context.getArgument("statted", Statted.class);
							MessageEditCallbackAction editAction = e.deferEdit();
							switchPage(e, editAction, editBuilder, statted, oldMessage, context.getArgument("page", StatEmbedPage.class));
							editAction.queue();
							return 1;
						})
					)
				)
			)
			.then(Commands.literal("menu")
					.then(Commands.argument("state", StringArgumentType.word())
						.then(Commands.argument("statted", StattedArgumentType.CHARACTER_NULLABLE)
							.then(Commands.argument("page", StatPageArgumentType.ARGUMENT)
								.executes((context) -> {
									CommandContext<?> c = context.getSource();
									ComponentInteraction e = c.getEvent(ComponentInteraction.class);
									MessageEditBuilder editBuilder = new MessageEditBuilder();
									Statted statted = context.getArgument("statted", Statted.class);
									MessageEditCallbackAction editAction = e.deferEdit();
									showDropdown(e, editAction, editBuilder, statted, context.getArgument("state", String.class).equals("show"));
									editAction.queue();
									return 1;
								})
							)
						)
					)
				)
		);
	}
	
	
	private static MessageEditCallbackAction switchPage(ComponentInteraction i, MessageEditCallbackAction e, MessageEditBuilder b, Statted statted, Message message, StatEmbedPage page) {
		return e.setEmbeds(new StatEmbedBuilder(statted, page, b).getEmbed()).setComponents(new StatInteractionBuilder(statted, message, page).getComponents());
	}
	
	public static MessageEditCallbackAction showDropdown(ComponentInteraction i, MessageEditCallbackAction e, MessageEditBuilder b, Statted statted, boolean showMenu) {
		return e.setComponents(new StatInteractionBuilder(statted, i.getMessage(), showMenu).getComponents());
	}
	
	
}
