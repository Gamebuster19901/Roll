package com.gamebuster19901.excite.bot.database;

import java.io.IOError;
import java.sql.SQLException;

import com.gamebuster19901.excite.bot.command.CommandContext;
import com.gamebuster19901.excite.bot.database.sql.PreparedStatement;
import static com.gamebuster19901.excite.bot.database.Column.ALL_COLUMNS;

public enum Table {
	
	CHARACTERS,
	THEMES,
	
	;
	
	public static final String HOST = "@'localhost'";
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectColumnsFrom(CommandContext context, Column columns, Table table) throws SQLException {
		PreparedStatement st = context.getConnection().prepareStatement("SELECT " + columns + " FROM " + table);
		return st.query();
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectColumnsFromWhere(CommandContext context, Column columns, Table table, Comparison comparison) throws SQLException {
		PreparedStatement st;
		st = context.getConnection().prepareStatement("SELECT " + columns + " FROM " + table + " WHERE " + comparison);
		comparison.insertValues(st);

		return st.query();
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFrom(CommandContext context, Table table) throws SQLException {
		return selectColumnsFrom(context, ALL_COLUMNS, table);
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFromWhere(CommandContext context, Table table, Comparison comparison) throws SQLException {
		return selectColumnsFromWhere(context, ALL_COLUMNS, table, comparison);
	}
	
	@SuppressWarnings("rawtypes")
	public static Result selectAllFromJoinedUsingWhere(CommandContext context, Table mainTable, Table otherTable, Column usingColumn, Comparison comparison) {
		try {
			PreparedStatement st = context.getConnection().prepareStatement("SELECT * FROM " + mainTable + " JOIN " + otherTable + " USING (" + usingColumn + ") WHERE " + comparison);
			comparison.insertValues(st);
			return st.query();
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean existsWhere(CommandContext context, Table table, Comparison comparison) {
		try {
			Result result = selectColumnsFromWhere(context, (Column) comparison.getColumn(), table, comparison);
			return result.getRowCount() > 0;
		} catch (SQLException e) {
			throw new IOError(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void updateWhere(CommandContext context, Table table, Column parameter, Object value, Comparison comparison) throws SQLException {
		PreparedStatement st = context.getConnection().prepareStatement("UPDATE " + table + " SET " + parameter + " = ? WHERE " + comparison);
		insertValue(st, 1, value);
		comparison.offset(1);
		comparison.insertValues(st, 2);
		st.execute();
	}
	
	@SuppressWarnings("rawtypes")
	public static void deleteWhere(CommandContext context, Table table, Comparison comparison) throws SQLException {
		PreparedStatement st = null;
		st = context.getConnection().prepareStatement("DELETE FROM " + table + " WHERE " + comparison);
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
