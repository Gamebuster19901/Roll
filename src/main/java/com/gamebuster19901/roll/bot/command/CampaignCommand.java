package com.gamebuster19901.roll.bot.command;

import java.util.concurrent.CompletableFuture;

import com.gamebuster19901.roll.bot.command.argument.QuotableStringType;
import com.gamebuster19901.roll.bot.command.argument.SuggestableArgument;
import com.gamebuster19901.roll.bot.game.campaign.Campaign;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class CampaignCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("campaign")
			.then(Commands.literal("create")
				.then(Commands.argument("name", QuotableStringType.TYPE).suggests((context, builder) -> {return getSuggestionsCreateName(context, builder);})
					.executes((context) -> {
						CommandContext c = context.getSource();
						Campaign campaign = createCampaign(c, context.getArgument("name", String.class));
						c.replyMessage("Created campaign `" + campaign.getName() + "`\n\nID: " + campaign.getID() + "\nOwner: " + Users.getUser(campaign.getOwner()).getAsTag());
						return 1;
					})
				)
			)
		);
	}

	public static Campaign createCampaign(CommandContext context, String name) {
		return Campaign.createNewCampaign(name, context.getAuthor());
	}
	
	private static CompletableFuture<Suggestions> getSuggestionsCreateName(com.mojang.brigadier.context.CommandContext<CommandContext> context, SuggestionsBuilder builder) {
		SuggestableArgument helper = new SuggestableArgument(){@Override public Object parse(Object context, StringReader reader) throws CommandSyntaxException { throw new UnsupportedOperationException();}};
		String input = context.getInput();
		String arg = helper.getCurrentArg(input);
		StringRange range = helper.getCurrentArgRange(input);
		int index = helper.getCurrentArgIndex(input);
		
		if(helper.canSuggest(input)) {
			if(index == 0) {
				builder.suggest(new Suggestion(range, "<name>"));
				builder.suggest(new Suggestion(range, "\"<name>\""));
			}
			else {
				if(arg.length() == 0) {
					builder.suggest(new Suggestion(range, "<name>"));
					builder.suggest(new Suggestion(range, "\"<name>\""));
				}
				else if(arg.startsWith("\"")) {
					if(arg.endsWith("\"")) {
						if(arg.length() > 2) {
							builder.suggest(new Suggestion(range, arg));
						}
						else {
							builder.suggest(new Suggestion(range, arg + "<name>"));
						}
					}
					else {
						builder.suggest(new Suggestion(range, arg + "\""));
					}
				}
				else {
					builder.suggest(new Suggestion(range, arg));
				}
			}
		}
		return builder.buildFuture();
	}
	
}
