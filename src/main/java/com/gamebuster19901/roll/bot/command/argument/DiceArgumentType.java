package com.gamebuster19901.roll.bot.command.argument;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.DieType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class DiceArgumentType implements ArgumentType<Dice> {

	public static final DiceArgumentType DICE_ARGUMENT_TYPE = new DiceArgumentType();
	
	private DiceArgumentType() {}
	
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
		if(Commands.lastArgOf(builder.getInput()).equals("d")) {
			builder.suggest("<sides>");
			for(DieType type : DieType.getStandardDiceTypes()) {
				builder.suggest(type.name());
			}
			return builder.buildFuture();
		}
		if(builder.getInput().endsWith("-") || builder.getInput().endsWith("+")) {
			builder.suggest(Commands.lastArgOf(builder.getInput()) + "<dice>");
			builder.suggest(Commands.lastArgOf(builder.getInput()) + "<integer>");
			builder.suggest(Commands.lastArgOf(builder.getInput()) + "<stat>");
		}
		return builder.buildFuture();
	}
	
}
