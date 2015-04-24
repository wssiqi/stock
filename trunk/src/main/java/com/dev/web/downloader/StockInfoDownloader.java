package com.dev.web.downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.dev.web.StockException;
import com.dev.web.model.Stocks;

public class StockInfoDownloader extends Downloader {

	private final String startDate;
	private final String endDate;

	public StockInfoDownloader(List<String> stockIdList, String startDate,
			String endDate) {
		super(stockIdList, new File("StockInfo"), ".csv");
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	protected URL makeDownloadUrl(String stockId) {
		try {
			String csvDownloadUrl = String
					.format("http://quotes.money.163.com/service/chddata.html?"
							+ "code=%s&start=%s&"
							+ "end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP",
							getCodeWithPrefix(stockId), startDate, endDate);
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
