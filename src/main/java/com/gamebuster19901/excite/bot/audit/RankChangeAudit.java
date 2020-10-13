package com.gamebuster19901.excite.bot.audit;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.gamebuster19901.excite.Main;
import com.gamebuster19901.excite.bot.command.MessageContext;
import com.gamebuster19901.excite.bot.user.DiscordUser;

public class RankChangeAudit extends Audit {

	private static final int DB_VERSION = 0;
	
	@SuppressWarnings("rawtypes")
	public RankChangeAudit(MessageContext promoter, MessageContext<DiscordUser> promotee, String rank, boolean added) {
		super(promoter, getMessage(promoter, promotee, rank, added));
	}
	
	protected RankChangeAudit() {
		super();
	}
	
	@Override
	public Audit parseAudit(CSVRecord record) {
		super.parseAudit(record);
		//0-6 is audit
		//7 is RankChanceAudit version
		
		return this;
	}
	
	@Override
	public List<Object> getParameters() {
		List<Object> params = super.getParameters();
		params.addAll(Arrays.asList(new Object[] {new Integer(DB_VERSION)}));
		return params;
	}
	
	@SuppressWarnings("rawtypes")
	private static final String getMessage(MessageContext promoter, MessageContext<DiscordUser> promotee, String rank, boolean added) {
		if(added) {
			return promoter.getAuthor().toDetailedString() + " made " + promotee.getAuthor().toDetailedString() + " a bot " + rank + " for " + Main.discordBot.getSelfUser().getName();
		}
		return promoter.getAuthor().toDetailedString() + " removed the bot " + rank + " rights from " + promotee.getAuthor().toDetailedString() + " for " + Main.discordBot.getSelfUser().getName();
	}
	
}