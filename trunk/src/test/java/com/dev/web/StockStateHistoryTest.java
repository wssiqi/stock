package com.dev.web;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.h2.tools.Csv;
import org.h2.tools.SimpleResultSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StockStateHistoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		// List<String> stockIdList = Stocks.getAllStockId();
		// StockDailyHistoryDownloader.downloadStockHistory(stockIdList);
		// SimpleResultSet srs = new SimpleResultSet();
		File file = new File("daily/000001.csv");
		File outfile = new File("daily-utf8/000001.csv");
		CommonUtils.mkdirs(outfile);
		InputStreamReader inReader = new InputStreamReader(new FileInputStream(
				file), Charset.forName("GBK"));
		OutputStreamWriter outWriter = new OutputStreamWriter(
				new FileOutputStream(outfile), Charset.forName("UTF-8"));
		IOUtils.copy(inReader, outWriter);
		IOUtils.closeQuietly(inReader);
		IOUtils.closeQuietly(outWriter);
//		new Csv().read(reader, colNames)
		DBTable dbTable = DBUtil
				.executeQuery("select * from csvread('daily-utf8/000001.csv')");
		DBUtil.execute("create table daily000001 as select * from csvread('daily-utf8/000001.csv')");
		System.out.println(dbTable);
	}
}
