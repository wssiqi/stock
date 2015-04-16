package com.dev.web;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期中心，实时获取互联网时间
 * 
 * @author dev
 * 
 */
public class DateUtils {

	private DateUtils() {
	}

	/**
	 * 2015-04-09 11:02:59
	 * 
	 * @return
	 */
	public static String getCurrentGMT8Datetime() {
		return getCurrentDateGMT8("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 2015-04-09
	 * 
	 * @return
	 */
	public static String getCurrentDateGMT8() {
		return getCurrentDateGMT8("yyyy-MM-dd");
	}

	public static String getYestorday() {
		Date date = getCurrentDatetimeGMT8();
		long timeInMsFrom1990 = date.getTime();
		long timeFor24H = 24 * 60 * 60 * 1000;
		Date yestordayDate = new Date(timeInMsFrom1990 - timeFor24H);
		return getDateString("yyyyMMdd", yestordayDate);
	}

	public static Date getCurrentDatetimeGMT8() {
		try {
			URL url = new URL("http://m.baidu.com");
			URLConnection urlConnection = url.openConnection();
			SimpleDateFormat dateFormatForBaidu = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			String dateHeaderField = urlConnection.getHeaderFields()
					.get("Date").get(0);
			Date date = dateFormatForBaidu.parse(dateHeaderField);
			return date;
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String getCurrentDateGMT8(String pattern) {
		Date date = getCurrentDatetimeGMT8();
		return getDateString(pattern, date);
	}

	private static String getDateString(String pattern, Date date) {
		SimpleDateFormat dateFormatForGMT8 = new SimpleDateFormat(pattern,
				Locale.US);
		dateFormatForGMT8.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String formatedDate = dateFormatForGMT8.format(date);
		return formatedDate;
	}
}
