package com.gamebuster19901.roll.bot.game.character;

import java.util.HashMap;
import java.util.LinkedHashMap;

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

import net.dv8tion.jda.api.entities.User;

public class PlayerCharacter implements Statted {

	private String name;
	protected long id;
	protected HashMap<Stat, StatValue<?>> baseStats = new LinkedHashMap<>();
	protected HashMap<Stat, StatValue<?>> modifiers = new LinkedHashMap<>();
	protected HashMap<Stat, StatValue<?>> overrides = new LinkedHashMap<>();
	
	public PlayerCharacter(String name, HashMap<Stat, StatValue<?>> baseStats) {
		this.name = name;
		this.baseStats = baseStats;
	}
	
	public PlayerCharacter(String name, HashMap<Stat, StatValue<?>> baseStats, HashMap<Stat, StatValue<?>> modifiers, HashMap<Stat, StatValue<?>> overrides) {
		this.name = name;
		this.baseStats = baseStats;
		this.modifiers = modifiers;
		this.overrides = overrides;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public long getID() {
		return id;
	}

	@Override
	public int getAbilityScore(Ability ability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getModifier(GameLayer layer, Ability ability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ProficiencyLevel getProficiency(GameLayer layer, Ability ability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifier(GameLayer layer, Skill skill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ProficiencyLevel getProficiency(Skill skill) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHP(GameLayer layer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTempHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpeed(GameLayer effect, MovementType movementType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAC(GameLayer layer) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static User getOwner(long id) {
		return Main.discordBot.jda.getUserById(Table.selectColumnsFromWhere(Column.DISCORD_ID, Table.CHARACTERS, new Comparison(Column.CHARACTER_ID, Comparator.EQUALS, id)).getLong(Column.CHARACTER_ID));
	}
	
	public static boolean exists(long id) {
		return Table.existsWhere(Table.CHARACTERS, new Comparison(Column.CHARACTER_ID, Comparator.EQUALS, id));
	}
	
}
