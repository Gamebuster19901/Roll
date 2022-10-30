package com.gamebuster19901.roll.bot.command.interaction;

public abstract class ConfirmationThread extends Thread {
	
	public static final String PREFIX = "Confirmation Thread ";
	
	boolean accepted = false;
	boolean rejected = false;
	
	public ConfirmationThread(long id) {
		this.setName(PREFIX + id);
	}
	
	public synchronized void accept() {
		accepted = true;
		this.notify();
	}
	
	public synchronized void reject() {
		rejected = true;
		this.notify();
	}
	
}
