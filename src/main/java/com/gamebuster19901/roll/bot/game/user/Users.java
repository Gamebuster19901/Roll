package com.gamebuster19901.roll.bot.game.user;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.EQUALS;
import static com.gamebuster19901.roll.bot.database.Table.PLAYERS;

import java.io.IOError;
import java.sql.SQLException;

import javax.annotation.Nullable;

import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Order;
import com.gamebuster19901.roll.bot.database.Pagination;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.session.SelfSession;
import com.gamebuster19901.roll.bot.game.session.Session;
import com.gamebuster19901.roll.bot.game.session.Sessions;

import net.dv8tion.jda.api.entities.User;

public final class Users {

	private Users() {
		throw new AssertionError();
	}
	
	public static Session getCampaignSession(User user) {
		return Sessions.getCampaignSession(user);
	}
	
	public static SelfSession getSelfSession(User user) {
		return Sessions.getSelfSession(user);
	}
	
	public static Session getSession(User user) {
		return Sessions.getSession(user);
	}
	
	/**
	 * @param user the user to get the active character of
	 * @return the character the user is actively controlling, may be null if the user is not controlling a character.
	 * The character also might not be owned by the user.
	 */
	@Nullable
	public static Statted getActiveCharacter(User user) {
		return getSession(user).getActiveCharacter(user);
	}
	
	/**
	 * @param user the id of the user to get the active character of
	 * @return the character the user is actively controlling, may be null if the user is not controlling a character.
	 * The character also might not be owned by the user.
	 */
	@Nullable
	@Deprecated
	public static Statted getActiveCharacterFromDB(long discordID) {
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
		return getSession(user).hasActiveCharacter(user);
	}
	
	public static void setActiveCharacter(User user, Statted character) {
		getSession(user).setActiveCharacter(user, character);
	}
	
	@Deprecated
	public static void setActiveCharacterInDB(User user, Statted character) {
		try {
			if(character != null) {
				Table.updateWhere(PLAYERS, CURRENT_CHARACTER, character.getID(), new Comparison(Column.DISCORD_ID, EQUALS, user.getIdLong()));
			}
			else {
				Table.updateWhere(PLAYERS, CURRENT_CHARACTER, null, new Comparison(Column.DISCORD_ID, EQUALS, user.getIdLong()));
			}
		} catch (SQLException e) {
			throw new IOError(e);
		}
		getSession(user).setActiveCharacter(user, character);
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
