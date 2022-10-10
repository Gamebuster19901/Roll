package com.gamebuster19901.excite.bot.game.character;

import com.gamebuster19901.excite.bot.game.SpeedType;
import com.gamebuster19901.excite.bot.game.Statted;
import com.gamebuster19901.excite.bot.game.stat.Ability;
import com.gamebuster19901.excite.bot.game.stat.Proficiency;
import com.gamebuster19901.excite.bot.game.stat.Skill;

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
	public Proficiency getProficiency(Ability ability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifier(Skill skill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Proficiency getProficiency(Skill skill) {
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
	public int getSpeed(SpeedType speedType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAC() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
