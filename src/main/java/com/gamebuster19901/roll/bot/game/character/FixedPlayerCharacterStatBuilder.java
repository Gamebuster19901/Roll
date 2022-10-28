package com.gamebuster19901.roll.bot.game.character;

import com.gamebuster19901.roll.bot.game.stat.FixedStatBuilder;
import com.gamebuster19901.roll.bot.game.stat.GameLayer;
import com.gamebuster19901.roll.bot.game.stat.StatSource;
import com.gamebuster19901.roll.bot.game.stat.StatValue;

import net.dv8tion.jda.api.entities.User;

public class FixedPlayerCharacterStatBuilder extends FixedStatBuilder implements PlayerCharacterStats {

	public FixedPlayerCharacterStatBuilder(User user, long id, String name) {
		super(name);
		this.requiredStats.add(Stat.Owner);
		this.requiredStats.add(Stat.ID);
		this.addStat(new StatValue<Long>(Stat.Owner, StatSource.of(GameLayer.CHOSEN, "Owner DiscordID"), user.getIdLong()));
		this.addStat(new StatValue<Long>(Stat.ID, StatSource.of(GameLayer.DATABASE, "Character ID"), id));
	}

}
