package com.gamebuster19901.roll.gson;

import java.io.IOException;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSerializableTypeAdapterFactory implements TypeAdapterFactory {

	private static final Gson subGson = new Gson();
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		return new TypeAdapter<T>() {

			@Override
			public void write(JsonWriter out, T value) throws IOException {
				if(value instanceof GSerializable) {
					JsonElement e = gson.getDelegateAdapter(GSerializableTypeAdapterFactory.this, type).toJsonTree(value);
					
					JsonObject newObj = new JsonObject();
					
					if(e.isJsonObject()) { 
						JsonObject obj = e.getAsJsonObject();
						//we want the class to be listed first

						newObj.addProperty("class", ((GSerializable)value).getClassName());
						for(Entry<String, JsonElement> entry : obj.entrySet()) {
							newObj.add(entry.getKey(), entry.getValue());
						}
						
					}
					
					Streams.write(newObj, out);
					return;
				}
				JsonElement e = gson.getDelegateAdapter(GSerializableTypeAdapterFactory.this, type).toJsonTree(value);
				Streams.write(e, out);
				return;
			}

			@Override
			public T read(JsonReader in) throws IOException {
				try {
					JsonElement e = Streams.parse(in);
					if(e.isJsonNull()) {
						return null;
					}
					boolean overwritten = false;
					TypeToken<T> actualTypeToken = type;
					Class<T> clazz = (Class<T>) type.getRawType();
					System.out.println(clazz);
					if(e.isJsonObject()) {
						JsonObject obj = e.getAsJsonObject();
						if(obj.has("class")) {
							overwritten = true;
							clazz = (Class<T>) Class.forName(obj.get("class").getAsString());
							actualTypeToken = TypeToken.get(clazz);
						}
					}
					System.out.println(clazz + " " + overwritten + " " + e);
					try {
						return gson.getDelegateAdapter(GSerializableTypeAdapterFactory.this, actualTypeToken).fromJsonTree(e);
					}
					catch(JsonIOException e1) {
						e1.printStackTrace();
						return null;
					}
				}
				catch(Throwable t) {
					throw new IOException(t);
				}
			}
			
		};
	}


}
