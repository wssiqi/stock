package com.dev.web;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

public class CommonUtils {

	private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);
	private static final char EXTENSION_SEPERATOR = '.';

	public static String showReturnAndNextLineChar(String asText) {
		return asText.replaceAll("\\r", "\\\\r").replaceAll("\\n", "\\\\n");
	}

	public static String skipReturnAndNextLineChar(String asText) {
		return asText.replaceAll("[\\r\\n]", "");
	}

	public static void closeQuietly(Object closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.getClass().getMethod("close").invoke(closeable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getWebPageContent(String htmlUrl) {
		try {
			return Jsoup.parse(new URL(htmlUrl), 5000).text();
		} catch (Exception e) {
			throw new RuntimeException(String.format("获取网址'%s'失败~", htmlUrl), e);
		}
	}

	public static String extractRegexGroup1(String string, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(string);
		if (matcher.find()) {
			if (matcher.groupCount() > 0) {
				return matcher.group(1);
			}
		}
		throw new RuntimeException(String.format(
				"ExtractRegexGroup1 error, string='%s', regex='%s'", string,
				regex));
	}

	public static void mkdirsForFile(File file) {
		if (file == null) {
			throw new RuntimeException("创建文件夹'null'失败!");
		}
		mkdirs(file.getParentFile());
	}

	public static void mkdirs(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(String.format(
						"创建文件夹'%s'失败，请检查权限是否正确!", dir.getAbsolutePath()));
			}
		} else if (!dir.isDirectory()) {
			throw new RuntimeException(String.format("创建文件夹'%s'失败，存在一个同名的文件!",
					dir.getAbsolutePath()));
		}
	}

	public static String getFileNameWithoutExtension(File file) {
		if (file == null) {
			return "";
		}
		String fileName = file.getName();
		int extensionSeperatorIndex = fileName.lastIndexOf(EXTENSION_SEPERATOR);
		if (extensionSeperatorIndex == -1) {
			return fileName;
		}
		return fileName.substring(0, extensionSeperatorIndex);
	}

	public static String getFileExtension(File file) {
		if (file == null) {
			return "";
		}
		String fileName = file.getName();
		int extensionSeperatorIndex = fileName.lastIndexOf(EXTENSION_SEPERATOR);
		if (extensionSeperatorIndex == -1) {
			return "";
		}
		return fileName.substring(extensionSeperatorIndex + 1);
	}

	public static String getAbsolutePath(File file) {
		if (file == null) {
			return null;
		}
		try {
			return file.getAbsolutePath();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return file.getPath();
		}
	}

	public static void setProxy() {
		// System.setProperty("http.proxyHost", "10.144.1.10");
		// System.setProperty("http.proxyPort", "8080");
	}
}
