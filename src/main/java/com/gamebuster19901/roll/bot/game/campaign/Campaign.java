package com.gamebuster19901.roll.bot.game.campaign;

import java.nio.file.Path;
import java.util.LinkedHashSet;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.game.Statted;

import net.dv8tion.jda.api.entities.User;

public class Campaign {

	public static final Path CAMPAIGN_FOLDER = Main.RUN_DIR.toPath().resolve("Campaigns");
	static {
		CAMPAIGN_FOLDER.toFile().mkdirs();
	}
	
	private String name;
	private Long owner;
	private LinkedHashSet<Long> dungeonMasters;
	private LinkedHashSet<Long> players;
	private LinkedHashSet<Long> characters;
	private CampaignSession session = new NoSession(this);
	
	public Campaign(String name, User owner) {
		this(name, owner.getIdLong());
	}
	
	public Campaign(String name, Long owner) {
		addDM(owner);
	}
	
	public void addPlayer(User player) {
		addPlayer(player.getIdLong());
	}
	
	public void addPlayer(Long player) {
		players.add(player);
	}
	
	public void removePlayer(User player) {
		removePlayer(player.getIdLong());
	}
	
	public void removePlayer(Long player) {
		if(!owner.equals(player)) {
			dungeonMasters.remove(player);
			players.remove(player);
			session.leavePlayer(player);
		}
	}
	
	public void addCharacter(Statted statted) {
		addCharacter(statted.getID());
	}
	
	public void addCharacter(Long statted) {
		characters.add(statted);
	}
	
	public void removeCharacter(Statted statted) {
		removeCharacter(statted.getID());
	}
	
	public void removeCharacter(Long statted) {
		characters.remove(statted);
	}
	
	public void addDM(User dm) {
		addDM(dm.getIdLong());
	}
	
	public void addDM(Long dm) {
		players.add(dm);
		dungeonMasters.add(dm);
		if(session.getActivePlayers().contains(dm)) {
			session.joinPlayer(Main.discordBot.jda.getUserById(dm));
		}
	}
	
	public void removeDM(User dm) {
		removeDM(dm.getIdLong());
	}
	
	public void removeDM(Long dm) {
		if(!isOwner(dm)) {
			dungeonMasters.remove(dm);
			
		}
	}
	
	public boolean isOwner(User user) {
		return isOwner(user.getIdLong());
	}
	
	public boolean isOwner(Long user) {
		return owner.equals(user);
	}
	
	public boolean isDM(User user) {
		return isDM(user.getIdLong());
	}
	
	public boolean isDM(Long user) {
		return dungeonMasters.contains(user);
	}
	
	public boolean isPlayer(User user) {
		return isPlayer(user.getIdLong());
	}
	
	public boolean isPlayer(Long user) {
		return players.contains(user);
	}
	
	public LinkedHashSet<Long> getDMs() {
		LinkedHashSet<Long> ret =  new LinkedHashSet<>();
		ret.addAll(dungeonMasters);
		return ret;
	}
	
	public LinkedHashSet<Long> getPlayers() {
		LinkedHashSet<Long> ret =  new LinkedHashSet<>();
		ret.addAll(players);
		return ret;
	}
	
	public Long getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
}
