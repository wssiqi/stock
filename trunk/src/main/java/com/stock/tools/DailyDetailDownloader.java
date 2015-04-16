package com.stock.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
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

public class DailyDetailDownloader {

	static Map<String, String> prefixDigitMap = new HashMap<String, String>();
	static {
		prefixDigitMap.put("0", "1");
		prefixDigitMap.put("2", "1");
		prefixDigitMap.put("3", "1");
		prefixDigitMap.put("6", "0");
		prefixDigitMap.put("9", "0");
	}

	public static void main(String[] args) {
		List<String> dateList = Arrays.asList("20150406", "20150407",
				"20150408", "20150409", "20150410");
		for (String date : dateList) {
			downloadDetail(Stocks.getAllStockId(), date);
		}
	}

	public static void downloadStockHistory(String stockId, String date)
			throws Exception {
		ArrayList<String> stockList = new ArrayList<String>();
		stockList.add(stockId);
		downloadDetail(stockList, date);
	}

	public static void downloadDetail(List<String> stockIdList, String date) {
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		List<Future<DownloadTask>> futureList = new ArrayList<Future<DownloadTask>>();
		for (String stockId : stockIdList) {
			Future<DownloadTask> submit = threadPool.submit(new DownloadTask(
					stockId, date, new File(date + "/" + stockId + ".xls")));
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
		private final String date;

		public DownloadTask(String stockId, String date, File saveToFile) {
			this.stockId = stockId;
			this.date = date;
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
				String year = date.substring(0, 4);
				String csvDownloadUrl = String.format(
						"http://quotes.money.163.com/cjmx/%s/%s/%s%s.xls",
						year, date, prefixStr, stockId);
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
