package com.gamebuster19901.roll.bot.audit;

import java.io.IOError;
import java.sql.SQLException;

import com.gamebuster19901.roll.Main;
import com.gamebuster19901.roll.bot.command.ConsoleContext;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.user.DiscordUser;

import net.dv8tion.jda.api.entities.User;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Table.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;

public class RankChangeAudit extends Audit {
	
	Audit parentData;
	
	protected RankChangeAudit(Row row) {
		super(row, AuditType.RANK_CHANGE_AUDIT);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static RankChangeAudit addRankChange(CommandContext context, User promotee, String rank, boolean added) {
		Audit parent = Audit.addAudit(ConsoleContext.INSTANCE, context, AuditType.RANK_CHANGE_AUDIT, getMessage(context, new CommandContext(promotee), rank, added));
		
		PreparedStatement st;
		try {
			st = Insertion.insertInto(AUDIT_RANK_CHANGES)
			.setColumns(AUDIT_ID, PROMOTEE, PROMOTEE_ID)
			.to(parent.getID(), promotee, promotee.getIdLong())
			.prepare(true);
			
			st.execute();
			
			RankChangeAudit ret = getRankChangeAuditByID(ConsoleContext.INSTANCE, parent.getID());
			ret.parentData = parent;
			return ret;
		}
		catch(SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static RankChangeAudit getRankChangeAuditByID(CommandContext context, long auditID) {
		return new RankChangeAudit(Table.selectAllFromJoinedUsingWhere(AUDITS, AUDIT_RANK_CHANGES, AUDIT_ID, new Comparison(AUDIT_ID, EQUALS, auditID)).getRow(true));
	}
	
	@SuppressWarnings("rawtypes")
	private static final String getMessage(CommandContext promoter, CommandContext<User> promotee, String rank, boolean added) {
		if(added) {
			return promoter.getAuthor().getAsTag() + " made " + DiscordUser.getDetailedString(promotee.getAuthor()) + " a bot " + rank + " for " + Main.discordBot.getSelfUser().getName();
		}
		return DiscordUser.getDetailedString(promoter.getAuthor()) + " removed the bot " + rank + " rights from " + DiscordUser.getDetailedString(promotee.getAuthor()) + " for " + Main.discordBot.getSelfUser().getName();
	}
	
}