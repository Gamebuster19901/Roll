package com.gamebuster19901.roll.bot.command.argument;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mysql.cj.util.StringUtils;

public class DiceArgumentType implements ArgumentType<Dice> {
	
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
		if(builder.getInput().isBlank()) {
			builder.suggest("<Dice>");
			return builder.buildFuture();
		}
		
		if(input.isBlank()) {
			builder.suggest("<Dice>");
			return builder.buildFuture();
		}
		
		String lastArg;
		int index = Math.max(input.lastIndexOf('+') + 1, input.lastIndexOf('-') + 1);
		System.err.println("Index: " + index);
		System.err.println("Length: " + input.length());
		if(index == 0) {
			lastArg = input.substring(0, input.length());
		}
		else {
			lastArg = input.substring(index, input.length());
		}
		
		System.err.println("Input: " + input);
		System.err.println("Last Arg: " + lastArg);
		if(!input.endsWith("+") && !input.endsWith("-")) {
			for(String suggestion : suggestions) {
				if(suggestion.startsWith(lastArg)) {
					System.out.println("Suggestion: " + suggestion);
					if(suggestion.equalsIgnoreCase(lastArg)) {
						builder.forceSuggest(input + "+");
						builder.forceSuggest(input + "-");
					}
					else {
						builder.forceSuggest(input.substring(0, input.indexOf(lastArg)) + suggestion);
					}
				}
			}
			
			if(StringUtils.isStrictlyNumeric(lastArg)) {
				for(DieType type : DieType.getStandardDiceTypes()) {
					builder.forceSuggest(input + type);
				}
			}
			else if (lastArg.equalsIgnoreCase("d")) {
				for(DieType type : DieType.getStandardDiceTypes()) {
					builder.forceSuggest(input + type.getSides());
				}
			}
			else {
				Matcher matcher = DICE_REGEX.matcher(lastArg);
				if(matcher.matches()) {
					boolean foundSuggestion = false;
					String amount = matcher.group("amount");
					String die = matcher.group("die");
					if(!amount.isBlank() && die.isBlank()) {
						for(DieType type : DieType.getStandardDiceTypes()) {
							builder.forceSuggest(input + type.getSides());
							foundSuggestion = true;
						}
					}
					else if (amount.isBlank() && !die.isBlank()) {
						for(DieType type : DieType.getStandardDiceTypes()) {
							if((type.getSides() + "").startsWith(die + "")) {
								builder.forceSuggest(input.substring(0, input.indexOf(lastArg)) + "d" + type.getSides());
								foundSuggestion = true;
							}
						}
					}
					else if(!amount.isBlank() && !die.isBlank()) {
						for(DieType type : DieType.getStandardDiceTypes()) {
							if((type.getSides() + "").startsWith(die + "")) {
								System.err.println("Suggested " + type);
								builder.forceSuggest(input.substring(0, input.indexOf(lastArg)) + amount + "d" + type.getSides());
								foundSuggestion = true;
							}
						}
					}
					else {
						throw new AssertionError();
					}
					
					if(!foundSuggestion) {
						builder.forceSuggest(input);
						builder.forceSuggest(input + "+");
						builder.forceSuggest(input + "-");
					}
				}
			}
			
			List<Suggestion> suggestions = builder.build().getList();
			if(suggestions.size() == 1) {
				if(suggestions.get(0).getText().equalsIgnoreCase(input)) {
					builder.forceSuggest(input + "+");
					builder.forceSuggest(input + "-");
				}
				else {
					System.err.println(suggestions.get(0).getText() + " != " + input);
				}
			}
			
		}
		else {
			builder.suggest(input + "<Dice>");
			for(String suggestion : suggestions) {
				builder.forceSuggest(input + suggestion);
			}
		}
		
		/*if(input.endsWith("-") || input.endsWith("+")) {
			foundSuggestion = true;
			builder.suggest(input + "<Dice>");
			for(String suggestion : suggestions) {
				builder.suggest(input + suggestion);
			}
		}
		
		if(!foundSuggestion) {
			for(String suggestion : suggestions) {
				if(input.endsWith(suggestion)) {
					builder.suggest(input + "+");
					builder.suggest(input + "-");
					foundSuggestion = true;
				}
			}
		}
		if(!foundSuggestion && !input.endsWith("d")) {
			builder.suggest(input + "+");
			builder.suggest(input + "-");
		}
		else if (!foundSuggestion && input.endsWith("d")) {
			for(DieType type : DieType.getStandardDiceTypes()) {
				builder.suggest(input.substring(0, input.length() - 1) + type.name());
			}
		}*/
		
		return builder.buildFuture();
	}
	
	private void suggestDice(String input, String lastArg, SuggestionsBuilder suggestionsBuilder) {
		
	}
	
}
