package com.dev.web;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

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
}
