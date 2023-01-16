package com.gamebuster19901.roll.bot.audit;

import com.gamebuster19901.roll.bot.command.ConsoleContext;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.user.DiscordUser;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;
import static com.gamebuster19901.roll.bot.database.Table.*;

import java.io.IOError;
import java.sql.SQLException;

public class BotDeleteMessageAudit extends Audit {

	Audit parentData;
	
	protected BotDeleteMessageAudit(Row result) {
		super(result, AuditType.BOT_MSG_DELETE);
	}
	
	public static BotDeleteMessageAudit addBotDeleteMessageAudit(CommandContext deleter, CommandContext deleted, String reason) {
		Audit parent = Audit.addAudit(deleter, deleted, AuditType.BOT_MSG_DELETE, getMessage(ConsoleContext.INSTANCE, deleted, reason));
		
		PreparedStatement st;
		
		try {
			st = Insertion.insertInto(Table.AUDIT_BOT_MSG_DEL)
			.setColumns(AUDIT_ID, DISCORD_ID)
			.to(parent.getID(), deleted.getAuthor())
			.prepare(true);
			
			st.execute();
			
			BotDeleteMessageAudit ret = getBotDeleteMessageAuditByID(ConsoleContext.INSTANCE, parent.getID());
			ret.parentData = parent;
			
			return ret;
		}
		catch(SQLException e) {
			throw new IOError(e);
		}
	}
	
	private static BotDeleteMessageAudit getBotDeleteMessageAuditByID(ConsoleContext context, long auditID) {
		return new BotDeleteMessageAudit(Table.selectAllFromJoinedUsingWhere(AUDITS, AUDIT_BOT_MSG_DEL, AUDIT_ID, new Comparison(AUDIT_ID, EQUALS, auditID)).getRow(true));
	}
	
	private static String getMessage(CommandContext deleter, CommandContext sender, String reason) {
		return DiscordUser.getDetailedString(deleter.getAuthor()) + " deleted a message by " + DiscordUser.getDetailedString(sender.getAuthor())+ " due to \"" + reason + "\"";
	}

}