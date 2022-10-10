package com.gamebuster19901.excite.bot.game;

import com.gamebuster19901.excite.bot.game.stat.Ability;
import com.gamebuster19901.excite.bot.game.stat.Proficiency;
import com.gamebuster19901.excite.bot.game.stat.Skill;

public interface Statted {

	public String getName();
	
	public int getRaw(Ability ability);
	
	public int getAbilityScore(Ability ability);
	
	public int getModifier(Ability ability);
	
	public Proficiency getProficiency(Ability ability);
	
	public int getModifier(Skill skill);
	
	public Proficiency getProficiency(Skill skill);
	
	public int getHP();
	
	public int getMaxHP();
	
	public int getRawMaxHP();
	
	public int getTempHP();
	
	public int getSpeed(SpeedType speedType);
	
	public int getAC();
	
}
