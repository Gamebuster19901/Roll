package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

class CharacterCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("character").executes((context) -> {
			CommandContext<?> c = context.getSource();
			SlashCommandInteractionEvent e = c.getEvent(SlashCommandInteractionEvent.class);
			ReplyCallbackAction r = e.deferReply();
			r.queue();
			
			Result result = PlayerCharacter.getCharacterDataByOwner(c.getAuthor(), 23, 0);
			StringSelectMenu.Builder selectBuilder = StringSelectMenu.create("selectcharacter");
			while(result.next()) {
				String name = result.getString(Column.NAME);
				long id = result.getLong(Column.CHARACTER_ID);
				selectBuilder.addOption(name, id + "");
			}
			
			e.getHook().editOriginalComponents(ActionRow.of(selectBuilder.build())).queue();
			e.getHook().editOriginalEmbeds(new EmbedBuilder().setTitle("Select Character").build()).queue();
			
			return 1;
		}));
	}
	
}
