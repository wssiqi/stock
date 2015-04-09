package com.dev.web;

public class StockDaily {

	private final String stockId;

	public StockDaily(String stockId) {
		this.stockId = stockId;
		init();
	}

	private void init() {
		DBUtil.executeQuery("");
	}

	public static StockDaily get(String stockId) {
		return new StockDaily(stockId);
	}

}
