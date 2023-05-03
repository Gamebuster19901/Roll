package com.gamebuster19901.roll.bot.command.argument;

import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.DieType;
import com.gamebuster19901.roll.bot.game.Statistic;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mysql.cj.util.StringUtils;

public class DiceArgumentType implements ArgumentType<Dice> {
	
	public static final Pattern DELIMITER_REGEX = Pattern.compile("(.*?)([+\\-])");
	public static final Pattern DICE_REGEX = Pattern.compile("^(?<amount>\\d*)d(?<die>\\d*)$");
	
	private LinkedHashSet<String> suggestions = new LinkedHashSet<String>();
	
	public DiceArgumentType() {}
	
	public DiceArgumentType suggestStats(Statted statted) {
		return suggestStats(statted.getStats().keySet().toArray(new Stat[]{}));
	}
	
	public DiceArgumentType suggestStats(Statistic... statistics) {
		for(Statistic statistic : statistics) {
			suggestions.add(statistic.getStat().getSuggestion());
		}
		return this;
	}
	
	public DiceArgumentType suggest(String... text) {
		for(String s : text) {
			suggestions.add(s);
		}
		return this;
	}
	
	@Override
	public <S> Dice parse(S context, StringReader reader) throws CommandSyntaxException {
		Dice dice = new Dice(reader);
		if(dice.getAllDice().size() <= 0) {
			throw ParseExceptions.NO_DICE_TO_ROLL.create(dice);
		}
		else if (dice.getAllDice().size() > 1000) {
			throw ParseExceptions.TOO_MANY_DICE.create();
		}
		return dice;
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String input = Commands.lastArgOf(builder.getInput());
		String lowercaseInput = Commands.lastArgOf(builder.getInput().toLowerCase());
		
		if(input.isBlank()) {
			builder.suggest(new Suggestion(new StringRange(0, 0), "<Dice>"));
		}
		
		String lastValidArg = getLastValidArg(input);
		StringRange lastValidRange = getLastArgRange(input);
		String currentArg = getCurrentArg(input);
		StringRange currentRange = getCurrentArgRange(input);
		boolean isDelimiter = isDelimiter(currentArg);
		
		int lastArgEnd = getLastArgRange(input).getEnd();
		StringRange replaceRange = 
				isDelimiter 
					? new StringRange(input.length(), input.length())
					: currentRange;
		
		if(isDelimiter || input.isBlank()) {
			builder.suggest(new Suggestion(replaceRange, input + "<Dice>"));
			for(String suggestion : suggestions) {
				builder.suggest(new Suggestion(replaceRange, input + suggestion));
			}
			return builder.buildFuture();
		}
		
		for(String suggestion : suggestions) {
			if(suggestion.toLowerCase().startsWith(currentArg.toLowerCase())) {
				if(suggestion.equalsIgnoreCase(currentArg)) {
					StringRange range = replaceRange;
					replaceRange = new StringRange(input.length(), input.length());
					builder.suggest(new Suggestion(replaceRange, "+"));
					builder.suggest(new Suggestion(replaceRange, "-"));
					replaceRange = range;
				}
				else {
					builder.suggest(new Suggestion(replaceRange, suggestion));
				}
			}
		}
		
		if(StringUtils.isStrictlyNumeric(currentArg)) {
			StringRange old = replaceRange;
			replaceRange = new StringRange(lastValidRange.getEnd(), input.length());
			for(DieType type : DieType.getStandardDiceTypes()) {
				builder.suggest(new Suggestion(replaceRange, lastValidArg + type.toString()));
			}
			replaceRange = old;
		}
		else if(currentArg.equalsIgnoreCase("d")) {
			for(DieType type : DieType.getStandardDiceTypes()) {
				builder.suggest(new Suggestion(replaceRange, type.toString()));
			}
		}
		else {
			Matcher matcher = DICE_REGEX.matcher(currentArg);
			if(matcher.matches()) {
				boolean foundSuggestion = false;
				String amount = matcher.group("amount");
				String die = matcher.group("die");
				if(!amount.isBlank() && die.isBlank()) {
					for(DieType type : DieType.getStandardDiceTypes()) {
						builder.suggest(new Suggestion(replaceRange, amount + "d" + type.getSides() + ""));
						foundSuggestion = true;
					}
				}
				else if (amount.isBlank() && !die.isBlank()) {
					for(DieType type : DieType.getStandardDiceTypes()) {
						if((type.getSides() + "").startsWith(die + "")) {
							builder.suggest(new Suggestion(replaceRange, "d" + type.getSides()));
							foundSuggestion = true;
						}
					}
				}
				else if(!amount.isBlank() && !die.isBlank()) {
					for(DieType type : DieType.getStandardDiceTypes()) {
						if((type.getSides() + "").startsWith(die + "")) {
							System.err.println("Suggested " + type);
							builder.suggest(new Suggestion(replaceRange, amount + "d" + type.getSides()));
							if(currentArg.endsWith("d" + type.getSides())) {
								builder.suggest(new Suggestion(replaceRange, amount + "d" + type.getSides() + "+"));
								builder.suggest(new Suggestion(replaceRange, amount + "d" + type.getSides() + "-"));
							}
							foundSuggestion = true;
						}
					}
				}
				else {
					throw new AssertionError();
				}
				
				if(!foundSuggestion) {
					builder.suggest(new Suggestion(replaceRange, input));
					builder.suggest(new Suggestion(replaceRange, input + "+"));
					builder.suggest(new Suggestion(replaceRange, input + "-"));
				}
			}
		}
		
		return builder.buildFuture();
	}
	
	/**
	 * @param input the full command input
	 * @return the StringRange representing the start and end of the argument
	 */
	private StringRange getLastArgRange(String input) {
		
		int lastSeparator = Math.max(input.indexOf('-'), input.indexOf('+'));
		if(lastSeparator == -1) { 
			return new StringRange(0, input.length());
		}
		else {
			Matcher matcher = DELIMITER_REGEX.matcher(input);
			MatchResult start = null;
			MatchResult end = null;
			
			MatchResult result = null;
			while(matcher.find()) {
				result = matcher.toMatchResult();
			}
			
			return new StringRange(result.start(), result.end());
		}
	}
	
	/**
	 * Gets the last fully complete and valid subargument that the user typed.
	 * 
	 * This argument may or may not be the at the end of the input.
	 * @param input
	 * @return
	 */
	private String getLastValidArg(String input) {
		if(input.isBlank()) {
			return input;
		}
		StringRange range = getLastArgRange(input);
		return input.substring(range.getStart(), range.getEnd());
	}
	

	private StringRange getCurrentArgRange(String input) {
		StringRange lastRange = getLastArgRange(input);
		StringRange currentRange = new StringRange(lastRange.getEnd(), input.length());
		if(lastRange.getEnd() == currentRange.getEnd()) {
			return lastRange;
		}
		return currentRange;
	}
	
	/**
	 * Gets the last argument that the user typed.
	 * 
	 * This argument may not be valid, and is always at the end of the input.
	 * @param input
	 * @return
	 */
	private String getCurrentArg(String input) {
		if(input.isBlank()) {
			return input;
		}
		StringRange range = getCurrentArgRange(input);
		return input.substring(range.getStart(), range.getEnd());
	}
	
	private boolean isDelimiter(String s) {
		return s.endsWith("+") || s.endsWith("-");
	}
	
}
