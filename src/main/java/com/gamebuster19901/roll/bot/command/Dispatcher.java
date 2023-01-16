package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.audit.CommandAudit;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class Dispatcher extends CommandDispatcher<CommandContext> {

	@Override
	public int execute(final StringReader input, final CommandContext source) throws CommandSyntaxException {
		String command = input.getString();
		CommandAudit.addCommandAudit(source, command);
		source.constructEmbedResponse(command);
		System.out.println(command);
		return super.execute(input, source);
	}
	
	@SuppressWarnings("unchecked")
	public LiteralCommandNode<CommandContext<?>> register(LiteralArgumentBuilder command, String description) {
		
		this.getRoot().getChildren().forEach((e) -> e.getName());
		
		return super.register(command);
	}
	
}
