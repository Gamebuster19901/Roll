package com.gamebuster19901.roll.bot.command.argument;

import com.gamebuster19901.roll.bot.game.SaveType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import static com.gamebuster19901.roll.bot.game.SaveType.*;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;

public class SaveArgumentType implements ArgumentType<SaveType> {

	public static final SaveArgumentType ANY_SAVE = SaveArgumentType.of(SaveType.values());
	public static final SaveArgumentType ABILITY_SAVE = SaveArgumentType.of(STRENGTH_SAVE, DEXTERITY_SAVE, CONSTITUTION_SAVE, INTELLIGENCE_SAVE, WISDOM_SAVE, CHARISMA_SAVE);;
	public static final SaveArgumentType DEATH_SAVE = SaveArgumentType.of(SaveType.DEATH_SAVE);
	
	private SaveType[] saves;
	
	private SaveArgumentType(SaveType[] saves) {
		this.saves = saves;
	}
	
	@Override
	public <S> SaveType parse(S context, StringReader reader) throws CommandSyntaxException {
		String lookingFor = Commands.readString(reader);
		for(SaveType save : saves) {
			if(save.getName().equalsIgnoreCase(lookingFor)) {
				return save;
			}
		}
		throw ParseExceptions.INVALID_THROW.create(lookingFor);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(SaveType save : saves) {
			if(save.getName().toLowerCase().startsWith(Commands.lastArgOf(builder.getInput()).toLowerCase())) {
				builder.suggest(save.getName());
			}
		}
		return builder.buildFuture();
	}

	private static SaveArgumentType of(SaveType... saves) {
		if(saves.length > 0) {
			return new SaveArgumentType(saves);
		}
		throw new IllegalArgumentException("Argument must accept at least one save type!");
	}
	
}
