package com.gamebuster19901.roll.bot.audit;

import com.gamebuster19901.roll.bot.audit.ban.*;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;

import static com.gamebuster19901.roll.bot.database.Table.*;
import static com.gamebuster19901.roll.bot.database.Column.*;

public enum AuditType {

	BAN(Ban.class, AUDIT_BANS),
	PARDON(Pardon.class, AUDIT_PARDONS),
	
	BOT_MSG_DELETE(BotDeleteMessageAudit.class, AUDIT_BOT_MSG_DEL),
	COMMAND_AUDIT(CommandAudit.class, AUDIT_COMMANDS),
	RANK_CHANGE_AUDIT(RankChangeAudit.class, AUDIT_RANK_CHANGES),
	
	;

	private Class<? extends Audit> type;
	private Table table;
	
	AuditType(Class<? extends Audit> type, Table table) {
		this.type = type;
		this.table = table;
	}
	
	public Class<? extends Audit> getType() {
		return type;
	}
	
	public Table getTable() {
		return table;
	}
	
	public static AuditType getType(Audit audit) {
		for(AuditType type : values()) {
			if(audit.getClass() == type.type) {
				return type;
			}
		}
		throw new AssertionError("Could not find audit type " + audit.getClass().getSimpleName());
	}
	
	public static AuditType getType(Row row) {
		String type = row.getString(AUDIT_TYPE);
		for(AuditType audit : values()) {
			if(audit.name().equals(type)) {
				return audit;
			}
			System.out.println(audit.getType().getName() + " != " + type);
		}
		throw new AssertionError("Could not find audit type " + type);
	}
	
}