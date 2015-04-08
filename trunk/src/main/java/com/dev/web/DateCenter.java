package com.dev.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
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
public class DateCenter {

	private DateCenter() {
	}

	public static String getCurrentGMT8DateStr() throws MalformedURLException,
			IOException, ParseException {
		Date date = getCurrentDatetime();
		SimpleDateFormat dateFormatForGMT8 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
		dateFormatForGMT8.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String formatedDate = dateFormatForGMT8.format(date);
		return formatedDate;
	}

	public static Date getCurrentDatetime() throws MalformedURLException,
			IOException, ParseException {
		URL url = new URL("http://m.baidu.com");
		URLConnection urlConnection = url.openConnection();
		SimpleDateFormat dateFormatForBaidu = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		String dateHeaderField = urlConnection.getHeaderFields().get("Date")
				.get(0);
		Date date = dateFormatForBaidu.parse(dateHeaderField);
		return date;
	}
}
