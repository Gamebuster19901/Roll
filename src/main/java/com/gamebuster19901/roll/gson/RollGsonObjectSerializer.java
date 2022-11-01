package com.gamebuster19901.roll.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RollGsonObjectSerializer<T> implements JsonSerializer<T> {
	
	private boolean shouldWriteType(T value) {
		Class clazz = value.getClass();
		if(clazz.isPrimitive() || clazz.isEnum() || clazz.isAssignableFrom(String.class) || clazz.isLocalClass() || clazz.isAnonymousClass() || clazz.isHidden()) {
			return false;
		}
		return true;
	}

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		Class srcType = src.getClass();
		if(shouldWriteType(src)) {
			JsonObject obj = new JsonObject();
			obj.addProperty("class", srcType.getCanonicalName()); //a java identifier cannot be named class
			obj.add("return", context.serialize(src)); //a java identifier cannot be named return
			return obj;
		}
		return context.serialize(src);
	}
	
}
