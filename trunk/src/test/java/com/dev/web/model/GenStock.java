package com.dev.web.model;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.dev.web.downloader.FundFlowDownloader;
import com.dev.web.downloader.StockInfoDownloader;
import com.stock.tools.CsvCombiner;

public class GenStock {
	public static void main(String[] args) {
		new StockInfoDownloader(Stocks.getAllStockId(), "20150423", "20150423")
				.download();
		combineCsvFile(new File("StockInfo"));
	}

	private static void combineCsvFile(File csvFolder) {
		List<File> fileList = new ArrayList<File>((int) csvFolder.length());
		for (File csvFile : csvFolder.listFiles()) {
			fileList.add(csvFile);
		}
		CsvCombiner csvCombiner = new CsvCombiner();
		csvCombiner.setCharsetForRead(Charset.forName("GBK"));
		csvCombiner.setCharsetForWrite(Charset.forName("GBK"));
		csvCombiner.combine(fileList, new File("stockinfo.csv"));
	}
}
