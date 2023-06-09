package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.coinage.CoinPurse;
import com.gamebuster19901.roll.bot.game.foreign.ForeignLocation;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.gamebuster19901.roll.util.URI;
import com.google.common.collect.ImmutableMap;

import net.dv8tion.jda.api.entities.User;

public interface PlayerCharacterStats extends Statted, CoinPurse {
	
	public default User getOwner() {
		return Main.discordBot.jda.retrieveUserById(getStat(Stat.Owner, long.class)).complete();
	}
	
	public default long getID() {
		return getStat(Stat.ID, long.class);
	}
	
	public default ForeignLocation getForeignLocation() {
		return getStat(Stat.ForeignLocation, ForeignLocation.class);
	}
	
	public default URI getForeignURI() {
		return getStat(Stat.URI, URI.class);
	}
	
	public default void setForeignLocation(ForeignLocation foreignLocation) {
		if(this.hasStat(Stat.ForeignLocation)) {
			throw new IllegalStateException(this.getName() + " (" + this.getID() + ") already has a foreign location!");
		}
		this.addStat(Stat.ForeignLocation, StatSource.DATABASE_SOURCE, foreignLocation);
	}
	
	public default void setForeignURI(URI foreignURI) {
		if(this.hasStat(Stat.URI)) {
			throw new IllegalStateException(this.getName() + " (" + this.getID() + ") already has a foreign URI");
		}
		this.addStat(Stat.URI, StatSource.DATABASE_SOURCE, foreignURI);
	}
	
	public default ImmutableMap<Stat, StatValue<?>> getStats() {
		return (ImmutableMap<Stat, StatValue<?>>) Statted.super.getStats();
	}
	
	@Override
	public ImmutableMap<Stat, StatValue<?>> getStats(GameLayer layer);

}
