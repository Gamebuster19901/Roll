package com.gamebuster19901.roll;

import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class GsonInitalizer {
	static Gson initialize() {
		return new GsonBuilder()
				
		.registerTypeAdapter(PlayerCharacter.class, new PlayerCharacterSerializer())
				
		.create();
	}
}
