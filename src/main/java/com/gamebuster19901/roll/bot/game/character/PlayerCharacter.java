package com.gamebuster19901.roll.bot.game.character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.Main;
import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.database.Table;
import static com.gamebuster19901.roll.bot.database.Table.CHARACTERS;
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
	
}
