package com.gamebuster19901.excite.bot.game;

import static com.gamebuster19901.excite.bot.game.stat.Ability.*;

import com.gamebuster19901.excite.bot.game.stat.Ability;

public enum SaveType {
		STRENGTH_SAVE(STRENGTH),
		DEXTERITY_SAVE(DEXTERITY),
		CONSTITUTION_SAVE(DEXTERITY),
		INTELLIGENCE_SAVE(INTELLIGENCE),
		WISDOM_SAVE(WISDOM),
		CHARISMA_SAVE(CHARISMA),
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
	
}
