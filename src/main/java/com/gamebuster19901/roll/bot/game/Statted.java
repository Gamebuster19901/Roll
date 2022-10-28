package com.gamebuster19901.roll.bot.game;

import javax.annotation.CheckForNull;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.TriFunction;

public interface Statted {

	public String getName();
	
	public default int getRawModifier(Ability ability) {
		return getModifier(GameLayer.CLASS_OR_BACKGROUND_FEATURE, ability);
	}
	
	public default int getAbilityScore(Ability ability) {
		return getAbilityScore(GameLayer.EFFECT, ability);
	}
	
	public default int getAbilityScore(GameLayer layer, Ability ability) {
		return getStat(layer, ability.getStat(), int.class);
	}
	
	public default int getModifier(Ability ability) {
		return getModifier(GameLayer.EFFECT, ability);
	}
	
	public default int getModifier(GameLayer layer, Ability ability) {
		return getStat(layer, ability.getModStat(), int.class);
	}
	
	public default ProficiencyLevel getProficiency(Ability ability) {
		return getProficiency(GameLayer.EFFECT, ability);
	}
	
	public default ProficiencyLevel getProficiency(GameLayer layer, Ability ability) {
		return getStat(layer, ability.getProficiencyStat(), ProficiencyLevel.class);
	}
	
	public default int getModifier(GameLayer layer, Skill skill) {
		return getStat(layer, skill.getStat(), int.class);
	}
	
	public default ProficiencyLevel getProficiency(Skill skill) {
		return getProficiency(GameLayer.EFFECT, skill);
	}
	
	public default ProficiencyLevel getProficiency(GameLayer layer, Skill skill) {
		return getStat(layer, skill.getProficiencyStat(), ProficiencyLevel.class);
	}
	
	public default int getHP() {
		return getHP(GameLayer.EFFECT);
	}
	
	public default int getHP(GameLayer layer) {
		return getStat(layer, Stat.HP, int.class);
	}
	
	public default int getMaxHP() {
		return getMaxHP(GameLayer.EFFECT);
	}
	
	public default int getMaxHP(GameLayer layer) {
		return getStat(layer, Stat.Max_HP, int.class);
	}
	
	public default int getRawMaxHP() {
		return getMaxHP(GameLayer.CLASS_OR_BACKGROUND_FEATURE);
	}
	
	public default int getTempHP() {
		return getTempHP(GameLayer.EFFECT);
	}
	
	public default int getTempHP(GameLayer layer) {
		return getStat(layer, Stat.Temp_HP, int.class);
	}
	
	public default int getBaseSpeed(MovementType movementType) {
		return getSpeed(GameLayer.CLASS_OR_BACKGROUND_FEATURE, movementType);
	}
	
	public default int getSpeed(MovementType movementType) {
		return getSpeed(GameLayer.EFFECT, movementType);
	}
	
	public default int getSpeed(GameLayer layer, MovementType movementType) {
		return getStat(layer, movementType.getStat(), int.class);
	}

	public default int getBaseAC() {
		return getAC(GameLayer.CLASS_OR_BACKGROUND_FEATURE);
	}
	
	public default int getBaseACIncludingArmor() {
		return getAC(GameLayer.EQUIPMENT);
	}
	
	public default int getAC() {
		return getAC(GameLayer.EFFECT);
	}
	
	public default int getAC(GameLayer layer) {
		return getStat(layer, Stat.AC, int.class);
	}
	
	@CheckForNull
	public default <T> T getStat(Stat stat, Class<T> type) {
		return getStat(GameLayer.EFFECT, stat, type);
	}
	
	public <T> T getStat(GameLayer layer, Stat stat, Class<T> type);
	
	public default void addStat(StatValue<?> value) {
		addStat(value, getOverwriteFunction(value));
	}
	
	/**
	 * 
	 * @param layer the layer to add the stat to
	 * @param stat
	 * @param value
	 * @param func a function that determines if a statValue should be removed before the current stat value is added
	 */
	public void addStat(StatValue<?> value, TriFunction<GameLayer, Stat, StatValue<?>, Boolean> func);
	
	public default void setVariables(Expression expression) {
		setVariables(GameLayer.VULNERABILITY, expression);
	}
	
	public void setVariables(GameLayer layer, Expression expression);
	
	private TriFunction<GameLayer, Stat, StatValue<?>, Boolean> getOverwriteFunction(StatValue<?> value) {
		if(value.getStat().equals(Stat.AC)) { //can have multiple AC values set on the same layer
			if(GameLayer.EFFECT.compareTo(value.getGameLayer()) >= 0) {
				return (oldLayer, oldStat, oldValue) -> {return false;};
			}
		}
		return (oldLayer, oldStat, oldValue) -> {
			if(value.getGameLayer() == oldLayer) {
				if(oldStat.equals(value.getStat())) {
					if(value.getStatSource().equals(oldValue.getStatSource())) {
						return true;
					}
				}
			}
			return false;
		};
	}
}
