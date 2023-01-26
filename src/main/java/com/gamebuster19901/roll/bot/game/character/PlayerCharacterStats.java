package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.coinage.CoinPurse;
import com.gamebuster19901.roll.bot.game.stat.StatValue;
import com.google.common.collect.ImmutableMap;

import net.dv8tion.jda.api.entities.User;

public interface PlayerCharacterStats extends Statted, CoinPurse {
	
	public default User getOwner() {
		return Main.discordBot.jda.retrieveUserById(getStat(Stat.Owner, long.class)).complete();
	}
	
	public default long getID() {
		return getStat(Stat.ID, long.class);
	}
	
	@Override
	public ImmutableMap<Stat, StatValue<?>> getStats();

}
