package com.gamebuster19901.roll;

import java.lang.reflect.Type;

import com.ezylang.evalex.Expression;
import com.gamebuster19901.roll.gson.GSerializableTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class GsonInitalizer {
	static Gson initialize() {
		return new GsonBuilder()
		.setPrettyPrinting()
		.registerTypeAdapterFactory(new GSerializableTypeAdapterFactory())
		.enableComplexMapKeySerialization()
		.create();
	}
	
	private static final class ExpressionSerializer implements JsonSerializer<Expression> {

		@Override
		public JsonElement serialize(Expression src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.getExpressionString());
		}
		
	}
	
	private static final class ExpressionDeserializer implements JsonDeserializer<Expression> {
		@Override
		public Expression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return new Expression(json.getAsString());
		}
		
	}
}
