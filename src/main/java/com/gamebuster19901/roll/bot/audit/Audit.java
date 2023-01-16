package com.gamebuster19901.roll.bot.audit;

import java.io.IOError;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Instant;

import javax.annotation.Nullable;

import com.gamebuster19901.roll.bot.command.CommandContext;
import com.gamebuster19901.roll.bot.database.Comparison;
import com.gamebuster19901.roll.bot.database.Insertion;
import com.gamebuster19901.roll.bot.database.Result;
import com.gamebuster19901.roll.bot.database.Row;
import com.gamebuster19901.roll.bot.database.Table;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;
import com.gamebuster19901.roll.bot.database.sql.ResultSet;
import com.gamebuster19901.roll.util.Identified;
import com.gamebuster19901.roll.util.TimeUtils;

import net.dv8tion.jda.api.entities.User;

import static com.gamebuster19901.roll.bot.database.Column.*;
import static com.gamebuster19901.roll.bot.database.Comparator.*;
import static com.gamebuster19901.roll.bot.database.Table.*;

public class Audit implements Identified{
	
	private final long auditID;
	private final AuditType type;
	protected Row row;
	
	protected Audit(Row result, AuditType type) {
		if(result == null) {
			throw new NullPointerException();
		}
		this.auditID = result.getLong(AUDIT_ID);
		this.type = type;
		this.row = result;
	}
	
	@SuppressWarnings("rawtypes")
	protected static Audit addAudit(CommandContext context, AuditType type, String description) {
		return addAudit(context, type, description, Instant.now());
	}
	
	@SuppressWarnings("rawtypes")
	protected static Audit addAudit(CommandContext executor, CommandContext context, AuditType type, String description) {
		return addAudit(executor, context, type, description, Instant.now());
	}
	
	@SuppressWarnings("rawtypes")
	protected static Audit addAudit(CommandContext context, AuditType type, String description, Instant dateIssued) {
		return addAudit(context, context, type, description, dateIssued);
	}
	
	@Deprecated
	@SuppressWarnings("rawtypes")
	protected static Audit addAudit(CommandContext executor, CommandContext context, AuditType type, String description, Instant dateIssued) {
		PreparedStatement ps;
		try {
			ps = Insertion.insertInto(AUDITS).setColumns(AUDIT_TYPE, ISSUER_ID, ISSUER_NAME, DESCRIPTION, DATE_ISSUED)
				.to(type, context.getAuthor().getIdLong(), context.getAuthor().getName(), description, Instant.now())
				.prepare(true);
			ps.execute();
			ResultSet results = ps.getGeneratedKeys();
			results.next();
			long auditID = results.getLong(GENERATED_KEY);
			Row row = Table.selectAllFromWhere(AUDITS, new Comparison(AUDIT_ID, EQUALS, auditID)).getRow(true);
			return new Audit(row, type);
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	@Nullable
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Audit createAudit(CommandContext context, Row row) throws SQLException {
		AuditType type = AuditType.getType(row);
		long auditID = row.getLong(AUDIT_ID);
		Class<? extends Audit> auditTypeClazz = AuditType.getType(row).getType();
		try {
			Constructor<? extends Audit> constructor = auditTypeClazz.getDeclaredConstructor(Row.class);
			constructor.setAccessible(true);
			Result result = Table.selectAllFromJoinedUsingWhere(AUDITS, type.getTable(), AUDIT_ID, new Comparison(AUDIT_ID, EQUALS, auditID));
			result.next();
			if(result.cursorValid()) {
				Row resultRow = result.getRow();
				return constructor.newInstance(resultRow);
			}
			return null;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new AssertionError(e);
		}
	}
	
	@Override
	public long getID() {
		return auditID;
	}
	
	public final AuditType getType() {
		return type;
	}
	
	public long getIssuerID() {
		return row.getLong(ISSUER_ID);
	}
	
	public String getIssuerUsername() {
		return row.getString(ISSUER_NAME);
	}
	
	public String getDescription() {
		return row.getString(DESCRIPTION);
	}
	
	public Instant getDateIssued() {
		return TimeUtils.parseInstant(row.getString(DATE_ISSUED));
	}
	
	@Nullable
	@SuppressWarnings("rawtypes")
	public static Audit getAuditById(CommandContext context, long id) {
		try {
			return createAudit(context, Table.selectAllFromWhere(AUDITS, new Comparison(AUDIT_ID, EQUALS, id)).getRow(true));
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	public AuditType getAuditType() {
		return AuditType.getType(this);
	}
	
	@SuppressWarnings("rawtypes")
	public User getIssuerDiscord(CommandContext context) {
		return context.getAuthor();
	}
	
}