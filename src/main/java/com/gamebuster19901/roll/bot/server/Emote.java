package com.gamebuster19901.roll.bot.server;

import com.gamebuster19901.roll.Main;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Emote {

	
	public static String getEmoji(String name) {
		String ret = "";
		for(Emoji emoji : Main.discordBot.jda.getEmojisByName(name, false)) {
			String s = emoji.getFormatted();
			System.err.println(s);
			return s;
		}
		return ret;
	}
	
}