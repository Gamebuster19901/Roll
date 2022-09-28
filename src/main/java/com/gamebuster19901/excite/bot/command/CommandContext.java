package com.gamebuster19901.excite.bot.command;

import java.time.Instant;

import com.gamebuster19901.excite.util.MessageUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandContext<E>{
	
	private E event;
	private EmbedBuilder embedBuilder;
	
	public CommandContext(E e) {
		if(e instanceof MessageReceivedEvent || e instanceof SlashCommandInteractionEvent) {
			this.event = e;
		}
		else {
			throw new IllegalArgumentException(e.getClass().getCanonicalName());
		}
	}
	
	public CommandContext(E e, String message) {
		this(e);
		long id = 0;
		if(e instanceof MessageReceivedEvent) {
			id = ((MessageReceivedEvent) e).getMessageIdLong();
		}

	}
	
	public User getAuthor() {
		if(event instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) event).getAuthor();
		}
		else if (event instanceof SlashCommandInteractionEvent) {
			return ((SlashCommandInteractionEvent) event).getUser();
		}
		throw new AssertionError();
	}
	
	@SuppressWarnings("unchecked")
	public CommandContext() {
		this.event = null;
	}
	
	public boolean isDiscordContext() {
		return event instanceof ISnowflake;
	}
	
	public void sendMessage(String message) {
		if(isDiscordContext()) {
			if(event instanceof MessageReceivedEvent) {
				for(String submessage : MessageUtil.toMessages(message)) {
					((MessageReceivedEvent)event).getChannel().sendMessage(submessage).complete();
				}
			}
			else if (event instanceof SlashCommandInteractionEvent) {
				for(String submessage : MessageUtil.toMessages(message)) {
					((SlashCommandInteractionEvent) event).reply(submessage).complete();
				}
			}
		}
		else {
			throw new AssertionError("Unknown Command context: " + event.getClass().getCanonicalName());
		}
	}
	
	public void sendMessage(EmbedBuilder embed) {
		if(isDiscordContext()) {
			if(event instanceof MessageReceivedEvent) {
				((MessageReceivedEvent)event).getChannel().sendMessageEmbeds(embed.build()).complete();
			}
			else if (event instanceof SlashCommandInteractionEvent) {
				((SlashCommandInteractionEvent) event).replyEmbeds((embed.build())).complete();
			}
		}
		else {
			throw new AssertionError("Unknown Command context: " + event.getClass().getCanonicalName());
		}
	}
	
	public EmbedBuilder constructEmbedResponse(String command) {
		return constructEmbedResponse(command, null);
	}
	
	public EmbedBuilder constructEmbedResponse(String command, String title) {
		User user = getAuthor();
		embedBuilder = new EmbedBuilder();
		embedBuilder.setAuthor(user.getAsTag(), null, user.getAvatarUrl());
		embedBuilder.setTimestamp(Instant.now());
		return embedBuilder;
	}
	
	public MessageChannel getChannel() {
		if(event instanceof SlashCommandInteractionEvent) {
			return ((SlashCommandInteractionEvent) event).getMessageChannel();
		}
		return null;
	}
	
	public EmbedBuilder getEmbed() {
		return embedBuilder;
	}

}
