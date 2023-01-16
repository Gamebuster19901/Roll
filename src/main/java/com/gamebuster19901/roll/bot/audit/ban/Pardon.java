package com.gamebuster19901.roll.bot.audit.ban;

import com.gamebuster19901.roll.bot.audit.Audit;
import com.gamebuster19901.roll.bot.audit.AuditType;
import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.user.DiscordUser;

import static com.gamebuster19901.roll.bot.database.Table.*;

import java.io.IOError;
import java.sql.SQLException;

import javax.annotation.Nullable;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;
import static com.gamebuster19901.roll.bot.audit.AuditType.*;

public class Pardon extends Audit{

	Audit parentData;
	
	protected Pardon(Row result) {
		super(result, PARDON);
	}
	
	@Nullable
	@SuppressWarnings("rawtypes")
	public static Pardon addPardonByAuditID(CommandContext context, long id) {
		try {
			Audit pardoned = Audit.getAuditById(context, id);
			if(pardoned == null) {
				context.sendMessage("Could not find audit #" + id);
				return null;
			}
			AuditType type = pardoned.getType();
			if(type == BAN) {
				Ban ban = (Ban) pardoned;
				if(ban.isPardoned()) {
					context.sendMessage(type + " #" + id + " is already pardoned.");
					return null;
				}
				Audit parent = Audit.addAudit(context, PARDON, DiscordUser.getDetailedString(context.getAuthor()) + " pardoned " + ban.getBannedUsername());
				
				PreparedStatement st;
				
				st = Insertion.insertInto(AUDIT_PARDONS)
				.setColumns(AUDIT_ID, PARDONED_AUDIT_ID)
				.to(parent.getID(), pardoned.getID())
				.prepare(true);
				
				st.execute();
				
				Pardon ret = getPardonByAuditID(context, parent.getID());
				ret.parentData = parent;
				
				return ret;
			}
			else {
				context.sendMessage("Audit #" + id + " is a " + type + ", not a " + BAN);
				return null;
			}
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Pardon addPardonByBan(CommandContext context, Ban ban) {
		if(ban != null) {
			return addPardonByAuditID(context, ban.getID());
		}
		throw new NullPointerException();
	}
	
	@SuppressWarnings("rawtypes")
	public static Pardon getPardonByAuditID(CommandContext context, long auditID) {
		return new Pardon(Table.selectAllFromJoinedUsingWhere(AUDITS, AUDIT_PARDONS, AUDIT_ID, new Comparison(AUDIT_ID, EQUALS, auditID)).getRow(true));
	}
	
	@SuppressWarnings("rawtypes")
	public static Pardon getPardonByPardonedID(CommandContext context, long banID) {
		return new Pardon(Table.selectAllFromJoinedUsingWhere(AUDITS, AUDIT_PARDONS, AUDIT_ID, new Comparison(PARDONED_AUDIT_ID, EQUALS, banID)).getRow(true));
	}
	
}