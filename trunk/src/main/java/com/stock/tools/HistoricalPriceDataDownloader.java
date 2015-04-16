package com.stock.tools;

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

import com.dev.web.DateUtils;
import com.dev.web.Stocks;

public class HistoricalPriceDataDownloader {

	static Map<String, String> prefixDigitMap = new HashMap<String, String>();
	static {
		prefixDigitMap.put("0", "1");
		prefixDigitMap.put("2", "1");
		prefixDigitMap.put("3", "1");
		prefixDigitMap.put("6", "0");
		prefixDigitMap.put("9", "0");
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
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		List<Future<DownloadTask>> futureList = new ArrayList<Future<DownloadTask>>();
		for (String stockId : stockIdList) {
			Future<DownloadTask> submit = threadPool.submit(new DownloadTask(
					stockId, new File("daily/" + stockId + ".csv")));
			futureList.add(submit);
		}
		threadPool.shutdown();
		for (int i = 0; i < futureList.size(); i++) {
			try {
				futureList.get(i).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(String.format("%s %2.2f", stockIdList.get(i),
					100.0f * i / futureList.size()));
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
			return null;
		}

		private void download() {
			InputStream in = null;
			OutputStream out = null;
			try {
				;
				String prefixStr = prefixDigitMap.get(stockId.charAt(0) + "");
				if (prefixStr == null) {
					throw new RuntimeException("找不到匹配的股票前缀：" + stockId);
				}
				String currentDate = DateUtils.getCurrentDateGMT8("yyyyMMdd");
				String csvDownloadUrl = String
						.format("http://quotes.money.163.com/service/chddata.html?"
								+ "code=%s%s&start=20050101&"
								+ "end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP",
								prefixStr, stockId, currentDate);
				System.out.println("Downloading: " + csvDownloadUrl);
				URL url = new URL(csvDownloadUrl);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setConnectTimeout(5000);
				urlConnection.setReadTimeout(20000);
				in = urlConnection.getInputStream();
				File fileDir = saveToFile.getParentFile();
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				out = new FileOutputStream(saveToFile);
				IOUtils.copy(in, out);
				System.out.println("Downloaded : " + csvDownloadUrl);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}
}
