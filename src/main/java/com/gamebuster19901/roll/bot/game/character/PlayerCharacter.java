package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;

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
	
}
