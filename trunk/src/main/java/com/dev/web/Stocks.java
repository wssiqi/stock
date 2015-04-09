package com.dev.web;

import java.util.List;

public class Stocks {

	public static List<String> getAllStockId() {
		DBTable dbtable = DBUtil.executeQuery("select * from stock order by id");
		return dbtable.getColumnValues("ID");
	}

}
