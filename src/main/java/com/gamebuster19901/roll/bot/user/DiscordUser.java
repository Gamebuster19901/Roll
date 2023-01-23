package com.gamebuster19901.roll.bot.user;

import java.sql.SQLException;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.audit.RankChangeAudit;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Column;
import com.gamebuster19901.roll.bot.database.Comparator;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Table;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

public class DiscordUser {

	public static String getDetailedString(User user) {
		return user.getAsTag() + "(" + user.getIdLong() + ")";
	}
	
	public static boolean isOperator(User user) {
		if(user != null) {
			return user == ConsoleUser.INSTANCE || Table.existsWhere(Table.OPERATORS, new Comparison(Column.DISCORD_ID, Comparator.EQUALS, user.getIdLong()));
		}
		return false;
	}
	
	public static boolean addOperator(CommandContext promoter, User user) throws SQLException {
		RankChangeAudit.addRankChange(promoter, user, "operator", true);
		PrivateChannel channel = user.openPrivateChannel().complete();
		channel.sendMessage(promoter.getAuthor().getAsMention() + " has promoted you. You are now a global operator of " + Main.discordBot.getSelfUser().getAsMention() + ".").queue();;
		return Insertion.insertInto(Table.OPERATORS).setColumns(Column.DISCORD_ID).to(user.getIdLong()).prepare(true).execute();
	}
	
	public static void removeOperator(CommandContext promoter, User user) throws SQLException {
		RankChangeAudit.addRankChange(promoter, user, "operator", false);
		PrivateChannel channel = user.openPrivateChannel().complete();
		channel.sendMessage(promoter.getAuthor().getAsMention() + " has demoted you. You are no longer a global operator of " + Main.discordBot.getSelfUser().getAsMention() + ".").queue();;
		Table.deleteWhere(Table.OPERATORS, new Comparison(Column.DISCORD_ID, Comparator.EQUALS, user.getIdLong()));
	}
	
}
