package com.dev.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class StockState {

	private final String stockId;
	private boolean valid;
	private Object currentPrice;
	private String deltaPrice;
	private String deltaPercent;
	private String turnoverRate;
	private String dateTime;

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
	}

	public static StockState get(String stockId) {
		return new StockState(stockId);
	}

	public String getCurrentPrice() {
		System.setProperty("http.proxyHost", "10.144.1.10");
		System.setProperty("http.proxyPort", "8080");
		String stockPageUrl = String.format(
				"http://wap.eastmoney.com/StockInfo.aspx?stockcode=%s&vt=0",
				stockId);
		Document document;
		try {
			document = Jsoup.parse(new URL(stockPageUrl), 5000);
			String pageText = document.text();
			System.out.println(pageText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
