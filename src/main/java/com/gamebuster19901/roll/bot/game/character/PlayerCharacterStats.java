package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.stat.FixedStats;
import com.gamebuster19901.roll.bot.game.stat.StatBuilder;

import net.dv8tion.jda.api.entities.User;

public interface PlayerCharacterStats extends Statted, StatBuilder<FixedStats>{
	
	public default User getOwner() {
		return Main.discordBot.jda.retrieveUserById(getStat(Stat.Owner, long.class)).complete();
	}
	
	public default long getID() {
		return getStat(Stat.ID, long.class);
	}

}
