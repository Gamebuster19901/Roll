package com.gamebuster19901.roll.bot.command.argument;

import java.util.function.Predicate;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class SubCommandArgumentBuilder<S> extends LiteralArgumentBuilder<S> {

	protected SubCommandArgumentBuilder(String literal) {
		super(literal);
	}
	
	public static <S> SubCommandArgumentBuilder<S> sub(final String name) {
		return new SubCommandArgumentBuilder<>(name);
	}
	
	@Override
	public SubCommandNode<S> build() {
		final SubCommandNode<S> result = new SubCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
		
		for(final CommandNode<S> argument : getArguments()) {
			result.addChild(argument);
		}
		
		return result;
	}

	public static final class SubCommandNode<S> extends LiteralCommandNode<S> {

		public SubCommandNode(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
			super(literal, command, requirement, redirect, modifier, forks);
		}
		
	}
	
}
