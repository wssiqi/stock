package com.dev.web;

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
}
