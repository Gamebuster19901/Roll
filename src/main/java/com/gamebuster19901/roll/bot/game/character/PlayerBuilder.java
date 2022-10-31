package com.gamebuster19901.roll.bot.game.character;

import net.dv8tion.jda.api.entities.User;

public class PlayerBuilder {
	
	protected PlayerCharacterStatBuilder stats;
	
	public PlayerBuilder(User owner, PlayerCharacterStatBuilder stats) {
		this(owner, stats, 0);
	};
	
	public PlayerBuilder(User owner, PlayerCharacterStatBuilder stats, long characterID) {
		this.stats = stats;
	}
	
	public void checkValid() {
		stats.validate();
	}
	
	public PlayerCharacter build() {
		checkValid();
		return new PlayerCharacter(stats.build());
	}
	
}
