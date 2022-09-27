package com.gamebuster19901.excite.bot.game;

import static com.gamebuster19901.excite.bot.game.Ability.*;

public enum Save implements Roll {
	Strength_Save(Strength),
	Dexterity_Save(Dexterity),
	Constitution_Save(Constitution),
	Intelligence_Save(Intelligence),
	Wisdom_save(Wisdom),
	Charisma_Save(Charisma),
	Death_Save("Death");
	
	private final Ability ability;
	private final String shortName;
	
	private Save(Ability ability) {
		this.ability = ability;
		this.shortName = null;
	}
	
	private Save(String shortName) {
		this.ability = null;
		this.shortName = shortName;
	}
	
	@Override
	public String getName() {
		return name().replace('_', ' ');
	}

	@Override
	public String shortName() {
		if(ability != null) {
			return ability.shortName() + " Save";
		}
		return shortName + " Save";
	}

}
