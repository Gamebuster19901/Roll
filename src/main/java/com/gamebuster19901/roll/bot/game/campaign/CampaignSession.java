package com.gamebuster19901.roll.bot.game.campaign;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.session.Session;
import com.gamebuster19901.roll.bot.game.session.Sessions;
import com.gamebuster19901.roll.bot.game.session.Sessions.SessionType;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class CampaignSession implements Session {

	protected boolean ended = false;
	private transient GuildMessageChannel channel;
	private transient Instant lastInteraction = Instant.now();
	private transient Campaign campaign;
	private transient LinkedHashSet<Long> activeDMs = new LinkedHashSet<>();
	private transient LinkedHashSet<Long> activePlayers = new LinkedHashSet<>();
	
	public CampaignSession(Campaign campaign, IReplyCallback replyCallback) {
		this.campaign = campaign;
		if(replyCallback != null) {
			this.channel = (GuildMessageChannel) replyCallback.getGuildChannel();
			joinPlayer(replyCallback.getUser());
		}
	}
	
	@Override
	public Set<Long> getActiveDMs() {
		return Collections.unmodifiableSet(activeDMs);
	}
	
	@Override
	public Set<Long> getActivePlayers() {
		return Collections.unmodifiableSet(activePlayers);
	}
	
	@Override
	public void leavePlayer(User player) {
		if(campaign.isDM(player)) {
			activeDMs.remove(player.getIdLong());
			if(activeDMs.size() == 0) {
				end("There are no dungeon masters left in the session.");
			}
		}
		activePlayers.remove(player.getIdLong());
	}
	
	@Override
	public void joinPlayer(User player) {
		if(campaign.isDM(player)) {
			activeDMs.add(player.getIdLong());
		}
		activePlayers.add(player.getIdLong());
	}
	
	@Override
	public boolean hasPlayer(User player) {
		return activeDMs.contains(player.getIdLong()) || activePlayers.contains(player.getIdLong());
	}
	
	@Override
	public boolean hasDM(User dm) {
		return activeDMs.contains(dm.getIdLong());
	}
	
	@Override
	public Statted getActiveCharacter(User player) {
		update();
		if(hasActiveCharacter(player)) {
			return campaign.getActiveChar(player);
		}
		return null;
	}
	
	@Override
	public boolean hasActiveCharacter(User player) {
		Statted statted = Sessions.getSelfSession(player).getActiveCharacter(player);
		if(statted != null) {
			return campaign.hasCharacter(statted);
		}
		return false;
	}
	
	@Override
	public void setActiveCharacter(User player, Statted character) {
		campaign.setActiveChar(player, character);
	}
	
	@Override
	public void save() {
		campaign.save();
	}
	
	@Override
	public void update() {
		lastInteraction = Instant.now();
	}
	
	@Override
	public void end(String reason) {
		channel.sendMessage("The session for " + campaign.getName() + " has ended:\n\n" + reason);
		end(false);
	}
	
	@Override
	public void end(boolean notify) {
		ended = true;
		save();
		if(notify) {
			channel.sendMessage("The session for " + campaign.getName() + " has ended.");
		}
		campaign.session = new NoSession(campaign);
	}
	
	@Override
	public Instant getLastInteraction() {
		return lastInteraction;
	}
	
	@Override
	public SessionType getSessionType() {
		return SessionType.CAMPAIGN;
	}

	@Override
	public boolean ended() {
		return this.ended;
	}
	
}
