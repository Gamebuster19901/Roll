package com.gamebuster19901.roll.bot.audit;

import com.gamebuster19901.roll.bot.command.ConsoleContext;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Table.*;

import java.io.IOError;
import java.sql.SQLException;

import static com.gamebuster19901.roll.bot.database.Comparator.*;

public class CommandAudit extends Audit {

	Audit parentData;
	
	protected CommandAudit(Row row) {
		super(row, AuditType.COMMAND_AUDIT);
	}
	
	@SuppressWarnings("rawtypes")
	public static CommandAudit addCommandAudit(CommandContext<Object> context, String command) {
		Audit parent = Audit.addAudit(ConsoleContext.INSTANCE, context, AuditType.COMMAND_AUDIT, command);
		
		PreparedStatement st;
		try {
			
			String serverName = null;
			long serverID = 0;
			String channelName = null;
			long channelID = 0;
			long messageID = 0;
			boolean isOperator = context.isOperator();
			
			boolean isConsoleMessage = context.isConsoleMessage();
			
			if(isConsoleMessage) {
				channelName = context.getAuthor().getName();
				channelID = context.getAuthor().getIdLong();
			}
			else {
				serverName = context.getServer().getName();
				serverID = context.getServer().getIdLong();
				channelName = context.getChannel().getName();
				channelID = context.getChannel().getIdLong();
			}
			
			st = Insertion.insertInto(AUDIT_COMMANDS)
			.setColumns(AUDIT_ID, SERVER_NAME, SERVER_ID, CHANNEL_NAME, CHANNEL_ID, IS_OPERATOR)
			.to(parent.getID(), serverName, serverID, channelName, channelID, isOperator)
			.prepare(true);
			
			st.execute();
			
			CommandAudit ret = getCommandAuditByID(ConsoleContext.INSTANCE, parent.getID());
			ret.parentData = parent;
			return ret;
		}
		catch(SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static CommandAudit getCommandAuditByID(CommandContext context, long auditID) {
		return new CommandAudit(Table.selectAllFromJoinedUsingWhere(AUDITS, AUDIT_COMMANDS, AUDIT_ID, new Comparison(AUDIT_ID, EQUALS, auditID)).getRow(true));
	}
	
}