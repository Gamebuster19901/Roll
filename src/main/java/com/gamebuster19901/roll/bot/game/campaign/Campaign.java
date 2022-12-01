package com.gamebuster19901.roll.bot.game.campaign;

import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.annotation.Nullable;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.database.sql.ResultSet;
import com.gamebuster19901.roll.bot.game.Savable;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.gamebuster19901.roll.gson.GSerializable;
import static com.gamebuster19901.roll.bot.database.Table.*;
import static com.gamebuster19901.roll.bot.database.Column.*;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class Campaign implements GSerializable, Savable {

	public static final Path CAMPAIGN_FOLDER = Main.RUN_DIR.toPath().resolve("Campaigns");
	static {
		CAMPAIGN_FOLDER.toFile().mkdirs();
	}
	
	private String name;
	private Long owner;
	private final Long id;
	private final LinkedHashSet<Long> dungeonMasters = new LinkedHashSet<>();
	private final LinkedHashSet<Long> players = new LinkedHashSet<>();
	private final LinkedHashSet<Statted> characters = new LinkedHashSet<>();
	private final LinkedHashMap<Long, Statted> activeChars = new LinkedHashMap<>();
	CampaignSession session = new NoSession(this);
	
	protected Campaign(String name, User owner, long id) {
		this.owner = owner.getIdLong();
		this.name = name;
		this.id = id;
		addDM(owner);
	}
	
	public void addPlayer(User player) {
		players.add(player.getIdLong());
	}
	
	public void removePlayer(User player) {
		if(!owner.equals(player.getIdLong())) {
			if(hasSession()) {
				session.leavePlayer(player);
			}
			dungeonMasters.remove(player.getIdLong());
			players.remove(player.getIdLong());
		}
	}
	
	public void addCharacter(Statted statted) {
		characters.add(statted);
		if(statted.hasStat(Stat.Owner)) {
			setActiveChar(Users.getUser(statted.getStat(Stat.Owner, Long.class)), statted);
		}
	}
	
	@Deprecated
	public Statted getActiveChar(User player) {
		return activeChars.get(player);
	}
	
	public void setActiveChar(User player, @Nullable Statted statted) {
		Statted previousChar = activeChars.get(player.getIdLong());
		if(!characters.contains(statted)) {
			return; //character not in campaign!
		}
		if(previousChar != null && previousChar instanceof Savable) {
			((Savable) previousChar).save();
		}
		if(statted == null) {
			activeChars.remove(player.getIdLong());
		}
		else {
			activeChars.put(player.getIdLong(), statted);
		}
		if(hasSession()) {
			session.update();
		}
	}
	
	public void removeCharacter(Statted statted) {
		activeChars.entrySet().forEach((entry) -> {
			if(entry.getValue().equals(statted)) {
				setActiveChar(Main.discordBot.jda.getUserById(entry.getKey()), null);
				activeChars.remove(entry.getKey());
			}
		});
		characters.remove(statted);
	}
	
	public boolean hasCharacter(Statted statted) {
		return characters.add(statted);
	}
	
	public void addDM(User dm) {
		addDM(dm.getIdLong());
	}
	
	public void addDM(Long dm) {
		players.add(dm);
		dungeonMasters.add(dm);
		if(hasSession() && session.getActivePlayers().contains(dm)) {
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
	
	public boolean hasSession() {
		return !(session instanceof NoSession);
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
	
	public void startSession(IReplyCallback callback) {
		if(session instanceof NoSession) {
			session = new CampaignSession(this, callback);
		}
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
	
	public Long getID() {
		return id;
	}
	
	public String toString() {
		return name;
	}

	@Override
	public void save() {
		File folder = getCampaignFolder().toFile();
		if(!folder.exists()) {
			folder.mkdirs();
		}
		try {
			FileWriter writer = new FileWriter(getCampaignFile().toFile());
			writer.write(Main.gson.toJson(this));
			writer.close();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	public Path getCampaignFolder() {
		return CAMPAIGN_FOLDER.resolve("" + this.getID());
	}
	
	public Path getCampaignFile() {
		return getCampaignFolder().resolve("campaign.json");
	}
	
	public static Campaign createNewCampaign(String name, User owner) {
		try {
			PreparedStatement insertion = Insertion.insertInto(CAMPAIGNS).setColumns(DISCORD_ID, NAME).to(owner.getIdLong(), name).prepare(true);
			insertion.execute();
			ResultSet results = insertion.getGeneratedKeys();
			results.next();
			long auditID = results.getLong(GENERATED_KEY);
			Campaign campaign = new Campaign(name, owner, results.getLong(GENERATED_KEY));
			campaign.save();
			return campaign;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
