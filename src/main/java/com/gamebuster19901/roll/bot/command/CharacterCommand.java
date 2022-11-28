package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

class CharacterCommand {

	private static final MessageEmbed TITLE_EMBED = new EmbedBuilder().setTitle("Select Character").build();
	
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("character").executes((context) -> {
			CommandContext<?> c = context.getSource();
			SlashCommandInteractionEvent e = c.getEvent(SlashCommandInteractionEvent.class);
			ReplyCallbackAction r = e.deferReply();
			r.queue();
			
			Result result = Users.getCharacterDataByOwner(c.getAuthor(), 23, 0);
			StringSelectMenu.Builder selectBuilder = StringSelectMenu.create("selectcharacter");
			while(result.next()) {
				String name = result.getString(Column.NAME);
				long id = result.getLong(Column.CHARACTER_ID);
				selectBuilder.addOption(name, id + "");
			}
			selectBuilder.addOption("No Character", "null");
			
			MessageEditBuilder editBuilder = new MessageEditBuilder();
			editBuilder.setComponents(ActionRow.of(selectBuilder.build())).setEmbeds(TITLE_EMBED);
			e.getHook().editOriginal(editBuilder.build()).queue();
			
			return 1;
		}));
	}
	
}
