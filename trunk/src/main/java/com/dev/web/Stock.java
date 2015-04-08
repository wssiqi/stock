package com.dev.web;

public class Stock {

	private final String stockId;

	public Stock(String stockId) {
		this.stockId = stockId;
	}

	public static Stock get(String stockId) {
		return new Stock(stockId);
	}

	public String getCurrentPrice() {
		StockState stockState = StockState.get(stockId);
		return stockState.getCurrentPrice();
	}

}
