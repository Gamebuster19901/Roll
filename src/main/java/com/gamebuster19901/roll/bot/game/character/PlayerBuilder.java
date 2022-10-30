package com.gamebuster19901.roll.bot.game.character;

import java.sql.SQLException;

import net.dv8tion.jda.api.entities.User;

public class PlayerBuilder {
	
	private final User owner;
	protected PlayerCharacterStatBuilder stats;
	
	public PlayerBuilder(User owner, PlayerCharacterStatBuilder stats) {
		this(owner, stats, 0);
	};
	
	public PlayerBuilder(User owner, PlayerCharacterStatBuilder stats, long characterID) {
		this.owner = owner;
		this.stats = stats;
	}
	
	public void checkValid() {
		stats.validate();
	}
	
	public PlayerCharacter build() throws SQLException {
		checkValid();
		return new PlayerCharacter(stats);
	}
	
}
