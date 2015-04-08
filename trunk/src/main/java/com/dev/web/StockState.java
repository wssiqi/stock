package com.dev.web;


public class StockState {

	private final String stockId;
	private boolean valid;
	private String currentPrice;
	private String deltaPrice;
	private String deltaPercent;
	private String turnoverRate;
	private String dateTime;
	private String closingPriceYesterday;
	private String openingPrice;
	private String topPrice;
	private String lowestPrice;
	private String turnover;
	private String volume;
	private String relativeRate;
	private String priceEarningsRatio;
	private String sell;
	private String buy;

	public StockState(String stockId) {
		this.stockId = stockId;
		initPropertyFromWeb();
	}

	private void initPropertyFromWeb() {
		this.valid = false;
		String stockPageUrl = String.format(
				"http://wap.eastmoney.com/StockInfo.aspx?stockcode=%s&vt=0",
				stockId);
		String pageContent = CommonUtils.getWebPageContent(stockPageUrl);
		this.currentPrice = CommonUtils.extractRegexGroup1(pageContent,
				"当前价：([0-9\\.]+)元");
		this.deltaPrice = CommonUtils.extractRegexGroup1(pageContent,
				"涨跌额：([0-9\\.\\-]+)");
		this.deltaPercent = CommonUtils.extractRegexGroup1(pageContent,
				"涨跌幅：([0-9\\.\\-\\%]+)");
		this.turnoverRate = CommonUtils.extractRegexGroup1(pageContent,
				"换手率：([0-9\\.\\-\\%]+)");
		this.dateTime = CommonUtils.extractRegexGroup1(pageContent,
				"更新时间：([0-9\\.\\-: ]+)");

		this.closingPriceYesterday = CommonUtils.extractRegexGroup1(
				pageContent, "昨收盘：([0-9\\.\\-: ]+)");
		this.openingPrice = CommonUtils.extractRegexGroup1(pageContent,
				"今开盘：([0-9\\.\\-: ]+)");
		this.topPrice = CommonUtils.extractRegexGroup1(pageContent,
				"最高价：([0-9\\.\\-: ]+)");
		this.lowestPrice = CommonUtils.extractRegexGroup1(pageContent,
				"最低价：([0-9\\.\\-: ]+)");
		this.volume = CommonUtils.extractRegexGroup1(pageContent,
				"成交量：([0-9\\.\\-: ]+)");
		this.turnover = CommonUtils.extractRegexGroup1(pageContent,
				"成交额：([0-9\\.\\-: ]+)");

		this.relativeRate = CommonUtils.extractRegexGroup1(pageContent,
				"量比：([0-9\\.\\-: ]+)");
		this.priceEarningsRatio = CommonUtils.extractRegexGroup1(pageContent,
				"市盈：([0-9\\.\\-: ]+)");
		this.sell = CommonUtils.extractRegexGroup1(pageContent,
				"内盘：([0-9\\.\\-: ]+)");
		this.buy = CommonUtils.extractRegexGroup1(pageContent,
				"外盘：([0-9\\.\\-: ]+)");
	}

	public static StockState get(String stockId) {
		return new StockState(stockId);
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	@Override
	public String toString() {
		return "StockState [stockId=" + stockId + ", valid=" + valid
				+ ", currentPrice=" + currentPrice + ", deltaPrice="
				+ deltaPrice + ", deltaPercent=" + deltaPercent
				+ ", turnoverRate=" + turnoverRate + ", dateTime=" + dateTime
				+ ", closingPriceYesterday=" + closingPriceYesterday
				+ ", openingPrice=" + openingPrice + ", topPrice=" + topPrice
				+ ", lowestPrice=" + lowestPrice + ", turnover=" + turnover
				+ ", volume=" + volume + ", relativeRate=" + relativeRate
				+ ", priceEarningsRatio=" + priceEarningsRatio + ", sell="
				+ sell + ", buy=" + buy + "]";
	}

}
