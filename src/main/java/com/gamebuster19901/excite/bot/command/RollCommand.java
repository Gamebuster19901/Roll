package com.gamebuster19901.excite.bot.command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.gamebuster19901.excite.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.graphics.DieAnimationBuilder;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class RollCommand {

	@SuppressWarnings("rawtypes")
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.literal("roll")
			.then(Commands.argument("dice", DiceArgumentType.DICE_ARGUMENT_TYPE)
				.executes((context) -> {
					CommandContext c = context.getSource();
					Dice dice = context.getArgument("dice", Dice.class);
					dice.roll();
					int result = dice.getValue();
					c.getEmbed().appendDescription(dice.toString());
					c.getEmbed().appendDescription("\n\nResult: " + result);
					DieAnimationBuilder animation = new DieAnimationBuilder(dice);
					//c.getEmbed().setImage("attachment://test.png");
					PipedInputStream in = new PipedInputStream();

					try {
						PipedOutputStream out = new PipedOutputStream(in);
						new Thread(() -> {try {
							animation.buildFrames().writeTo(out);
							out.close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}}).start();
						c.getChannel().sendFiles(FileUpload.fromData(in, "test.gif")).complete();
						//c.getChannel().sendFiles(FileUpload.fromData(RollCommand.class.getResourceAsStream("./0000.png"), "test2.png")).complete();
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
