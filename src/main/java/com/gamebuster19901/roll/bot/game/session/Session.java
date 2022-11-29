package com.gamebuster19901.roll.bot.game.session;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import com.gamebuster19901.roll.bot.game.Savable;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.session.Sessions.SessionType;
import com.gamebuster19901.roll.gson.GSerializable;

import net.dv8tion.jda.api.entities.User;

public interface Session extends GSerializable, Savable {
	
	public Set<Long> getActiveDMs();
	
	public Set<Long> getActivePlayers();
	
	public void leavePlayer(User player);
	
	public void joinPlayer(User player);
	
	public boolean hasPlayer(User player);
	
	public boolean hasDM(User dm);
	
	public Statted getActiveCharacter(User player);
	
	public boolean hasActiveCharacter(User player);
	
	public void setActiveCharacter(User player, Statted character);
	
	public void save();
	
	public void update();
	
	public void end(String reason);
	
	public void end(boolean notify);
	
	public Instant getLastInteraction();
	
	public default boolean expired() {
		return getLastInteraction().plus(Duration.ofSeconds(5)).isBefore(Instant.now());
		//return getLastInteraction().plus(Duration.ofHours(1)).isAfter(Instant.now());
	}
	
	public boolean ended();
	
	public SessionType getSessionType();
	
}
