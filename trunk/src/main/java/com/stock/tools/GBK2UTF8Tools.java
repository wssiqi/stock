package com.stock.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.dev.web.DBUtil;

public class GBK2UTF8Tools {
	public static void main(String[] args) {
		File srcDir = new File("daily");
		File dstDir = new File("daily-utf8");
//		convertFileEncoding(srcDir, dstDir);
		importCsvDataToTable(dstDir);
	}

	private static void importCsvDataToTable(File dstDir) {
		File[] csvFiles = dstDir.listFiles();
		for (int i = 0; i < csvFiles.length; i++) {
			File utf8CsvFile = csvFiles[i];
			String csvPath = utf8CsvFile.getAbsolutePath();
			System.out.println(String.format("%4s/%4s:%s", i, csvFiles.length,
					csvPath));
			try {
				String importSql = String
						.format("insert into dailyrecordcsv("
								+ "日期,股票代码,名称,收盘价,最高价,最低价,开盘价,前收盘,涨跌额,涨跌幅,换手率,成交量,成交金额,总市值,流通市值"
								+ ") select * from CSVREAD('%s')", csvPath);
				DBUtil.execute(importSql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void convertFileEncoding(File srcDir, File dstDir) {
		for (File srcFile : srcDir.listFiles()) {
			Reader in = null;
			Writer out = null;
			try {
				in = new InputStreamReader(new FileInputStream(srcFile),
						Charset.forName("GBK"));
				in = new BufferedReader(in);
				out = new OutputStreamWriter(new FileOutputStream(new File(
						dstDir, srcFile.getName())), Charset.forName("UTF-8"));
				out = new BufferedWriter(out);
				IOUtils.copy(in, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}
}
