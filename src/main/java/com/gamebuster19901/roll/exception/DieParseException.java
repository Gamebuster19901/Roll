package com.gamebuster19901.roll.exception;

public class DieParseException extends RuntimeException {

	public DieParseException(int cursor, Throwable cause) {
		super(cause);
	}
	
}
