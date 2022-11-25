package com.gamebuster19901.roll.bot.game.character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.Main;
import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Order;
import com.gamebuster19901.roll.bot.database.Pagination;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.database.Table;
import static com.gamebuster19901.roll.bot.database.Table.*;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.bot.graphics.ImageResource;
import com.gamebuster19901.roll.util.TriFunction;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.dv8tion.jda.api.entities.User;

public class PlayerCharacter implements Statted {
	public static final Path CHARACTER_FOLDER = Main.RUN_DIR.toPath().resolve("Characters");
	static {
		CHARACTER_FOLDER.toFile().mkdirs();
	}

	protected final PlayerCharacterStats stats;
	protected ImageResource image;
	
	public PlayerCharacter(PlayerCharacterStats stats) {
		this.stats = stats;
		this.image = DefaultCharacterImageResource.INSTANCE;
	}
	
	@Override
	public String getName() {
		return stats.getName();
	}
	
	public long getID() {
		return stats.getID();
	}

	@Override
	public int getAbilityScore(Ability ability) {
		return stats.getAbilityScore(ability);
	}

	@Override
	public int getModifier(GameLayer layer, Ability ability) {
		return stats.getModifier(layer, ability);
	}

	@Override
	public ProficiencyLevel getProficiency(GameLayer layer, Ability ability) {
		return stats.getProficiency(layer, ability);
	}

	@Override
	public int getModifier(GameLayer layer, Skill skill) {
		return stats.getModifier(layer, skill);
	}

	@Override
	public ProficiencyLevel getProficiency(Skill skill) {
		return stats.getProficiency(skill);
	}

	@Override
	public int getHP() {
		return stats.getHP();
	}

	@Override
	public int getHP(GameLayer layer) {
		return stats.getHP(layer);
	}

	@Override
	public int getTempHP(GameLayer layer) {
		return stats.getTempHP(layer);
	}

	@Override
	public int getSpeed(GameLayer effect, MovementType movementType) {
		return stats.getSpeed(effect, movementType);
	}

	@Override
	public int getAC(GameLayer layer) {
		return stats.getAC(layer);
	}

	@Override
	public ProficiencyLevel getProficiency(GameLayer layer, Skill skill) {
		return stats.getProficiency(layer, skill);
	}

	@Override
	public int getMaxHP(GameLayer layer) {
		return stats.getMaxHP(layer);
	}

	@Override
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type) {
		return stats.getStat(layer, stat, type);
	}

	@Override
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func) {
		stats.addStat(value, func);
	}
	
	@Override
	public boolean hasStat(GameLayer layer, Stat stat) {
		return stats.hasStat(layer, stat);
	}

	@Override
	public boolean hasStatAt(GameLayer layer, Stat stat) {
		return stats.hasStatAt(layer, stat);
	}

	@Override
	public void setVariables(GameLayer layer, Expression expression) {
		stats.setVariables(layer, expression);
	}
	
	public void setImage(ImageResource image) {
		this.image = image;
		try {
			addCharacterToDatabase(this, true);
		} catch (SQLException | IOException e) {
			throw new IOError(e);
		}
	}
	
	public User getOwner() {
		return getOwner(getID());
	}
	
	public File getCharacterFolder() {
		return getCharacterFolder(getID());
	}
	
	protected File getJsonFile() {
		return getCharacterFile(getID());
	}
	
	public BufferedImage getCharacterImage() {
		return image.getImage();
	}
	
	public InputStream getCharacterImageStream() throws IOException {
		return image.getImageStream();
	}
	
	public static User getOwner(long id) {
		Result result = Table.selectColumnsFromWhere(DISCORD_ID, CHARACTERS, new Comparison(CHARACTER_ID, EQUALS, id));
		result.next();
		return Main.discordBot.jda.retrieveUserById(result.getLong(DISCORD_ID)).complete();
	}
	
	private static File getCharacterFolder(long id) {
		return getCharacterPath(id).toFile();
	}
	
	private static File getCharacterFile(long id) {
		return getCharacterPath(id).resolve("stats.json").toFile();
	}
	
	private static Path getCharacterPath(long id) {
		return CHARACTER_FOLDER.resolve(id + "");
	}
	
	public static boolean exists(long id) {
		return Table.existsWhere(CHARACTERS, new Comparison(CHARACTER_ID, EQUALS, id));
	}
	
	public static final long genNewCharacterID(User owner) throws SQLException {
		PreparedStatement s = Insertion.insertInto(CHARACTERS).setColumns(DISCORD_ID).to(owner.getIdLong()).prepare(true);
		s.execute();
		return s.getGeneratedKeys().getLong(CHARACTER_ID);
	}
	
	public static void removeCharacter(long characterID) throws SQLException {
		Table.deleteWhere(CHARACTERS, new Comparison(CHARACTER_ID, EQUALS, characterID));
		getCharacterFolder(characterID).delete();
	}
	
	public static PlayerCharacter addCharacterToDatabase(PlayerCharacter character, boolean overwrite) throws SQLException, IOException {
		if(overwrite && exists(character.getID())) {
			removeCharacter(character.getID());
		}
		PreparedStatement s = Insertion.insertInto(Table.CHARACTERS)
				.setColumns(
						Column.CHARACTER_ID, 
						Column.DISCORD_ID, 
						Column.NAME)
				.to(
					character.getStat(Stat.ID, long.class), 
					character.getStat(Stat.Owner, long.class), 
					character.getStat(Stat.Name, String.class)
		).prepare(true);
		s.execute();
		character.getCharacterFolder().mkdirs();
		character.getJsonFile().createNewFile();
		FileWriter fw = new FileWriter(character.getJsonFile());
		fw.write(Main.gson.toJson(character));
		fw.close();
		return character;
	}
	
	public static PlayerCharacter deserialize(long characterID) {
		try {
			return Main.gson.fromJson(new FileReader(getCharacterFile(characterID)), PlayerCharacter.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			throw new IOError(e);
		}
	}
	
	public static String getName(long characterID) {
		Result result = Table.selectColumnsFromWhere(NAME, CHARACTERS, new Comparison(CHARACTER_ID, EQUALS, characterID));
		if(result.next()) {
			return result.getString(NAME);
		}
		return "";
	}
	
	/**
	 * @param user the user to get the active character of
	 * @return the character the user is actively controlling, may be null if the user is not controlling a character.
	 * The character also might not be owned by the user.
	 */
	@Nullable
	public static PlayerCharacter getActiveCharacter(User user) {
		return getActiveCharacter(user.getIdLong());
	}
	
	/**
	 * @param user the id of the user to get the active character of
	 * @return the character the user is actively controlling, may be null if the user is not controlling a character.
	 * The character also might not be owned by the user.
	 */
	@Nullable
	public static PlayerCharacter getActiveCharacter(long discordID) {
		Result result = Table.selectColumnsFromWhere(CURRENT_CHARACTER, PLAYERS, new Comparison(DISCORD_ID, EQUALS, discordID));
		if(result.hasNext()) {
			result.next();
			long id = result.getLong(CURRENT_CHARACTER);
			if(id == 0) {
				return null;
			}
			return PlayerCharacter.deserialize(result.getLong(CURRENT_CHARACTER));
		}
		return null; //no active character
	}
	
	public static boolean hasActiveCharacter(User user) {
		return hasActiveCharacter(user.getIdLong());
	}
	
	public static boolean hasActiveCharacter(long discordID) {
		Result result = Table.selectColumnsFromWhere(CURRENT_CHARACTER, PLAYERS, new Comparison(DISCORD_ID, EQUALS, discordID));
		if(result.hasNext()) {
			result.next();
			return !result.isNull(CURRENT_CHARACTER);
		}
		return false;
	}
	
	public static void setActiveCharacter(User user, PlayerCharacter character) {
		setActiveCharacter(user.getIdLong(), character.getID());
	}
	
	public static void setActiveCharacter(User user, long characterID) {
		setActiveCharacter(user.getIdLong(), characterID);
	}
	
	public static void setActiveCharacter(long userID, PlayerCharacter character) {
		setActiveCharacter(userID, character.getID());
	}
	
	public static void setActiveCharacter(long userID, long characterID) {
		try {
			Table.updateWhere(PLAYERS, CURRENT_CHARACTER, characterID, new Comparison(Column.DISCORD_ID, EQUALS, userID));
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	/**
	 * Gets all characters that are owned by the specified owner.
	 * 
	 * This method deserializes of the characters, and will be very slow,
	 * it is highly recommended to use 'getCharacterDataByOwner' unless you need
	 * all of the player character objects.
	 * 
	 * @param owner the owner of the characters
	 * @return all of the characters owned by the user, may be an empty list
	 */
	@Deprecated
	public static ArrayList<PlayerCharacter> getAllCharactersByOwner(User owner) {
		return getAllCharactersByOwner(owner.getIdLong());
	}
	
	/**
	 * Gets all characters that are owned by the specified owner.
	 * 
	 * This method deserializes of the characters, and will be very slow,
	 * it is highly recommended to use 'getCharacterDataByOwner' unless you need
	 * all of the player character objects.
	 * 
	 * @param owner the owner id of the characters
	 * @return all of the characters owned by the user, may be an empty list
	 */
	@Deprecated
	public static ArrayList<PlayerCharacter> getAllCharactersByOwner(long ownerID) {
		ArrayList<PlayerCharacter> characters = new ArrayList<PlayerCharacter>();
		Result result = getAllCharacterDataByOwner(ownerID);
		while(result.next()) {
			characters.add(PlayerCharacter.deserialize(result.getLong(CHARACTER_ID)));
		}
		return characters;
	}
	
	/**
	 * Gets a SQL Result that contains data of all characters owned by the specified user
	 * @param owner the owner of the characters
	 * @return a SQL Result that contains data of all characters owned by the specified user
	 * @deprecated Use of this method is discoruaged because the results are not paginated
	 */
	@Deprecated
	public static Result getAllCharacterDataByOwner(User owner) {
		return getAllCharacterDataByOwner(owner.getIdLong());
	}
	
	/**
	 * Gets a SQL Result that contains data of all characters owned by the specified user
	 * @param owner the owner of the characters
	 * @return a SQL Result that contains data of all characters owned by the specified user
	 * @deprecated Use of this method is discoruaged because the results are not paginated
	 */
	@Deprecated
	public static Result getAllCharacterDataByOwner(long ownerID) {
		return Table.selectColumnsFromWhere(Column.CHARACTER_ID, Table.CHARACTERS, new Comparison(DISCORD_ID, Comparator.EQUALS, ownerID));
	}
	
	/**
	 * Gets a SQL Result that contains data of all characters owned by the specified user
	 * within the specified pagination
	 * 
	 * @param owner the owner of the characters
	 * @pageSize the size of the pages in the pagination
	 * @page the page number
	 * @return a SQL Result that contains data of all characters owned by the specified user
	 */
	public static Result getCharacterDataByOwner(User owner, int pageSize, int page) {
		return getCharacterDataByOwner(owner.getIdLong(), pageSize, page);
	}

	/**
	 * Gets a SQL Result that contains data of all characters owned by the specified user
	 * within the specified pagination
	 * 
	 * @param owner the owner of the characters
	 * @pageSize the size of the pages in the pagination
	 * @page the page number
	 * @return a SQL Result that contains data of all characters owned by the specified user
	 */
	public static Result getCharacterDataByOwner(long ownerID, int pageSize, int page) {
		return getCharacterDataByOwner(ownerID, new Pagination(NAME, Order.ASC, pageSize, page));
	}
	
	/**
	 * Gets a SQL Result that contains data of all characters owned by the specified user
	 * within the specified pagination
	 * 
	 * @param owner the owner of the characters
	 * @pagination the pagination used to obtain the character data from the database
	 * @return a SQL Result that contains data of all characters owned by the specified user
	 */
	public static Result getCharacterDataByOwner(long ownerID, Pagination pagination) {
		return Table.selectColumnsFromWhere(Column.ALL_COLUMNS, Table.CHARACTERS, new Comparison(DISCORD_ID, Comparator.EQUALS, ownerID), pagination);
	}
}
