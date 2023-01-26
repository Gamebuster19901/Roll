package com.gamebuster19901.roll.bot.game;

import static com.gamebuster19901.roll.bot.game.stat.Ability.*;

import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.stat.Ability;

public enum SaveType implements Statistic {
		STRENGTH_SAVE(Strength),
		DEXTERITY_SAVE(Dexterity),
		CONSTITUTION_SAVE(Constitution),
		INTELLIGENCE_SAVE(Intelligence),
		WISDOM_SAVE(Wisdom),
		CHARISMA_SAVE(Charisma),
		DEATH_SAVE("Death")
	;

	final Ability ability;
	final String name;
	
	SaveType(Ability ability) {
		this.ability = ability;
		this.name = ability.name();
	}
	
	SaveType(String name) {
		this.name =  name;
		this.ability = null;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Stat getStat() {
		return new Stat(name + " Saving Throw", name);
	}
	
}
