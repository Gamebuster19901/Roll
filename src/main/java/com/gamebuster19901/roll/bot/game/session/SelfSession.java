package com.gamebuster19901.roll.bot.game.session;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.game.Savable;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.session.Sessions.SessionType;
import com.gamebuster19901.roll.bot.game.user.Users;

import net.dv8tion.jda.api.entities.User;

public class SelfSession implements Session, Savable {

	private transient Instant lastInteraction = Instant.now();
	private final Set<Long> user;
	private Statted character;
	
	@SuppressWarnings("deprecation")
	public SelfSession(User user) {
		this.user = Collections.singleton(user.getIdLong());
		this.character = Users.getActiveCharacterFromDB(user.getIdLong());
		System.out.println("Started a self session for " + user.getAsTag());
	}
	
	@Override
	public Set<Long> getActiveDMs() {
		return user;
	}

	@Override
	public Set<Long> getActivePlayers() {
		return user;
	}

	@Override
	public void leavePlayer(User player) {
		throw new AssertionError("Leaving a self session?!");
	}

	@Override
	public void joinPlayer(User player) {
		throw new AssertionError("Joining a self session?!");
	}
	
	@Override
	public boolean hasPlayer(User player) {
		return user.contains(player.getIdLong());
	}

	@Override
	public boolean hasDM(User dm) {
		return user.contains(dm.getIdLong());
	}
	
	public long getPlayer() {
		return user.iterator().next();
	}

	@Override
	public void update() {
		lastInteraction = Instant.now();
	}
	
	public void save() {
		if(character instanceof Savable) { //Note: don't call getActiveCharacter() so we don't update
			((Savable) character).save();
		}
	}

	@Override
	public void end(String reason) {
		save();
		Sessions.SELF_SESSIONS.remove(this);
		System.out.println("Session ended for " + Main.discordBot.jda.retrieveUserById(getPlayer()).complete().getAsTag());
	}
	
	@Override
	public boolean hasActiveCharacter(User player) {
		return hasPlayer(player) && character != null;
	}

	@Override
	public Statted getActiveCharacter(User player) {
		update();
		if (hasPlayer(player)) {
			return character;
		}
		else {
			throw new IllegalStateException("Default session does not belong to " + player.getAsTag());
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void setActiveCharacter(User player, Statted character) {
		update();
		if(hasPlayer(player)) {
			save();
			this.character = character;
			Users.setActiveCharacterInDB(player, character);
			save();
		}
		else {
			throw new IllegalArgumentException("Default session does not belong to " + player.getAsTag());
		}
	}
	
	@Override
	public Instant getLastInteraction() {
		return lastInteraction;
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.SELF;
	}

	@Override
	public void end(boolean notify) {
		//no-op
	}

	@Override
	public boolean ended() {
		return false;
	}

}
