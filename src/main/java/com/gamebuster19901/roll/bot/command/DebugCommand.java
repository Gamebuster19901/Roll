package com.gamebuster19901.roll.bot.command;

import com.mojang.brigadier.CommandDispatcher;

import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class DebugCommand {

	private static final MessageCreateData SHOULD_BE_SILENT = new MessageCreateBuilder().setSuppressedNotifications(true).setContent("This should be silent!").build();
	
	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("debug")
			.then(Commands.suggestableString("silence1") //<bot> is thinking... message is not silent
					.executes((context) -> {
						context.getSource().getReplyCallback().deferReply().setSuppressedNotifications(true).queue();
						return 1;
					})
				)
			.then(Commands.suggestableString("silence2") //the editied message is not silent
					.executes((context) -> {
						ReplyCallbackAction action = context.getSource().getReplyCallback().deferReply().setSuppressedNotifications(true);
						action.queue((hook) -> {
							hook.editOriginal(MessageEditData.fromCreateData(SHOULD_BE_SILENT)).queue();
						});
						return 1;
					})
				)
			.then(Commands.suggestableString("silence3") //the message is silent as intended.
				.executes((context) -> {
					context.getSource().getReplyCallback().reply(SHOULD_BE_SILENT).queue();
					return 1;
				})
			)
		);
	}

}
