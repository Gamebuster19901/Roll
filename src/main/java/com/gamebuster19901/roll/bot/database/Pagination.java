package com.gamebuster19901.roll.bot.database;

public class Pagination {

	public static final Pagination NONE = new Pagination(Column.ALL_COLUMNS, 0, 0) {
		@Override
		public String toString() {
			return "";
		}
	};
	
	private final Column orderColumn;
	private final Order order;
	private final int pageSize;
	private final int page;
	
	public Pagination(Column orderColumn, int pageSize, int page) {
		this(orderColumn, Order.ASC, pageSize, page);
	}
	
	public Pagination(Column orderColumn, Order order, int pageSize, int page) {
		this.orderColumn = orderColumn;
		this.order = order;
		this.pageSize = pageSize;
		this.page = page;
	}
	
	
	public String toString() {
		return " ORDER BY " + orderColumn + " " + order + " OFFSET " + (pageSize * page) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
	}
}
