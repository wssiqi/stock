package com.dev.web.downloader;

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
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.dev.web.CommUtils;
import com.dev.web.StockException;

public abstract class Downloader {

	private final List<String> stockIdList;
	private final File saveToFolder;
	private final String fileExtension;
	static {
		CommUtils.setProxy();
	}

	public Downloader(List<String> stockIdList, File saveToFolder,
			String fileExtension) {
		this.stockIdList = stockIdList;
		this.saveToFolder = saveToFolder;
		this.fileExtension = fileExtension;
		checkParameters();
	}

	private void checkParameters() {
		if (stockIdList == null) {
			throw new StockException("stockIdList can not be null");
		}

		if (saveToFolder == null) {
			throw new StockException("saveToFolder can not be null");
		}

		if (fileExtension == null) {
			throw new StockException("fileExtension can not be null");
		}

		try {
			if (saveToFolder.exists()) {
				if (saveToFolder.isFile()) {
					throw new StockException(String.format(
							"saveToFolder '%s' is a file.",
							saveToFolder.getAbsolutePath()));
				}
			} else if (!saveToFolder.mkdirs()) {
				throw new StockException(String.format(
						"Try create saveToFolder '%s' failed.",
						saveToFolder.getAbsolutePath()));
			}
		} catch (StockException e) {
			throw e;
		} catch (Exception e) {
			throw new StockException(String.format(
					"Try create saveToFolder '%s' failed.",
					saveToFolder.getAbsolutePath()), e);
		}

	}

	public void download() {
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		List<Future<?>> taskResultList = new ArrayList<Future<?>>();
		for (String stockId : stockIdList) {
			URL downloadUrl = makeDownloadUrl(stockId);
			File saveToFilename = makeSaveToFile(stockId);
			Future<?> future = threadPool.submit(new DownloadRunnable(
					downloadUrl, stockId));
			taskResultList.add(future);
		}

		threadPool.shutdown();
		for (Future<?> future : taskResultList) {
			try {
				future.get();
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
				List<Runnable> neverExecuteTasks = threadPool.shutdownNow();
				
				throw new RuntimeException(e);
			}
		}

	}

	protected File makeSaveToFile(String stockId) {
		return new File(saveToFolder, stockId + fileExtension);
	}

	protected abstract URL makeDownloadUrl(String stockId);

	protected abstract String afterDownload(Document htmlDoc);

	static class DownloadRunnable implements Runnable {

		private final URL downloadUrl;
		private final String stockId;

		public DownloadRunnable(URL downloadUrl, String stockId) {
			this.downloadUrl = downloadUrl;
			this.stockId = stockId;
		}

		public String getStockId() {
			return stockId;
		}

		public void run() {
			try {
				Document doc = Jsoup.parse(downloadUrl, 10000);
				System.out.println(doc.outerHtml());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
