package com.gamebuster19901.excite.bot.game.character;

import java.util.ArrayList;
import java.util.List;

import com.gamebuster19901.excite.bot.game.stat.Ability;
import com.gamebuster19901.excite.bot.game.stat.Skill;

public class PlayerBuilder {
	
	public Object[] requiredStats = new Object[] {
		"HP",
		"Max HP"
	};
	
	public Stat[] defaultStats = new Stat[] {
		new Stat<String>("Temp HP", 0)
	};

	String name;
	ArrayList<Stat> stats = new ArrayList<Stat>();
	ArrayList<Stat> modifiers = new ArrayList<Stat>();
	ArrayList<Stat> overrides = new ArrayList<Stat>();
	
	public PlayerBuilder() {};
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setHP(int hp) {
		stats.add(new Stat<String>("HP", hp));
	}
	
	public void setMaxHP(int maxHP) {
		stats.add(new Stat<String>("Max HP", maxHP));
	}
	
	public void overrideMaxHP(int maxHP) {
		overrides.add(new Stat<String>("Max HP", maxHP));
	}
	
	public void setTempHP(int tempHP) {
		stats.add(new Stat<String>("Temp HP", tempHP));
	}
	
	public void set(Ability ability, int score) {
		stats.add(new Stat<Ability>(ability, score));
	}
	
	public void setModifiers(Ability ability, int modifier) {
		modifiers.add(new Stat<Ability>(ability, modifier));
	}
	
	public void override(Ability ability, int score) {
		overrides.add(new Stat<Ability>(ability, score));
	}
	
	public void set(Skill skill, int score) {
		stats.add(new Stat<Skill>(skill, score));
	}
	
	public List<Stat> getStats() {
		return stats;
	}
	
	public List<Stat> getModifiers() {
		return modifiers;
	}
	
	public List<Stat> getOverrides() {
		return overrides;
	}
	
	public boolean isValid() {
		if(name != null && name.isBlank() == false) {
			return true;
		}
		return false;
	}
	
	public PlayerCharacter build() {
		return new PlayerCharacter();
	}
	
}
