package com.gamebuster19901.roll.bot.command.embed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.AbstractMessageBuilder;

public class StatEmbedBuilder {

	private final Statted statted;
	private final AbstractMessageBuilder messageBuilder;
	private StatEmbedPage page;
	
	public StatEmbedBuilder(Statted statted, AbstractMessageBuilder messageBuilder) {
		this(statted, StatEmbedPage.Stats, messageBuilder);
	}

	public StatEmbedBuilder(Statted statted, StatEmbedPage page, AbstractMessageBuilder messageBuilder) {
		this.statted = statted;
		this.messageBuilder = messageBuilder;
		this.page = page;
	}
	
	public MessageEmbed getEmbed() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(statted.getName());
		setTitleAndDescription(builder, page);
		switch(page) {
			case Stats:
				buildStatPage(builder);
				break;
			case Actions:
				buildActionPage(builder);
				break;
			case Skills:
				break;
			case Backstory:
				break;
			case Featrues:
				break;
			case Inventory:
				break;
			case Spells:
				break;
			default:
				break;
		}

		return builder.build();
	}
	
	private void setPage(StatEmbedPage page) {
		this.page = page;
	}
	
	private void buildStatPage(EmbedBuilder builder) {
		for(Ability ability : Ability.values()) {
			builder.addField(ability.shortHand + statted.getProficiency(ability).getEmoji(),
					statted.getAbilityScore(ability) + "[" + modifierText(statted.getModifier(ability)) + "]" , true);
		}
		if(statted instanceof PlayerCharacter) {
			try {
				PlayerCharacter player = (PlayerCharacter) statted;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(player.getCharacterImage(), "png", baos);
				messageBuilder.setFiles(FileUpload.fromData(new ByteArrayInputStream(baos.toByteArray()), "portrait.png"));
				builder.setThumbnail("attachment://portrait.png");
			}
			catch(IOException e) {e.printStackTrace();}
		}
	}
	
	private void buildActionPage(EmbedBuilder builder) {
	}
	
	private void buildSkillPage(EmbedBuilder builder) {
		for(Skill skill : Skill.DEFAULT_SKILLS) {
			builder.addField(skill.getName() + statted.getProficiency(skill).getEmoji(),
					modifierText(statted.getStat(skill.getStat(), int.class)) , false);
		}
		MessageChannel channel;
		if(statted instanceof PlayerCharacter) {
			try {
				PlayerCharacter player = (PlayerCharacter) statted;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(player.getCharacterImage(), "png", baos);
				messageBuilder.setFiles(FileUpload.fromData(new ByteArrayInputStream(baos.toByteArray()), "portrait.png"));
				builder.setThumbnail("attachment://portrait.png");
			}
			catch(IOException e) {e.printStackTrace();}
		}
	}
	
	private void setTitleAndDescription(EmbedBuilder builder, StatEmbedPage page) {
		builder.setTitle(page.name());
		builder.setDescription("Male Example Race, Class 1 / Class 2 ");
	}
	
	public StatEmbedPage getPage() {
		return page;
	}
	
	private String modifierText(int modifier) {
		if(modifier > 0) {
			return "+" + modifier;
		}
		return "" + modifier;
	}
	
}
