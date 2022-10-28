package com.gamebuster19901.roll.bot.game.character;

import static com.gamebuster19901.roll.bot.game.MovementType.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.commons.collections4.multiset.HashMultiSet;

import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.bot.game.stat.Stats;
import com.google.common.collect.HashMultimap;

public class PlayerBuilder {
	
	protected PlayerCharacterStats stats;
	
	public PlayerBuilder(PlayerCharacterStats stats) {
		
	};
	
	public void setStat(GameLayer layer, Stat stat, StatValue<?> value) {
		
	}
	
	public void checkValid() {
		stats.validate();
	}
	
	public PlayerCharacter build() {
		checkValid();
		return new PlayerCharacter(stats);
	}
	
}
