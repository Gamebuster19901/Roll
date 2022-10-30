package com.gamebuster19901.roll.bot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class Dispatcher extends CommandDispatcher<CommandContext> {

	@Override
	public int execute(final StringReader input, final CommandContext source) throws CommandSyntaxException {
		source.constructEmbedResponse(input.getString());
		System.out.println(input.getString());
		return super.execute(input, source);
	}
	
	@SuppressWarnings("unchecked")
	public LiteralCommandNode<CommandContext<?>> register(LiteralArgumentBuilder command, String description) {
		
		this.getRoot().getChildren().forEach((e) -> e.getName());
		
		return super.register(command);
	}
	
}
