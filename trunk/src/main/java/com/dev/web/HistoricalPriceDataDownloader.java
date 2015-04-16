package com.dev.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.dev.web.model.Stocks;

public class HistoricalPriceDataDownloader {

	static Map<String, String> PREFIX_EXTRA_MAP = new HashMap<String, String>();
	static {
		PREFIX_EXTRA_MAP.put("0", "1");
		PREFIX_EXTRA_MAP.put("3", "1");
		PREFIX_EXTRA_MAP.put("6", "0");
	}

	public static void main(String[] args) {
		downloadStockHistory(Stocks.getAllStockId());
	}

	public static void downloadStockHistory(String stockId) throws Exception {
		ArrayList<String> stockList = new ArrayList<String>();
		stockList.add(stockId);
		downloadStockHistory(stockList);
	}

	public static void downloadStockHistory(List<String> stockIdList) {
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		List<Future<DownloadTask>> futureList = new ArrayList<Future<DownloadTask>>();
		for (String stockId : stockIdList) {
			File csvFile = new File("daily/" + stockId + ".csv");
			if (csvFile.exists()) {
				continue;
			}
			String stockCode = PREFIX_EXTRA_MAP.get(stockId.substring(0, 1));
			if (stockCode == null) {
				Logger.getLogger(HistoricalPriceDataDownloader.class).warn("Skip " + stockId);
				continue;
			}
			Future<DownloadTask> submit = threadPool.submit(new DownloadTask(stockId, csvFile));
			futureList.add(submit);
		}
		threadPool.shutdown();
		for (int i = 0; i < futureList.size(); i++) {
			try {
				DownloadTask downloadTask = futureList.get(i).get();
				System.out.println(String.format("downloading %s %2.2f", downloadTask.getStockId(), 100.0f * i
				        / futureList.size()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class DownloadTask implements Callable<DownloadTask> {
		private String stockId;
		private File saveToFile;

		public DownloadTask(String stockId, File saveToFile) {
			this.stockId = stockId;
			this.saveToFile = saveToFile;
		}

		public DownloadTask call() throws Exception {
			for (int i = 0; i < 5; i++) {
				try {
					download();
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return this;
		}

		public String getStockId() {
			return stockId;
		}

		private void download() {
			InputStream in = null;
			OutputStream out = null;
			try {
				String currentDate = DateUtils.getCurrentDateGMT8("yyyyMMdd");
				String stockCode = PREFIX_EXTRA_MAP.get(stockId.substring(0, 1));
				if (stockCode == null) {
					Logger.getLogger(getClass()).warn("Skip " + stockId);
					return;
				}
				stockCode = stockCode + stockId;
				String csvDownloadUrl = String
				        .format("http://quotes.money.163.com/service/chddata.html?"
				                + "code=%s&start=20150101&"
				                + "end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP",
				                stockCode, currentDate);
				URL url = new URL(csvDownloadUrl);
				URLConnection urlConnection = url.openConnection();
				System.out.println(urlConnection.getHeaderFields());
				urlConnection.setConnectTimeout(5000);
				urlConnection.setReadTimeout(20000);
				in = urlConnection.getInputStream();
				File fileDir = saveToFile.getParentFile();
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				out = new FileOutputStream(saveToFile);
				IOUtils.copy(in, out);
			} catch (Exception e) {
				throw new RuntimeException(String.format("download failed... %s", stockId), e);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}
}
