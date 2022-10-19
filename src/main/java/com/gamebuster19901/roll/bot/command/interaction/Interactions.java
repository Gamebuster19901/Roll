package com.gamebuster19901.roll.bot.command.interaction;

import java.util.List;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Dispatcher;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.Component.Type;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class Interactions {

	private final CommandDispatcher<CommandContext> dispatcher = new Dispatcher();
	public static final Interactions DISPATCHER = new Interactions();
	
	public Interactions() {
		CharacterImportInteraction.register(dispatcher);
	}
	
	public static void execute(CommandContext context, String command) throws CommandSyntaxException {
		DISPATCHER.getDispatcher().execute(command, context);
	}
	
	public static void execute(ModalInteractionEvent e) throws CommandSyntaxException {
		List<ModalMapping> arguments = e.getValues();
		StringBuilder command = new StringBuilder(e.getModalId());
		for(ModalMapping arg : arguments) {
			command.append(' ');
			if(arg.getType() == Type.TEXT_INPUT) {
				command.append(arg.getAsString());
			}
			else if (arg.getType() == Type.BUTTON) {
				command.append(arg.getId());
			}
			else if (arg.getType() == Type.SELECT_MENU) {
				command.append(arg.getId());
			}
		}
		DISPATCHER.getDispatcher().execute(command.toString(), new CommandContext(e));
	}
	
	public static void execute(ButtonInteractionEvent e) throws CommandSyntaxException {
		DISPATCHER.getDispatcher().execute(e.getButton().getId(), new CommandContext(e));
	}
	
	public static LiteralArgumentBuilder<CommandContext<?>> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}
	
	public static <T> RequiredArgumentBuilder<CommandContext<?>, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
	
	public CommandDispatcher<CommandContext> getDispatcher() {
		return this.dispatcher;
	}
	
	public static boolean isValidPrefix(String prefix) {
		if(prefix == null || prefix.isEmpty()) {
			return false;
		}
		for(int c : prefix.toCharArray()) {
			if(Character.isWhitespace(c) || Character.isSupplementaryCodePoint(c) || Character.isISOControl(c) || c == '@' || c == '#' || c == '`') {
				return false;
			}
		}
		return true;
	}
	
	public static String readString(StringReader reader) {
		StringBuilder ret = new StringBuilder("");
		while(reader.canRead() && !Character.isSpaceChar(reader.peek())) {
			ret.append(reader.read());
		}
		return ret.toString();
	}
	
	public static String readQuotedString(StringReader reader) throws CommandSyntaxException {
		StringBuilder ret = new StringBuilder("");
		if(reader.canRead()) {
			if(reader.peek() == '"') {
				reader.read();
			}
			else {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(reader);
			}
		}
		boolean foundEndQuote = false;
		while(reader.canRead()) {
			char c = reader.read();
			if(c == '"') {
				foundEndQuote = true;
				break;
			}
			ret.append(c);
		}
		if(!foundEndQuote) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(reader);
		}
		return ret.toString();
	}
	
}
