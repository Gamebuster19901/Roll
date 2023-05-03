package com.gamebuster19901.roll.bot.command.argument;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.Commands;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class LiteralArgument implements SuggestableArgument<String> {

	private final String arg;
	private SuggestableArgument<String> suggestionImpl = new DefaultLiteralSuggestionImpl(this);
	
	private LiteralArgument(String arg) {
		this.arg = arg;
	}
	
	private LiteralArgument(String arg, SuggestableArgument<String> suggestionImplementation) {
		this.arg = arg;
		this.suggestionImpl = suggestionImplementation;
	}
	
	public static LiteralArgument of(String name) {
		return new LiteralArgument(name);
	}
	
	public static LiteralArgument of(String name, SuggestableArgument<String> suggestionImplementation) {
		return new LiteralArgument(name, suggestionImplementation);
	}
	
	@Override
	public <S> String parse(S context, StringReader reader) throws CommandSyntaxException {
		return Commands.readString(reader);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return suggestionImpl.listSuggestions(context, builder);
	}

	private static final class DefaultLiteralSuggestionImpl implements SuggestableArgument<String> {

		private final LiteralArgument arg;
		
		private DefaultLiteralSuggestionImpl(LiteralArgument arg) {
			this.arg = arg;
		}
		
		@Override
		public <S> String parse(S context, StringReader reader) throws CommandSyntaxException {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			String input = Commands.lastArgOf(builder.getInput());
			String currentArg = getCurrentArg(input);
			StringRange currentRange = getCurrentArgRange(input);
			
			if(canSuggest(input)) {
				if(arg.arg.toLowerCase().startsWith(currentArg.toLowerCase())) {
					builder.suggest(new Suggestion(currentRange, arg.arg));
				}
			}
				
			return builder.buildFuture();
		}
		
	}
}
