package com.gamebuster19901.roll.bot.command;

import java.io.ByteArrayOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;

import com.gamebuster19901.roll.bot.command.argument.AbilityArgumentType;
import com.gamebuster19901.roll.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.roll.bot.command.argument.SaveArgumentType;
import com.gamebuster19901.roll.bot.command.argument.SkillArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.game.SaveType;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.stat.Ability;
import com.gamebuster19901.roll.bot.game.stat.Skill;
import com.gamebuster19901.roll.bot.graphics.Theme;
import com.gamebuster19901.roll.bot.graphics.dice.DieGraphicBuilder;
import com.gamebuster19901.roll.bot.graphics.dice.RollGraphicBuilder;
import com.gamebuster19901.roll.bot.graphics.dice.RollResultGraphicBuilder;
import com.gamebuster19901.roll.util.pipe.PipeHelper;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class RollCommand {

	@SuppressWarnings("rawtypes")
	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("roll")
			.then(Commands.argument("ability", AbilityArgumentType.ANY_ABILITY)
				.executes((context) -> {
					return CheckCommand.rollCheck(context.getSource(), context.getArgument("ability", Ability.class));
				})
			)
			.then(Commands.argument("skill", SkillArgumentType.ANY_SKILL)
				.executes((context) -> {
					return CheckCommand.rollCheck(context.getSource(), context.getArgument("skill", Skill.class));
				})
			)
			.then(Commands.argument("death", SaveArgumentType.DEATH_SAVE)
				.executes((context) -> {
					return SaveCommand.rollSave(context.getSource(), context.getArgument("death", SaveType.class)); //all ability saves would be parsed as checks before it gets here, so the only valid save at this point are death saves
				})
			)
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					return roll(context, PlayerCharacter.getActiveCharacter(context.getSource().getAuthor()));
				})
			)
		);
				
	}
	
	public static int roll(com.mojang.brigadier.context.CommandContext<CommandContext> context, Statted statted) {
		Dice dice = context.getArgument("dice", Dice.class);
		return roll(context.getSource(), dice, statted);
	}
	
	public static int roll(CommandContext<?> c, Dice dice, Statted statted) {
		return roll(c, "", dice, statted);
	}
	
	public static int roll(CommandContext<?> c, String name, Dice dice, Statted statted) {
		IReplyCallback e = c.getEvent(IReplyCallback.class);
		InteractionHook hook = e.getHook();
		e.deferReply().queue();
		try {
			Roll roll = new Roll(name, dice, statted);
			int result = roll.getValue();
			c.getEmbed().appendDescription("\n\nResult: " + result);
			DieGraphicBuilder diceGraphic = new RollGraphicBuilder(Theme.DEFAULT_THEME, roll);
			DieGraphicBuilder resultGraphic = new RollResultGraphicBuilder(Theme.DEFAULT_THEME, roll);
			PipedInputStream inDiceGraphic = new PipedInputStream();
			PipedInputStream inResultGraphic = new PipedInputStream();

			
			PipedOutputStream outDiceGraphic = new PipedOutputStream(inDiceGraphic);
			ByteArrayOutputStream baos = resultGraphic.buildImage();
			PipedOutputStream outResultGraphic = (baos == null) ? null : new PipedOutputStream(inResultGraphic);
			
			PipeHelper.pipe(e, diceGraphic.buildImage(), outDiceGraphic);
			if(outResultGraphic != null) {
				PipeHelper.pipe(e, resultGraphic.buildImage(), outResultGraphic);
			}
			
			String rollingText = "Rolling ";
			if(roll.getName().isBlank()) {
				rollingText = rollingText + "`" + roll.getDice() + "`:";
			}
			else {
				rollingText = rollingText + " " + roll.getName() + " [`" + roll.getDice() + "`]:";
			}
			
			EmbedBuilder embedBuilder = c.getEmbed().setDescription(rollingText)
					.setImage("attachment://dice.png")
					.setFooter("Result: " + result + ". Min: " + roll.getMinValue() + " Max: " + roll.getMaxValue());
			if(statted != null) {
				embedBuilder.setAuthor(statted.getName(), null, "attachment://statted.png");
			}
			HashSet<FileUpload> fileUploads = new HashSet<>();
			fileUploads.add(FileUpload.fromData(inDiceGraphic, "dice.png"));
			if(outResultGraphic != null) {
				fileUploads.add(FileUpload.fromData(inResultGraphic, "result.png"));
				embedBuilder.setThumbnail("attachment://result.png");
			}
			
			MessageCreateBuilder message = new MessageCreateBuilder();
			
			message.setFiles(fileUploads).setEmbeds(embedBuilder.build());
			
			if(statted != null && statted instanceof PlayerCharacter) {
				message.addFiles(FileUpload.fromData(((PlayerCharacter)statted).getCharacterImageStream(), "statted.png"));
			}
			if(roll.isSortable()) {
				//action.addActionRow(Button.primary("Sort", "Sort Dice"), Button.secondary("Probability distribution", Emoji.fromUnicode("U+1F4C8")));
			}
			message.addActionRow(Button.secondary("Probability distribution", Emoji.fromUnicode("U+1F4C8")));
			hook.sendMessage(message.build()).queue();
				
			return 1;
		}
		catch(Throwable t) {
			e.reply(t.getMessage() + "\n\nPrinting stacktrace to console.").queue();
			t.printStackTrace();
			return 1;
		}
	}
}
