package com.gamebuster19901.roll.bot.game.character;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PlayerCharacterSerializer implements JsonSerializer<PlayerCharacter> {

	@Override
	public JsonElement serialize(PlayerCharacter player, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		result.add("name", new JsonPrimitive(player.getName()));
		result.add("id", new JsonPrimitive(player.getID()));
		JsonArray baseStats = new JsonArray();
		JsonArray modifiers = new JsonArray();
		JsonArray overrides = new JsonArray();
		result.add("baseStats", new JsonArray());
		
		return result;
	}

}
