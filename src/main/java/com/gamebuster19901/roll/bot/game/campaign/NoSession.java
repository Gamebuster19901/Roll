package com.gamebuster19901.roll.bot.game.campaign;

import java.time.Instant;
import java.util.Set;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.session.Sessions.SessionType;

import net.dv8tion.jda.api.entities.User;

public class NoSession extends CampaignSession {
	
	public NoSession(Campaign campaign) {
		super(campaign, null);
	}

	@Override
	public Set<Long> getActiveDMs() {
		throw new AssertionError();
	}

	@Override
	public Set<Long> getActivePlayers() {
		throw new AssertionError();
	}

	@Override
	public void leavePlayer(User player) {
		throw new AssertionError();
	}

	@Override
	public void joinPlayer(User player) {
		throw new AssertionError();
	}

	@Override
	public boolean hasPlayer(User player) {
		throw new AssertionError();
	}

	@Override
	public boolean hasDM(User dm) {
		throw new AssertionError();
	}

	@Override
	public Statted getActiveCharacter(User player) {
		throw new AssertionError();
	}

	@Override
	public boolean hasActiveCharacter(User player) {
		throw new AssertionError();
	}

	@Override
	public void setActiveCharacter(User player, Statted character) {
		throw new AssertionError();
	}

	@Override
	public void save() {
		throw new AssertionError();
	}

	@Override
	public void update() {
		throw new AssertionError();
	}

	@Override
	public void end(String reason) {
		throw new AssertionError();
	}

	@Override
	public void end(boolean notify) {
		throw new AssertionError();
	}

	@Override
	public Instant getLastInteraction() {
		throw new AssertionError();
	}

	@Override
	public boolean ended() {
		throw new AssertionError();
	}

	@Override
	public SessionType getSessionType() {
		throw new AssertionError();
	}
	
	@Override
	public boolean expired() {
		throw new AssertionError();
	}

}
