package com.gamebuster19901.roll.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class RollGsonObjectDesrializer<T> implements JsonDeserializer<T> {

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if(json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			if(obj.has("class")) {
				try {
					return context.deserialize(obj.get("return"), Class.forName(obj.get("class").getAsString()));
				} catch (ClassNotFoundException e) {
					throw new JsonParseException(e);
				}
			}
		}
		return context.deserialize(json, typeOfT);
	}

}
