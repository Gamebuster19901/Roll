package com.gamebuster19901.excite.bot.command;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.Duration;

import com.gamebuster19901.excite.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.graphics.DieGraphicBuilder;
import com.gamebuster19901.excite.bot.graphics.RollResultBuilder;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

public class RollCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("roll")
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					CommandContext<?> c = context.getSource();
					SlashCommandInteractionEvent e = c.getEvent(SlashCommandInteractionEvent.class);
					Dice dice = context.getArgument("dice", Dice.class);
					dice.roll();
					int result = dice.getValue();
					c.getEmbed().appendDescription("\n\nResult: " + result);
					DieGraphicBuilder graphic = new RollResultBuilder(dice);
					PipedInputStream in = new PipedInputStream();

					try {
						PipedOutputStream out = new PipedOutputStream(in);
						new Thread(() -> {try {
							graphic.buildImage().writeTo(out);
							out.close();
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}}).start();
						e.deferReply().setFiles(FileUpload.fromData(in, "test.png")).setEmbeds(c.getEmbed().setDescription("Rolling " + dice).setImage("attachment://test.png").build()).delay(Duration.ofSeconds(3)).queue();
						//e.reply("Rolling " + dice).addFiles(FileUpload.fromData(in, "test.png")).complete();
					}
					catch(Throwable t) {
						throw new RuntimeException(t);
					}

					//c.sendMessage(c.getEmbed());
					System.out.println(result);
					return 1;
				})
			)
		);
				
	}
	
}
