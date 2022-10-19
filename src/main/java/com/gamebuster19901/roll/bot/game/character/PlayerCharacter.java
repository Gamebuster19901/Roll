package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;

import net.dv8tion.jda.api.entities.User;

public class PlayerCharacter implements Statted {

	private String name;
	
	public PlayerCharacter(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRaw(Ability ability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAbilityScore(Ability ability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getModifier(Ability ability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ProficiencyLevel getProficiency(Ability ability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifier(Skill skill) {
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
	public int getMaxHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRawMaxHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTempHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpeed(MovementType speedType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAC() {
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
