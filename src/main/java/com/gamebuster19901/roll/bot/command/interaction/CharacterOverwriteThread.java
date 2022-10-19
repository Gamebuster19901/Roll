package com.gamebuster19901.roll.bot.command.interaction;

public abstract class CharacterOverwriteThread extends Thread {
	
	boolean accepted = false;
	boolean rejected = false;
	
	public void accept() {
		accepted = true;
		this.notify();
	}
	
	public void reject() {
		rejected = true;
		this.notify();
	}
	
}
