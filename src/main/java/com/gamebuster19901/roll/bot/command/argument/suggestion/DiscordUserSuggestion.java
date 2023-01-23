package com.gamebuster19901.roll.bot.command.argument.suggestion;

import java.util.Objects;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class DiscordUserSuggestion extends Suggestion {

	private final IMentionable value;
	
	public DiscordUserSuggestion(SuggestionsBuilder builder, final User value) {
		this(StringRange.between(builder.getStart(), builder.getInput().length()), value);
	}
	
	public DiscordUserSuggestion(SuggestionsBuilder builder, final Member value) {
		this(StringRange.between(builder.getStart(), builder.getInput().length()), value);
	}
	
	public DiscordUserSuggestion(final StringRange range, final User value) {
		this(range, value, new LiteralMessage(value.getAsTag()));
	}
	
	public DiscordUserSuggestion(final StringRange range, final Member value) {
		this(range, value, new LiteralMessage(value.getEffectiveName() + "#" + value.getUser().getDiscriminator()));
	}
	
	private DiscordUserSuggestion(final StringRange range, final IMentionable value, final Message tooltip) {
		super(range, value.getAsMention(), tooltip);
		this.value = value;
	}
	
	public IMentionable getValue() {
		return value;
	}
	
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if(!(o instanceof DiscordUserSuggestion)) {
			return false;
		}
		final DiscordUserSuggestion other = (DiscordUserSuggestion)o;
		return value == other.value && super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), value);
	}
	
	@Override
	public String toString() {
		return "DiscordUserSuggestion{" +
			"type=" + value.getClass().getCanonicalName() +
			", value='" + value + + '\'' +
			", range=" + getRange() +
			", text='" + getText() + '\'' +
			", tooltip='" + getTooltip() + '\'' +
		"}";
		
	}
	
	@Override
	public int compareTo(final Suggestion o) {
		if(o instanceof DiscordUserSuggestion) {
			return Long.compare(value.getIdLong(), ((DiscordUserSuggestion) o).value.getIdLong());
		}
		return super.compareTo(o);
	}
	
	@Override
	public int compareToIgnoreCase(final Suggestion b) {
		return compareTo(b);
	}
}
