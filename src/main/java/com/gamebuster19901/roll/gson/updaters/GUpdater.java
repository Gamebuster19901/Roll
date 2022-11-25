package com.gamebuster19901.roll.gson.updaters;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class GUpdater implements Comparable<Number>{
	
	public static final TreeMap<Integer, HashSet<GUpdater>> updaters = new TreeMap<>();
	
	static {
		add(new V2());
		add(new V3());
	}
	
	private final int version;
	protected final HashSet<String> clazzes = new HashSet<String>();
	
	public GUpdater(int version, Class...classes) {
		this.version = version;
		for(Class<?> clazz : classes) {
			clazzes.add(clazz.getTypeName());
		}
	}
	
	public GUpdater(int version, String...classes) { //for types that do not exist anymore
		this.version = version;
		for(String clazz : classes) {
			clazzes.add(clazz);
		}
	}
	
	protected abstract void updateImpl(JsonElement json);
	
	public boolean accepts(String clazz) {
		return clazzes.contains(clazz);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof GUpdater) {
			return version == ((GUpdater) o).version;
		}
		return false;
	}
	
	@Override
	public int compareTo(Number n) {
		return version - n.intValue();
	}
	
	public static void update(JsonElement json, Type clazz) {
		update(json, clazz.getTypeName());
	}
	
	public static void update(JsonElement json, String clazz) {
		if(json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			int version = 0;
			if(obj.has("implements")) {
				version = obj.get("implements").getAsInt();
			}
			
			SortedMap<Integer, HashSet<GUpdater>> applicableUpdaters = updaters.tailMap(version);
			
			for(HashSet<GUpdater>  updaters : applicableUpdaters.values()) {
				for(GUpdater updater : updaters) {
					if(updater.accepts(clazz)) {
						updater.updateImpl(json);
					}
					else {
						//System.out.println("Updater does not accept " + clazz);
					}
				}
			}
		}
	}
	
	private static void add(Updaters updaters) {
		if(!GUpdater.updaters.containsKey(updaters.version())) {
			GUpdater.updaters.put(updaters.version(), new HashSet<>());
		}
		GUpdater.updaters.get(updaters.version()).addAll(Arrays.asList(updaters.getUpdaters()));
	}
	
}
