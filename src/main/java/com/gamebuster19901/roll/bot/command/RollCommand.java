package com.gamebuster19901.roll.bot.command;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.gamebuster19901.roll.bot.command.argument.DiceArgumentType;
import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.Die;
import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.game.RollValue;
import com.gamebuster19901.roll.bot.game.character.PlayerCharacter;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.bot.graphics.Theme;
import com.gamebuster19901.roll.bot.graphics.dice.DieGraphicBuilder;
import com.gamebuster19901.roll.bot.graphics.dice.RollResultBuilder;
import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.EmbedBuilder;
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
						PlayerCharacter character = PlayerCharacter.getActiveCharacter(c.getAuthor().getIdLong());
						Roll roll = new Roll(dice, character);
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
						StringBuilder warn = new StringBuilder();
						if(character != null) {
							embedBuilder.setAuthor(character.getName(), null, "attachment://character.png");
							for(Die die : dice.getAllDice()) {
								if(die instanceof RollValue) {
									Stat stat = ((RollValue)die).getStat();
									if(!character.hasStat(stat)) {
										warn.append('\n');
										warn.append('`');
										warn.append(stat.getName());
										warn.append('`');
									}
								}
							}
						}
						else {
							for(Die die : dice.getAllDice()) {
								if(die instanceof RollValue) {
									warn.append('\n');
									warn.append('`');
									warn.append(((RollValue)die).getStat().getName());
									warn.append('`');
								}
							}
						}
						if(warn.length() > 0) {
							if(character == null) {
								embedBuilder.appendDescription("\n\nNOTICE: NO ACTIVE CHARACTER! THE FOLLOWING VARIABLES WERE SET TO `0`:\n\n" + warn);
							}
							else {
								embedBuilder.appendDescription("\n\nNOTICE: `" + character.getName() + "` DOES NOT HAVE THE FOLLOWING STATS! THEY HAVE BEEN SET TO `0`:\n\n" + warn);
							}
						}
						embedBuilder.appendDescription("\n");
						ReplyCallbackAction action = e.deferReply()
							.setFiles(FileUpload.fromData(in, "test.png"))
							.setEmbeds(embedBuilder.build());
						if(character != null) {
							action.addFiles(FileUpload.fromData(character.getCharacterImageStream(), "character.png"));
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
				})
			)
		);
				
	}
	
}
