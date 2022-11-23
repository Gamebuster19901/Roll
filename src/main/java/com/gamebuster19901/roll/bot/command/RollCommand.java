package com.gamebuster19901.roll.bot.command;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.gamebuster19901.roll.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.graphics.Theme;
import com.gamebuster19901.roll.bot.graphics.dice.DieGraphicBuilder;
import com.gamebuster19901.roll.bot.graphics.dice.RollResultBuilder;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class RollCommand {

	@SuppressWarnings("rawtypes")
	static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("roll")
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					return roll(context, PlayerCharacter.getActiveCharacter(context.getSource().getAuthor()));
				})
			)
		);
				
	}
	
	public static int roll(com.mojang.brigadier.context.CommandContext<CommandContext> context, Statted statted) {
		CommandContext<?> c = context.getSource();
		IReplyCallback e = c.getEvent(IReplyCallback.class);
		Dice dice = context.getArgument("dice", Dice.class);
		try {
			Roll roll = new Roll(dice, statted);
			int result = roll.getValue();
			c.getEmbed().appendDescription("\n\nResult: " + result);
			DieGraphicBuilder graphic = new RollResultBuilder(Theme.DEFAULT_THEME, roll);
			PipedInputStream in = new PipedInputStream();

			PipedOutputStream out = new PipedOutputStream(in);
			new Thread(() -> {try {
				graphic.buildImage().writeTo(out);
				out.close();
			} catch (Throwable ex) {
				e.reply(ex.getMessage() + "\n\n Printing stacktrace to console.").queue();
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}}).start();
			EmbedBuilder embedBuilder = c.getEmbed().setDescription("Rolling " + roll.getDice())
					.setImage("attachment://test.png")
					.setFooter("Result: " + result + ". Min: " + roll.getMinValue() + " Max: " + roll.getMaxValue());
			if(statted != null) {
				embedBuilder.setAuthor(statted.getName(), null, "attachment://statted.png");
			}
			ReplyCallbackAction action = e.deferReply()
				.setFiles(FileUpload.fromData(in, "test.png"))
				.setEmbeds(embedBuilder.build());
			if(statted != null && statted instanceof PlayerCharacter) {
				action.addFiles(FileUpload.fromData(((PlayerCharacter)statted).getCharacterImageStream(), "statted.png"));
			}
			if(roll.isSortable()) {
				//action.addActionRow(Button.primary("Sort", "Sort Dice"), Button.secondary("Probability distribution", Emoji.fromUnicode("U+1F4C8")));
			}
			action.addActionRow(Button.secondary("Probability distribution", Emoji.fromUnicode("U+1F4C8")));
			action.queue();
				
			return 1;
		}
		catch(Throwable t) {
			e.reply(t.getMessage() + "\n\nPrinting stacktrace to console.").queue();
			t.printStackTrace();
			return 1;
		}
	}
	
}
