package com.gamebuster19901.roll.bot.command.argument;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.exception.ParseExceptions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class DiscordUserArgumentType implements ArgumentType<User>{
	
	@Override
	public <S> User parse(S context, StringReader reader) throws CommandSyntaxException {
		String s = Commands.readString(reader);
		long id;
			if(s.length() > 3) {
				if(s.startsWith("<@")) {
					id = Long.parseLong(s.substring(2, s.length() - 1));
					return Main.discordBot.jda.retrieveUserById(id).complete();
				}
			}
		throw ParseExceptions.NONEXISTANT.create("Discord User", s);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(com.mojang.brigadier.context.CommandContext<S> context, SuggestionsBuilder builder) {
		CommandContext<?> c = (CommandContext<?>) context.getSource();
		Guild server = c.getServer();
		System.err.println(Commands.lastArgOf(builder.getInput()));
		if(server != null) {
			server.findMembers((member) -> {
				return (member.getEffectiveName().toLowerCase() + "#" + member.getUser().getDiscriminator()).startsWith(Commands.lastArgOf(builder.getInput().toLowerCase()));
			}).onSuccess((foundMembers) -> {
				for(Member member : foundMembers) {
					builder.suggest(member.getAsMention());
				}
			});
		}
		return builder.buildFuture();
	}
	
}
