package com.gamebuster19901.roll.bot.game.character;

import static com.gamebuster19901.roll.bot.game.MovementType.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.gamebuster19901.roll.bot.game.stat.Ability;

public class PlayerBuilder {
	
	protected HashSet<Stat> requiredStats = new LinkedHashSet<>();
	{
		requiredStats.add(Stat.HP);
		requiredStats.add(Stat.Max_HP);
		requiredStats.add(Stat.AC);
		Walking.getStat();
		for(Ability ability : Ability.values()) {
			requiredStats.add(ability.getStat());
			requiredStats.add(ability.getProficiencyStat());
		}
	}
	
	protected HashMap<Stat, Object> defaultStats = new LinkedHashMap<>();
	{
		defaultStats.put(Flying.getStat(), 0);
		defaultStats.put(Burrowing.getStat(), 0);
		defaultStats.put(Climbing.getStat(), 0);
		defaultStats.put(Swimming.getStat(), 0);
	}

	protected String name;
	protected long id;
	protected HashMap<Stat, Object> baseStats = new LinkedHashMap<>();
	protected HashMap<Stat, Object> modifiers = new LinkedHashMap<>();
	protected HashMap<Stat, Object> overrides = new LinkedHashMap<>();
	
	public PlayerBuilder() {};
	
	protected PlayerBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	protected PlayerBuilder setID(long id) {
		this.id = id;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public long getID() {
		return id;
	}
	
	public void set(Stat stat, Object value) {
		baseStats.put(stat, value);
	}
	
	public void setDefault(Stat stat, Object value) {
		defaultStats.put(stat, value);
	}
	
	public void override(Stat stat, Object value) {
		overrides.put(stat, value);
	}
	
	public Object get(Stat stat) {
		if(overrides.containsKey(stat)) {
			return overrides.get(stat);
		}
		if(baseStats.containsKey(stat)) {
			return baseStats.get(stat);
		}
		if(defaultStats.containsKey(stat)) {
			return defaultStats.get(stat);
		}
		return null;
	}
	
	public void checkValid() {
		if(name == null || name.isBlank()) {
			throw new IllegalStateException("Invalid character - No name");
		}
		if(id < 1) {
			throw new IllegalStateException("Invalid character - No character ID");
		}
		for(Stat stat : requiredStats) {
			if(get(stat) == null) {
				throw new IllegalStateException("Invalid character - Missing `" + stat.getName() + "` stat.");
			}
		}
	}
	
	public PlayerCharacter build() {
		checkValid();
		return new PlayerCharacter(name);
	}
	
}
