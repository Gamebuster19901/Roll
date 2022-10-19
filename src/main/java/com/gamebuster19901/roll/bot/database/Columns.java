package com.gamebuster19901.roll.bot.database;

public interface Columns {

	public Column[] getColumns();
	
	public default String asString() {
		StringBuilder builder = new StringBuilder();
		Column[] columns = getColumns();
		for(int i = 0; i < columns.length; i++) {
			builder.append(columns[i].toString());
			if(i + 1 < columns.length) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}
	
	public static Columns of(Column...columns) {
		return () -> {return columns;};
	}
}
