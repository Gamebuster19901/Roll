package com.gamebuster19901.roll.bot.command.exception;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public interface ParseExceptions {
	
	public DynamicCommandExceptionType INVALID_PDF_DOMAIN = new DynamicCommandExceptionType(url -> new LiteralMessage("`" + url + "` is not a PDF I know how to parse"));
	public Dynamic2CommandExceptionWithCause UNABLE_TO_PARSE_PDF_VALUE = new Dynamic2CommandExceptionWithCause((name, value, reason) -> new LiteralMessage("Unable to parse `" + name + "` field from PDF.\n\nUnparsable value: `" + value + "`\n\nReason: " + reason.getClass().getCanonicalName() + " - "+ reason.getMessage()));
	
}
