package com.gamebuster19901.roll.bot.game;

import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;

public interface Statted {

	public String getName();
	
	public default int getRawModifier(Ability ability) {
		return getModifier(GameLayer.CLASS_OR_BACKGROUND_FEATURE, ability);
	}
	
	public int getAbilityScore(Ability ability);
	
	public default int getModifier(Ability ability) {
		return getModifier(GameLayer.EFFECT, ability);
	}
	
	public int getModifier(GameLayer layer, Ability ability);
	
	public default ProficiencyLevel getProficiency(Ability ability) {
		return getProficiency(GameLayer.EFFECT, ability);
	}
	
	public ProficiencyLevel getProficiency(GameLayer layer, Ability ability);
	
	public int getModifier(GameLayer layer, Skill skill);
	
	public ProficiencyLevel getProficiency(Skill skill);
	
	public int getHP();
	
	public default int getMaxHP() {
		return getHP(GameLayer.EFFECT);
	}
	
	public int getHP(GameLayer layer);
	
	public default int getRawMaxHP() {
		return getHP(GameLayer.CLASS_OR_BACKGROUND_FEATURE);
	}
	
	public int getTempHP();
	
	public default int getBaseSpeed(MovementType movementType) {
		return getSpeed(GameLayer.CLASS_OR_BACKGROUND_FEATURE, movementType);
	}
	
	public default int getSpeed(MovementType movementType) {
		return getSpeed(GameLayer.EFFECT, movementType);
	}
	
	public int getSpeed(GameLayer effect, MovementType movementType);

	public default int getBaseAC() {
		return getAC(GameLayer.CLASS_OR_BACKGROUND_FEATURE);
	}
	
	public default int getBaseACIncludingArmor() {
		return getAC(GameLayer.EQUIPMENT);
	}
	
	public default int getAC() {
		return getAC(GameLayer.EFFECT);
	}
	
	public int getAC(GameLayer layer);
	
}
