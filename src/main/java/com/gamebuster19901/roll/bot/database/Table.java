package com.gamebuster19901.roll.bot.database;

import static com.gamebuster19901.roll.bot.database.Column.ALL_COLUMNS;

import java.io.IOError;
import java.sql.SQLException;

import com.gamebuster19901.roll.bot.database.sql.Database;
import com.gamebuster19901.roll.bot.database.sql.PreparedStatement;

public enum Table {
	
	CHARACTERS,
	CLASSES,
	PLAYERS,
	RACES,
	THEMES,
	
	;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectColumnsFrom(Columns columns, Table table) throws SQLException {
		PreparedStatement st = Database.INSTANCE.prepareStatement("SELECT " + columns + " FROM " + table);
		return st.query();
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectColumnsFromWhere(Columns columns, Table table, Comparison comparison, Pagination pagination) {
		try {
			PreparedStatement st;
			st = Database.INSTANCE.prepareStatement("SELECT " + columns + " FROM " + table + " WHERE " + comparison + pagination);
			comparison.insertValues(st);
	
			return st.query();
		}
		catch(SQLException e) {
			throw new IOError(e);
		}
	}
	
	public static Result selectColumnsFromWhere(Columns columns, Table table, Comparison comparison) {
		return selectColumnsFromWhere(columns, table, comparison, Pagination.NONE);
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFrom(Table table) throws SQLException {
		return selectColumnsFrom(ALL_COLUMNS, table);
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFromWhere(Table table, Comparison comparison) throws SQLException {
		return selectColumnsFromWhere(ALL_COLUMNS, table, comparison);
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFromJoinedUsingWhere(Table mainTable, Table otherTable, Column usingColumn, Comparison comparison) {
		try {
			PreparedStatement st = Database.INSTANCE.prepareStatement("SELECT * FROM " + mainTable + " JOIN " + otherTable + " USING (" + usingColumn + ") WHERE " + comparison);
			comparison.insertValues(st);
			return st.query();
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean existsWhere(Table table, Comparison comparison) {
		Result result = selectColumnsFromWhere((Column) comparison.getColumn(), table, comparison);
		return result.getRowCount() > 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void updateWhere(Table table, Column parameter, Object value, Comparison comparison) throws SQLException {
		PreparedStatement st = Database.INSTANCE.prepareStatement("UPDATE " + table + " SET " + parameter + " = ? WHERE " + comparison);
		insertValue(st, 1, value);
		comparison.offset(1);
		comparison.insertValues(st, 2);
		st.execute();
	}
	
	@SuppressWarnings("rawtypes")
	public static void deleteWhere(Table table, Comparison comparison) throws SQLException {
		PreparedStatement st = null;
		st = Database.INSTANCE.prepareStatement("DELETE FROM " + table + " WHERE " + comparison);
		comparison.insertValues(st);
		st.execute();
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void insertValue(PreparedStatement st, int index, Object value) throws SQLException {
		if(value == null) {
			st.setObject(index, null);
		}
		else {
			Class clazz = value.getClass();
			if(clazz == Comparison.class) {
				insertValue(st, index, ((Comparison) value).getValue(index));
			}
			if(clazz.isPrimitive() || value instanceof Number || value instanceof Boolean || value instanceof Character) {
				if (clazz == long.class || clazz == Long.class) {
					st.setLong(index, (long)value);
				}
				else if(clazz == int.class || clazz == Integer.class) {
					st.setInt(index, (int)value);
				}
				else if (clazz == boolean.class || clazz == Boolean.class) {
					st.setBoolean(index, (boolean)value);
				}
				else if (clazz == double.class || clazz == Double.class) {
					st.setDouble(index, (double)value);
				}
				else if (clazz == float.class || clazz == Float.class) {
					st.setFloat(index, (float)value);
				}
				else if (clazz == short.class || clazz == Short.class) {
					st.setShort(index, (short)value);
				}
				else if (clazz == byte.class || clazz == Byte.class) {
					st.setShort(index, (byte)value);
				}
				else if (clazz == char.class || clazz == Character.class) {
					st.setString(index, value.toString());
				}
			}
			else {
				st.setString(index, value.toString());
			}
		}
	}
	
	public static String makeSafe(String string) {
		return string.replace("%", "\\%").replace("_", "\\_").replace("\\", "\\\\");
	}
}
