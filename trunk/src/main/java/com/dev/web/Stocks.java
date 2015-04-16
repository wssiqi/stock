package com.dev.web;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Stocks {
	private static Logger logger = Logger.getLogger(Stocks.class);

	public static List<String> getAllStockId() {
		List<String> stockIdList = getStockIdList();
		Collections.sort(stockIdList);
		return Collections.unmodifiableList(stockIdList);
	}

	private static List<String> getStockIdList() {
//		CommonUtils.setProxy();
		Document stockListDoc = null;
		try {
			logger.warn("尝试从网络获取列表...");
			stockListDoc = tryFetchFromWeb();
			logger.warn("成功从网络获取列表...");
		} catch (Exception e) {
			logger.warn(e.getMessage());
			logger.warn("尝试从本地获取列表...");
			stockListDoc = tryFetchFromLocal();
		}
		Elements stockElements = stockListDoc.select("div#quotesearch ul li");
		List<String> stockIdList = new ArrayList<String>();
		for (Element stockElement : stockElements) {
			String stockInfo = StringUtils.trim(stockElement.text());
			String stockId = getStockId(stockInfo);
			if (isStockValid(stockId)) {
				stockIdList.add(stockId);
			}
		}
		logger.info("列表总数：" + stockIdList.size());
		return stockIdList;
	}

	private static boolean isStockValid(String stockId) {
		if (StringUtils.isEmpty(stockId)) {
			return false;
		}
		switch (stockId.charAt(0)) {
		case '0':
		case '3':
		case '6':
			return true;
		default:
			return false;
		}
	}

	/**
	 * 
	 * @param stockInfo
	 * @param charBegin
	 *            exclude
	 * @param charEnd
	 *            exclude
	 * @return
	 */
	private static String getStockId(String stockInfo) {
		int beginIndex = Math.max(0, stockInfo.indexOf('(') + 1);
		int endIndex = Math.max(0, stockInfo.indexOf(')'));
		return stockInfo.substring(beginIndex, endIndex);
	}

	private static Document tryFetchFromWeb() {
		String stockListUrl = "http://quote.eastmoney.com/stocklist.html";
		try {
			return Jsoup.parse(new URL(stockListUrl), 10000);
		} catch (Exception e) {
			throw new RuntimeException(String.format("从'%s'获取股票列表失败!",
					stockListUrl), e);
		}
	}

	private static Document tryFetchFromLocal() {
		File localFile = new File("stocklist.html");
		try {
			return Jsoup.parse(localFile, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(String.format("从'%s'获取股票列表失败!",
					localFile.getAbsolutePath()), e);
		}
	}

	public static void main(String[] args) {
		System.out.println(Stocks.getAllStockId());
	}
}
