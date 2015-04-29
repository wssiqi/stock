package com.dev.web.downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.dev.web.StockException;
import com.dev.web.model.Stocks;

public class FundFlowEastMoneyDownloader extends Downloader {

	public FundFlowEastMoneyDownloader(List<String> stockIdList) {
		super(stockIdList, new File("FundFlow1"), ".html");
	}

	@Override
	protected URL makeDownloadUrl(String stockId) {
		try {
			return new URL(String.format(
					"http://data.eastmoney.com/zjlx/%s.html", stockId));
		} catch (MalformedURLException e) {
			throw new StockException(e);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			new FundFlowEastMoneyDownloader(Stocks.getAllStockId()).download();
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
