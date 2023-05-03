package com.gamebuster19901.roll.bot.command.argument;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class AbilityArgumentType implements SuggestableArgument<Ability> {

	public static final AbilityArgumentType ANY_ABILITY = AbilityArgumentType.of(Ability.values());
	
	private Ability[] abilities;
	
	private AbilityArgumentType(Ability[] abilities) {
		this.abilities = abilities;
	}

	@Override
	public <S> Ability parse(S context, StringReader reader) throws CommandSyntaxException {
		String lookingFor = Commands.readString(reader);
		for(Ability ability : abilities) {
			if(ability.name().equalsIgnoreCase(lookingFor) || ability.shortHand.equalsIgnoreCase(lookingFor)) {
				return ability;
			}
		}
		throw ParseExceptions.INVALID_ABILITY.create(lookingFor);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String input = Commands.lastArgOf(builder.getInput());
		String currentArg = getCurrentArg(input);
		StringRange currentRange = getCurrentArgRange(input);
		for(Ability ability : abilities) {
			if(ability.name().toLowerCase().startsWith(currentArg.toLowerCase())) {
				builder.suggest(new Suggestion(currentRange, ability.name()));
			}
		}
		return builder.buildFuture();
	}

	private static AbilityArgumentType of(Ability[] abilities) {
		if(abilities.length > 0) {
			return new AbilityArgumentType(abilities);
		}
		throw new IllegalArgumentException("Argument must accept at least one ability!");
	}

}
