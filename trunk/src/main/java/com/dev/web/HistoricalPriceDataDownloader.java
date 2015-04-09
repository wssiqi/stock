package com.dev.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;

public class HistoricalPriceDataDownloader {
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
				String currentDate = DateUtils
						.getCurrentDateGMT8("yyyyMMdd");
				String csvDownloadUrl = String
						.format("http://quotes.money.163.com/service/chddata.html?"
								+ "code=1%s&start=19900101&"
								+ "end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;VOTURNOVER;VATURNOVER",
								stockId, currentDate);
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
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}
}
