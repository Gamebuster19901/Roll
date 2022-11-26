package com.gamebuster19901.roll.bot.command.embed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import com.gamebuster19901.roll.bot.game.MovementType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.AbstractMessageBuilder;

public class StatEmbedBuilder {
	
	private final Statted statted;
	private final AbstractMessageBuilder messageBuilder;
	private StatEmbedPage page;
	private HashSet<Stat> hiddenStats = new HashSet<Stat>(); //for future use
	
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
				buildSkillPage(builder);
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
		LinkedHashSet<Field> left = new LinkedHashSet<Field>();
		LinkedHashSet<Field> middle = new LinkedHashSet<Field>();
		LinkedHashSet<Field> right = new LinkedHashSet<Field>();
		
		for(Ability ability : Ability.values()) {
			left.add(abilityField(ability));
		}
		right.add(new Field(Stat.HP.getName(), statted.getHP() + "/" + statted.getMaxHP(), true));
		addIfHasStat(right, Stat.AC, int.class);
		addIfHasStatWithModification(right, Stat.Initiative, int.class, (number) -> {return number.doubleValue() < 0d ? number.toString() : "+" + number.toString();});
		addIfHasStatWithModification(right, Stat.Proficiency_Bonus, int.class, (number) -> {return number.doubleValue() < 0d ? number.toString() : "+" + number.toString();});
		addIfHasStatAnd(right, MovementType.Walking.getStat(), int.class, (stat) -> {return !stat.equals(Integer.valueOf(0));});
		addIfHasStatAnd(right, MovementType.Flying.getStat(), int.class, (stat) -> {return !stat.equals(Integer.valueOf(0));});
		
		weave(builder, left, middle, right);
		
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
		LinkedHashSet<Field> left = new LinkedHashSet<Field>();
		LinkedHashSet<Field> middle = new LinkedHashSet<Field>();
		LinkedHashSet<Field> right = new LinkedHashSet<Field>();
		int i = 0;
		
		LinkedHashSet<Field> current;
		for(Skill skill : Skill.DEFAULT_SKILLS) {
			if(i % 3 == 0) {
				current = left;
			}
			else if (i % 3 == 1){
				current = middle;
			}
			else {
				current = right;
			}
			current.add(new Field(skill.getName() + statted.getProficiency(skill).getEmoji(),
					modifierText(statted.getStat(skill.getStat(), int.class)) , true));
			
			i++;
		}
		
		weave(builder, left, middle, right);
		
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
	
	private Field abilityField(Ability ability) {
		return new Field(ability.shortHand + statted.getProficiency(ability).getEmoji(), statted.getAbilityScore(ability) + "[" + modifierText(statted.getModifier(ability)) + "]", true);
	}
	
	private Field skillField(Skill skill) {
		return new Field(skill.getName() + statted.getProficiency(skill).getEmoji(), statted.getStat(skill.getStat(), int.class) + "", true);
	}
	
	private void addIfHasStat(HashSet<Field> section, Stat stat, Class<?> type) {
		if(statted.hasStat(stat)) {
			section.add(new Field(stat.getName(), statted.getStat(stat, type).toString(), true));
		}
	}
	
	private <T> void addIfHasStatWithModification(HashSet<Field> section, Stat stat, Class<T> type, Function<T, String> modifier) {
		if(statted.hasStat(stat)) {
			T val = statted.getStat(stat, type);
			section.add(new Field(stat.getName(), modifier.apply(val), true));
		}
	}
	
	private <T> void addIfHasStatAnd(HashSet<Field> section, Stat stat, Class<T> type, Predicate<T> predicate) {
		if(statted.hasStat(stat)) {
			T val = statted.getStat(stat, type);
			if(predicate.test(val)) {
				section.add(new Field(stat.getName(), statted.getStat(stat, type).toString(), true));
			}
		}
	}
	
	private <T> void addIfHasStatAndModify(HashSet<Field> section, Stat stat, Class<T> type, Predicate<T> predicate, Function<T,String> modifier) {
		if(statted.hasStat(stat)) {
			T val = statted.getStat(stat, type);
			if(predicate.test(val)) {
				section.add(new Field(stat.getName(), modifier.apply(val), true));
			}
		}
	}
	
	private void weave(EmbedBuilder builder, HashSet<Field> left, HashSet<Field> middle, HashSet<Field> right) {
		Iterator<Field> lIt = left.iterator();
		Iterator<Field> mIt = middle.iterator();
		Iterator<Field> rIt = right.iterator();
		for(int i = 0, max = Math.max(Math.max(left.size(), middle.size()), right.size()); i < max; i++) {
			if(lIt.hasNext()) {
				builder.addField(lIt.next());
			}
			else {
				builder.addBlankField(true);
			}
			if(mIt.hasNext()) {
				builder.addField(mIt.next());
			}
			else {
				builder.addBlankField(true);
			}
			if(rIt.hasNext()) {
				builder.addField(rIt.next());
			}
			else {
				builder.addBlankField(true);
			}
		}
	}
	
}
