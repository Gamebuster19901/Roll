package com.gamebuster19901.roll.bot.command;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.gamebuster19901.roll.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.graphics.Theme;
import com.gamebuster19901.roll.bot.graphics.dice.DieGraphicBuilder;
import com.gamebuster19901.roll.bot.graphics.dice.RollResultBuilder;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;

class RollCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("roll")
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					CommandContext<?> c = context.getSource();
					SlashCommandInteractionEvent e = c.getEvent(SlashCommandInteractionEvent.class);
					Dice dice = context.getArgument("dice", Dice.class);
					try {
						Roll roll = new Roll(dice);
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
						ReplyCallbackAction action = e.deferReply()
							.setFiles(FileUpload.fromData(in, "test.png"))
							.setEmbeds(c.getEmbed().setDescription("Rolling " + roll.getDice())
								.setImage("attachment://test.png")
								.setFooter("Result: " + result + ". Min: " + roll.getMinValue() + " Max: " + roll.getMaxValue())
								.build());
						if(roll.isSortable()) {
							action.addActionRow(Button.primary("Sort", "Sort Dice"), Button.secondary("Probability distribution", Emoji.fromUnicode("U+1F4C8")));
						}
						action.queue();
							//e.reply("Rolling " + dice).addFiles(FileUpload.fromData(in, "test.png")).complete();

	
						//c.sendMessage(c.getEmbed());
						System.out.println(result);
						return 1;
					}
					catch(Throwable t) {
						e.reply(t.getMessage() + "\n\nPrinting stacktrace to console.").queue();
						t.printStackTrace();
						return 1;
					}
				})
			)
		);
				
	}
	
}
