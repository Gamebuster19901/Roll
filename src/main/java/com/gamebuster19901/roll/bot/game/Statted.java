package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;

public interface Statted {

	public String getName();
	
	public int getRaw(Ability ability);
	
	public int getAbilityScore(Ability ability);
	
	public int getModifier(Ability ability);
	
	public ProficiencyLevel getProficiency(Ability ability);
	
	public int getModifier(Skill skill);
	
	public ProficiencyLevel getProficiency(Skill skill);
	
	public int getHP();
	
	public int getMaxHP();
	
	public int getRawMaxHP();
	
	public int getTempHP();
	
	public int getSpeed(MovementType speedType);
	
	public int getAC();
	
}
