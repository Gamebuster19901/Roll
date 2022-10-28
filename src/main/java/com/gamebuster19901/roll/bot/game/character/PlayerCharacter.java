package com.gamebuster19901.roll.bot.game.character;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.bot.game.stat.Stats;
import com.gamebuster19901.roll.util.TriFunction;

import net.dv8tion.jda.api.entities.User;

public class PlayerCharacter implements Statted {

	protected final long id;
	protected final Stats stats;
	
	public PlayerCharacter(long id, String name, Stats stats) {
		this.id = id;
		this.stats = stats;
	}
	
	@Override
	public String getName() {
		return stats.getName();
	}
	
	public long getID() {
		return id;
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
	
	public User getOwner() {
		return getOwner(getID());
	}
	
	public static User getOwner(long id) {
		return Main.discordBot.jda.getUserById(Table.selectColumnsFromWhere(Column.DISCORD_ID, Table.CHARACTERS, new Comparison(Column.CHARACTER_ID, Comparator.EQUALS, id)).getLong(Column.CHARACTER_ID));
	}
	
	public static boolean exists(long id) {
		return Table.existsWhere(Table.CHARACTERS, new Comparison(Column.CHARACTER_ID, Comparator.EQUALS, id));
	}
	
}
