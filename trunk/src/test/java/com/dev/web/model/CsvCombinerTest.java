package com.dev.web.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dev.web.DBTable;
import com.dev.web.TestUtil;
import com.stock.tools.CsvCombiner;

public class CsvCombinerTest {

	private File csvFile1;
	private File csvFile2;

	@Before
	public void copyTestDataFileToWorkDir() throws IOException {
		String csv1 = "000001.csv";
		String csv2 = "000002.csv";
		this.csvFile1 = new File(TestUtil.WS_HOME, csv1);
		this.csvFile2 = new File(TestUtil.WS_HOME, csv2);
		FileUtils.copyFile(new File(TestUtil.RESOURCE_HOME, csv1), csvFile1);
		FileUtils.copyFile(new File(TestUtil.RESOURCE_HOME, csv2), csvFile2);
	}

	@After
	public void deleteTestData() {
		FileUtils.deleteQuietly(csvFile1);
		FileUtils.deleteQuietly(csvFile2);
	}

	@Test
	public void testCombineTwoCsvFile() {
		File csvFolder = new File("StockInfo");
		List<File> fileList = new ArrayList<File>((int) csvFolder.length());
		for (File csvFile : csvFolder.listFiles()) {
			fileList.add(csvFile);
		}
		CsvCombiner csvCombiner = new CsvCombiner();
		csvCombiner.setCharsetForRead(Charset.forName("GBK"));
		csvCombiner.setCharsetForWrite(Charset.forName("GBK"));
		csvCombiner.combine(fileList, new File(TestUtil.WS_HOME, "test.csv"));
		System.out.println();
	}

	// @Test
	public void testCombiner() throws Exception {
		// CsvCombiner csvCombiner = new CsvCombiner();
		// csvCombiner.setCharsetForRead(Charset.forName("GBK"));
		// csvCombiner.setCharsetForWrite(Charset.forName("GBK"));
		// List<File> fileList = new ArrayList<File>();
		// for (File subFile : new File("StockInfo").listFiles()) {
		// fileList.add(subFile);
		// }
		// csvCombiner.combine(fileList, new File("result.csv"));
		BaseDB db = new BaseDB();
		// db.execute("CREATE TABLE TEST AS SELECT * FROM CSVREAD('result.csv')");
		// String querySql = "select distinct 日期 from TEST order by 日期";
		String querySql = "select * from (select * from TEST order by 涨跌幅) where rownum<10";
		DBTable table = db.executeQuery(querySql);
		// table.print();
		table.writeToCSV(new File("out.csv"), Charset.forName("GBK"));
		// System.out.println();
		// db.executeQuery("select DISTINCT ");
	}
}
