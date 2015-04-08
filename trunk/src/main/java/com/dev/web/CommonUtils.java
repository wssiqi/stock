package com.dev.web;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CommonUtils {
	private static Logger logger = Logger.getLogger(CommonUtils.class);

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
			System.setProperty("http.proxyHost", "10.144.1.10");
			System.setProperty("http.proxyPort", "8080");
			Document document = Jsoup.parse(new URL(htmlUrl), 5000);
			return document.text();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String extractRegexGroup1(String string, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(string);
		if (matcher.find()) {
			if (matcher.groupCount() > 1) {
				return matcher.group(1);
			}
		}
		return "";
	}
}
