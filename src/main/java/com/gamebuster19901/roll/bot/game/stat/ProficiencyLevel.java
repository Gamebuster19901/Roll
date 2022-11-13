package com.gamebuster19901.roll.bot.game.stat;

import com.gamebuster19901.roll.bot.server.Emote;

public enum ProficiencyLevel {

	NOT_PROFICIENT,
	HALF_PROFICIENT(Emote.getEmoji("halfProficient")),
	PROFICIENT(Emote.getEmoji("proficient")),
	EXPERTISE(Emote.getEmoji("expertise"));
	
	String emote;
	
	private ProficiencyLevel() {
		this(null);
	}
	
	private ProficiencyLevel(String emote) {
		this.emote = emote;
	}
	
	public String getEmoji() {
		if(emote != null) {
			return emote;
		}
		return "";
	}
	
}
