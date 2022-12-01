package com.gamebuster19901.roll.bot.command;

import com.gamebuster19901.roll.bot.command.argument.QuotableStringType;
import com.gamebuster19901.roll.bot.game.campaign.Campaign;
import com.gamebuster19901.roll.bot.game.user.Users;
import com.mojang.brigadier.CommandDispatcher;

public class CampaignCommand {

	public static void register(CommandDispatcher<CommandContext> dispatcher) {
		dispatcher.register(Commands.global("campaign")
			.then(Commands.literal("create")
				.then(Commands.argument("name", QuotableStringType.TYPE)
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
	
}
