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

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.dev.web.StockException;

public abstract class Downloader {

	private final List<String> stockIdList;
	private final File saveToFolder;
	private final String fileExtension;

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
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		List<Future<File>> futureList = new ArrayList<Future<File>>();
		for (String stockId : stockIdList) {
			URL downloadUrl = makeDownloadUrl(stockId);
			File saveToFilename = makeSaveToFile(stockId);
			Future<File> future = newFixedThreadPool.submit(new DownloadThread(
					downloadUrl, saveToFilename));
			futureList.add(future);
		}
		newFixedThreadPool.shutdown();
		for (Future<File> future : futureList) {
			try {
				File file = future.get();
				System.out.println(file.getAbsolutePath());
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}

	}

	protected File makeSaveToFile(String stockId) {
		return new File(saveToFolder, stockId + fileExtension);
	}

	protected abstract URL makeDownloadUrl(String stockId);

	static class DownloadThread implements Callable<File> {

		private final URL downloadUrl;
		private final File saveToFile;
		private Exception lastException;

		public DownloadThread(URL downloadUrl, File saveToFile) {
			this.downloadUrl = downloadUrl;
			this.saveToFile = saveToFile;
		}

		public File call() throws Exception {
			int retryTimes = 2;
			while (retryTimes-- > 0) {
				if (successfulDownloadUrlToFile()) {
					break;
				}
			}
			if (lastException != null) {
				throw new StockException(String.format("Download '%s' failed!",
						downloadUrl), lastException);
			}
			return saveToFile;
		}

		private boolean successfulDownloadUrlToFile() {
			lastException = null;
			InputStream in = null;
			OutputStream out = null;
			try {
				URLConnection openConnection = downloadUrl.openConnection();
				openConnection.setConnectTimeout(2000);
				openConnection.setReadTimeout(20000);
				in = openConnection.getInputStream();
				out = new FileOutputStream(saveToFile);
				IOUtils.copy(in, out);
				return true;
			} catch (Exception e) {
				this.lastException = e;
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
			return false;
		}
	}
}
