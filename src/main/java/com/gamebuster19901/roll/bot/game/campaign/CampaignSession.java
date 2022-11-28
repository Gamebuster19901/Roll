package com.gamebuster19901.roll.bot.game.campaign;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import net.dv8tion.jda.api.entities.User;

public class CampaignSession {

	private transient Instant lastInteraction = Instant.now();
	private transient Campaign campaign;
	private transient LinkedHashSet<Long> activeDMs;
	private transient LinkedHashSet<Long> activePlayers;
	
	public CampaignSession(Campaign campaign) {
		this.campaign = campaign;
	}
	
	public void joinPlayer(User player) {
		if(campaign.isDM(player)) {
			activeDMs.add(player.getIdLong());
		}
		activePlayers.add(player.getIdLong());
	}
	
	public void leavePlayer(User player) {
		leavePlayer(player);
	}
	
	public void leavePlayer(Long player) {
		update();
		activeDMs.remove(player);
		activePlayers.remove(player);
		if(activeDMs.size() < 0) {
			end("There are no dungeon masters left in the session!");
		}
	}
	
	public Long getCampaignOwner() {
		update();
		return campaign.getOwner();
	}
	
	public Set<Long> getDMs() {
		update();
		return campaign.getDMs();
	}
	
	public LinkedHashSet<Long> getPlayers() {
		update();
		return campaign.getPlayers();
	}
	
	public LinkedHashSet<Long> getActiveDMs() {
		update();
		return campaign.getDMs();
	}
	
	public LinkedHashSet<Long> getActivePlayers() {
		update();
		return campaign.getPlayers();
	}
	
	private void update() {
		lastInteraction = Instant.now();
	}
	
	public void end(String reason) {
		end();
	}
	
	public void end() {
		activeDMs.clear();
		activePlayers.clear();
	}
	
}
