package com.dev.web.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.Charsets;
import org.apache.log4j.Logger;
import org.h2.tools.Csv;

import com.dev.web.DBTable;

public class InfoDB extends BaseDB {

	public InfoDB() {
		DBTable resultTable = executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='STOCKINFO'");
		if (resultTable.getRowList().isEmpty()) {
			init();
		}
	}

	private void init() {
		execute("CREATE TABLE STOCKINFO(ID CHAR(10) PRIMARY KEY, NAME CHAR(20), TOTALVALUE DECIMAL(20,2), TOTALVOL DECIMAL(20,0), TOTALSTOCK DECIMAL(20,0), MARKETSTOCK DECIMAL(20,0))");
		List<String> allStockId = Stocks.getAllStockId();
		for (String stockId : allStockId) {
			String insertSql = buildInsert("STOCKINFO", Arrays.asList("ID"),
					Arrays.asList(stockId));
			execute(insertSql);
			System.out.println(insertSql);
		}
	}

	@Override
	protected String getConnectionUrl() {
		return "jdbc:h2:./stockinfodb";
	}

	public static void main(String[] args) {
		Csv csv = new Csv();
		try {
			ResultSet read = csv.read(new InputStreamReader(
					new FileInputStream("daily/000001.csv"), "GBK"), null);
			DBTable table = new DBTable();
			table.parseResultSet(read);
			table.writeToCSV(new File("test.csv"), Charsets.UTF_8);
			System.out.println(read);
			CsvResultSet set = new CsvResultSet();
			set.addColumn(new String[] { "A,test\"", "\r\nB", "C" });
			Csv csv2 = new Csv();
			csv2.write("test1.csv", set, "");
			csv2.close();
		} catch (Exception e) {
			Logger.getLogger(InfoDB.class).error(e.getMessage(), e);
		} finally {
			csv.close();
		}

	}

}
