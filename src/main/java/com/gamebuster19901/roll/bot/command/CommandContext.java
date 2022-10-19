package com.gamebuster19901.roll.bot.command;

import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class CommandContext<E> {
	
	private E event;
	private EmbedBuilder embedBuilder;
	
	public CommandContext(E e) {
		if(e instanceof MessageReceivedEvent || e instanceof SlashCommandInteractionEvent || e instanceof ModalInteractionEvent || e instanceof ButtonInteractionEvent) {
			this.event = e;
		}
		else {
			throw new IllegalArgumentException(e.getClass().getCanonicalName());
		}
	}

	public User getAuthor() {
		if(event instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) event).getAuthor();
		}
		else if (event instanceof Interaction) {
			return ((Interaction) event).getUser();
		}
		throw new AssertionError();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getEvent(Class<T> type) throws ClassCastException {	
		if(type.isAssignableFrom(event.getClass())) {
			return (T)event;
		}
		throw new ClassCastException(event + " cannot be cast to " + type.getClass());
	}
	
	public boolean isDiscordContext() {
		return event instanceof ISnowflake;
	}
	
	public void replyMessage(MessageCreateData messageData) {
		if(event instanceof IReplyCallback) {
			((IReplyCallback) event).reply(messageData).complete();
		}
		else if(event instanceof MessageReceivedEvent) {
			((MessageReceivedEvent) event).getChannel().sendMessage(messageData);
		}
		else {
			throw new UnsupportedOperationException("Cannot reply to a " + event.getClass().getCanonicalName());
		}
	}
	
	public void editMessage(MessageEditData messageEdit) {
		if(event instanceof IMessageEditCallback) {
			((IMessageEditCallback) event).editMessage(messageEdit);
		}
		else {
			replyMessage(MessageCreateData.fromEditData(messageEdit));
		}
	}
	
	public void editMessage(String message) {
		editMessage(new MessageEditBuilder().closeFiles().clear().setContent(message).build());
	}
	
	public void editMessage(String message, boolean clear) {
		if(!clear) {
			editMessage(new MessageEditBuilder().setContent(message).build());
		}
		else {
			editMessage(message);
		}
	}
	
	public void sendMessage(String message) {
		replyMessage(new MessageCreateBuilder().setContent(message).build());
	}
	
	public void sendMessage(EmbedBuilder embed) {
		replyMessage(new MessageCreateBuilder().setEmbeds(embed.build()).build());
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
		if(event instanceof Interaction) {
			return ((Interaction) event).getMessageChannel();
		}
		else if (event instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) event).getChannel();
		}
		return null;
	}
	
	public EmbedBuilder getEmbed() {
		return embedBuilder;
	}

}
