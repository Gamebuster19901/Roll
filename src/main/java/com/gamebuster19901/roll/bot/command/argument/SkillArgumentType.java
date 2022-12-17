package com.gamebuster19901.roll.bot.command.argument;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class SkillArgumentType implements ArgumentType<Skill>{
	
	public static final SkillArgumentType ANY_SKILL = SkillArgumentType.of(Skill.DEFAULT_SKILLS);
	
	private Skill[] skills;
	
	private SkillArgumentType(Skill... skills) {
		this.skills = skills;
	}
	
	@Override
	public <S> Skill parse(S context, StringReader reader) throws CommandSyntaxException {
		String lookingFor = Commands.readString(reader);
		for(Skill skill : skills) {
			if(skill.getName().replace("_", "").replace(" ", "").equalsIgnoreCase(lookingFor)) {
				return skill;
			}
		}
		throw ParseExceptions.INVALID_SKILL.create(lookingFor);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(Skill skill : skills) {
			if(skill.getCommandArg().toLowerCase().startsWith(Commands.lastArgOf(builder.getInput()).toLowerCase())) {
				builder.suggest(skill.getCommandArg());
			}
		}
		return builder.buildFuture();
	}
	
	public static SkillArgumentType of(Skill... skills) {
		if(skills.length > 0) {
			return new SkillArgumentType(skills);
		}
		throw new IllegalArgumentException("Argument type must accept at least one skill!");
	}
	
}
