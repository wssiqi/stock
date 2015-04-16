package com.dev.web.downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import com.dev.web.DateUtils;
import com.dev.web.StockException;
import com.dev.web.Stocks;

public class StockInfoDownloader extends Downloader {

	public static void main(String[] args) {
		new StockInfoDownloader(Stocks.getAllStockId()).download();
	}

	public StockInfoDownloader(List<String> stockIdList) {
		super(stockIdList, new File("StockInfo"), ".csv");
	}

	@Override
	protected URL makeDownloadUrl(String stockId) {
		try {
			String yestorday = DateUtils.getYestorday();
			String today = DateUtils.getCurrentDateGMT8("yyyyMMdd");
			String csvDownloadUrl = String
					.format("http://quotes.money.163.com/service/chddata.html?"
							+ "code=%s&start=%s&"
							+ "end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP",
							getCodeWithPrefix(stockId), yestorday, today);
			System.out.println(csvDownloadUrl);
			return new URL(csvDownloadUrl);
		} catch (MalformedURLException e) {
			throw new StockException(e);
		}
	}

	private String getCodeWithPrefix(String stockId) {
		String prefix = "";
		char firstDigit = stockId.charAt(0);
		switch (firstDigit) {
		case '0':
		case '3':
			prefix = "1";
			break;
		case '6':
			prefix = "0";
			break;
		default:
			throw new StockException(String.format(
					"Stock code '%s' not support.", stockId));
		}
		return prefix + stockId;
	}

}
