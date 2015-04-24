package com.dev.web.model;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.dev.web.downloader.FundFlowDownloader;
import com.stock.tools.CsvCombiner;

public class GenFundFlow {
	public static void main(String[] args) {
//		FundFlowDownloader.main(null);
//		FundFlowParser.main(null);
		File csvFolder = new File("FundFlowCsv");
		combineCsvFile(csvFolder);
	}

	private static void combineCsvFile(File csvFolder) {
		List<File> fileList = new ArrayList<File>((int) csvFolder.length());
		for (File csvFile : csvFolder.listFiles()) {
			fileList.add(csvFile);
		}
		CsvCombiner csvCombiner = new CsvCombiner() {

		};
		csvCombiner.setCharsetForRead(Charset.forName("GBK"));
		csvCombiner.setCharsetForWrite(Charset.forName("GBK"));
		csvCombiner.combine(fileList, new File("fundflow.csv"));
	}
}
