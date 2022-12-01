package com.gamebuster19901.roll.bot.database;

import java.sql.SQLException;
import java.sql.Statement;

import com.gamebuster19901.roll.bot.database.sql.Database;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;

public class Insertion {

	private final Table table;
	
	private final Column[] columns;
	private Object[] values;
	
	private final String statementString;
	
	private Insertion(Table table, Column[] columns, Object[] values) {
		this.table = table;
		this.columns = columns;
		this.values = values;
		
		String[] columnNames = new String[columns.length];
		String[] parameters = new String[columns.length];
		for(int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columns[i].toString();
			parameters[i] = "?";
		}
		
		String columnString = String.join(", ", columnNames);
		String parameterString = String.join(", ", parameters);
		
		this.statementString = "INSERT INTO " + table + " (" + columnString + ") VALUES(" + parameterString + ");";
	}
	
	public PreparedStatement prepare(boolean returnGeneratedKeys) throws SQLException {
		PreparedStatement ps;
		if(!returnGeneratedKeys) {
			ps = Database.INSTANCE.prepareStatement(statementString);
		}
		else {
			ps = Database.INSTANCE.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS);
		}
		
		if(values != null && values.length > 0) {
			ps.setValues(values);
		}
		return ps;
	}
	
	public PreparedStatement prepare() throws SQLException {
		return prepare(false);
	}
	
	public Insertion to(Object... values) {
		if(this.values != null) {
			throw new IllegalStateException("Values already set!");
		}
		if(columns == null || columns.length == 0) {
			throw new IllegalStateException("Must set columns before setting their values!");
		}
		if(columns.length != values.length) {
			throw new IllegalArgumentException("Argument count is not equal to column count!");
		}
		this.values = values;
		return this;
	}
	
	public String toString() {
		return statementString;
	}
	
	public static IncompleteInsertion insertInto(Table table) {
		return new IncompleteInsertion(table);
	}
	
	public static final class IncompleteInsertion {
		private final Table table;
		
		private Column[] columns;
		private Object[] values;
		
		private IncompleteInsertion(Table table) {
			this.table = table;
		}
		
		public Insertion setColumns(Column...columns) {
			for(Column column : columns) {
				if(!column.isInTable(table)) {
					throw new IllegalArgumentException("Column " + column + " is not in table " + table);
				}
			}
			this.columns = columns;
			return new Insertion(table, columns, values);
		}
	}
	
}
