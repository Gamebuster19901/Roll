package com.gamebuster19901.roll.bot.command.exception;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public interface ParseExceptions {
	
	public DynamicCommandExceptionType INVALID_PDF_DOMAIN = new DynamicCommandExceptionType(url -> new LiteralMessage("`" + url + "` is not a PDF I know how to parse"));
	public Dynamic2CommandExceptionWithCause UNABLE_TO_PARSE_PDF_VALUE = new Dynamic2CommandExceptionWithCause((name, value, reason) -> new LiteralMessage("Unable to parse `" + name + "` field from PDF.\n\nUnparsable value: `" + value + "`\n\nReason: " + reason.getClass().getCanonicalName() + " - "+ reason.getMessage()));
	
	public Dynamic2CommandExceptionType NONEXISTANT = new Dynamic2CommandExceptionType((type, value) -> new LiteralMessage(type + " `" + value + "` does not exist or could not be found."));
	
	public DynamicCommandExceptionType NO_DICE_TO_ROLL = new DynamicCommandExceptionType((value) -> new LiteralMessage("No dice to roll! `" + value + "` contains no values or dice!"));
	public SimpleCommandExceptionType TOO_MANY_DICE = new SimpleCommandExceptionType(new LiteralMessage("Too many dice!"));
}
