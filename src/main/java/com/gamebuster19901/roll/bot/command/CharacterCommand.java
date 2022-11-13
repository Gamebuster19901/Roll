package com.gamebuster19901.roll.bot.command;

import java.util.HashSet;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Pagination;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

class CharacterCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("character").executes((context) -> {
			CommandContext<?> c = context.getSource();
			SlashCommandInteractionEvent e = c.getEvent(SlashCommandInteractionEvent.class);
			
			HashSet<PlayerCharacter> characters = new HashSet<PlayerCharacter>();
			Result result = Table.selectColumnsFromWhere(Column.CHARACTER_ID, Table.CHARACTERS, new Comparison(Column.DISCORD_ID, Comparator.EQUALS, e.getUser().getIdLong()), new Pagination(Column.CHARACTER_ID, 23, 0));
			StringSelectMenu.Builder selectBuilder = StringSelectMenu.create("Select Character");
			while(result.next()) {
				PlayerCharacter character = PlayerCharacter.deserialize(result.getLong(Column.CHARACTER_ID));
				characters.add(character);
				selectBuilder.addOption(character.getName(), character.getID() + "");
			}

			
			return 1;
		}));
	}
	
}
