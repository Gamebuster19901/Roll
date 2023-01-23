package com.gamebuster19901.roll.bot.command;

import java.sql.SQLException;

import com.gamebuster19901.roll.bot.command.argument.DiscordUserArgumentType;
import com.gamebuster19901.roll.bot.user.DiscordUser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.dv8tion.jda.api.entities.User;

public class RankCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("rank")
				.then(Commands.sub("add")
						.then(Commands.argument("rank", StringArgumentType.word()).suggests((context, builder) -> {return builder.buildFuture();})
								.then(Commands.argument("user", new DiscordUserArgumentType())
									.executes(context -> {
										try {
											return addRank(context.getSource(), context.getArgument("user", User.class), context.getArgument("rank", String.class));
										} catch (SQLException e) {
											return 1;
										}
									})
								)
						)
				).then(Commands.sub("remove")
						.then(Commands.argument("rank", StringArgumentType.word()).suggests((context, builder) -> {return builder.buildFuture();})
								.then(Commands.argument("user", new DiscordUserArgumentType())
									.executes(context -> {
										try {
											return removeRank(context.getSource(), context.getArgument("user", User.class), context.getArgument("rank", String.class));
										} catch (SQLException e) {
											return 1;
										}
									})
								)
						)
				).then(Commands.sub("test").then(Commands.literal("test").then(Commands.literal("test").then(Commands.literal("test")))))
		);
	}
	
	@SuppressWarnings("rawtypes")
	private static int addRank(CommandContext context, User user, String rank) throws SQLException {
		if(context.isOperator()) {
			if (rank.equalsIgnoreCase("operator") || rank.equalsIgnoreCase("op")) {
				return addOperator(context, user);
			}
		}
		else {
			context.sendMessage("You do not have permission to execute that command.");
		}
		return 1;
	}
	
	@SuppressWarnings("rawtypes")
	private static int removeRank(CommandContext context, User user, String rank) throws SQLException {
		if(context.isOperator()) {
			if (rank.equalsIgnoreCase("operator") || rank.equalsIgnoreCase("op")) {
				return removeOperator(context, user);
			}
		}
		else {
			context.sendMessage("You do not have permission to execute that command.");
		}
		return 1;
	}
	
	@SuppressWarnings("rawtypes")
	private static int addOperator(CommandContext context, User user) throws SQLException {
		
		if(DiscordUser.isOperator(user)) {
			context.sendMessage(user + " is already a bot operator");
		}
		else {
			DiscordUser.addOperator(context, user);
		}

		return 1;
	}
	
	@SuppressWarnings("rawtypes")
	private static int removeOperator(CommandContext context, User user) throws SQLException {
		if(!DiscordUser.isOperator(user)) {
			context.sendMessage(user + " is already not a bot operator");
		}
		else {
			DiscordUser.removeOperator(context, user);
		}
		return 1;
	}
	
}