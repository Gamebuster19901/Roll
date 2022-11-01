package com.gamebuster19901.roll.gson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MetamorphicTypeAdapterFactory implements TypeAdapterFactory {
	
	private final HashSet<Field> fields = new HashSet<Field>();
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		
		return new TypeAdapter<T>() {
			@Override
			public void write(JsonWriter out, T value) throws IOException {
				if(value != null && isMetamorphic(value.getClass())) {
					out.beginObject();
					out.name("class"); //a java identifier cannot be named 'class'
					out.value(value.getClass().getCanonicalName());
					out.name("return"); // a java identifier cannot be named 'return'
					gson.getDelegateAdapter(MetamorphicTypeAdapterFactory.this, type).write(out, value);
					out.endObject();
					return;
				}
				gson.getDelegateAdapter(MetamorphicTypeAdapterFactory.this, type).write(out, value);
			}

			@Override
			public T read(JsonReader in) throws IOException {
				JsonElement json = JsonParser.parseReader(in);
				if(json.isJsonObject()) {
					JsonObject obj = (JsonObject) json;
					if(obj.has("class")) {
						return gson.getDelegateAdapter(MetamorphicTypeAdapterFactory.this, type).fromJsonTree(obj.get("return"));
					}
				}
				return gson.getDelegateAdapter(MetamorphicTypeAdapterFactory.this, type).fromJsonTree(json);
			}
			
		};
	}

	private HashSet<Field> getFields(Class<?> rawType) {
		HashSet<Field> fields = new HashSet<>();
		System.out.println(rawType);
		Class<?> raw = rawType;
		while(raw != null) {
			for(Field f : raw.getDeclaredFields()) {
				int m = f.getModifiers();
				if(!(Modifier.isStatic(m) || Modifier.isTransient(m))) {
					fields.add(f);
				}
			}
			raw = raw.getSuperclass();
		}
		return fields;
	}
	
	private boolean isMetamorphic(Field field) {
		return field.isAnnotationPresent(Metamorphic.class) || isMetamorphic(field.getType());
	}
	
	@SuppressWarnings("unchecked")
	private boolean isMetamorphic(Class<?> clazz) {
		if (Metamorphic.class.isAssignableFrom(clazz)) {
			return true;
		}
		do {
			if(clazz.isAnnotationPresent(Metamorphic.class)) {
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		while(clazz.getSuperclass() != null);
		return false;
	}
	
}
