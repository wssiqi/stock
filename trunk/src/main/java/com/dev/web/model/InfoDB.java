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
		DBTable resultTable = executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='STOCKHIS'");
		if (resultTable.getRowList().isEmpty()) {
			init();
		}
	}

	public void init() {
		execute("CREATE TABLE STOCKHIS( code CHAR(10), "
				+ "date CHAR(10), "
				+ "ePrice DECIMAL(20,4), "
				+ "rate DECIMAL(20,4), "
				+ "mainIn DECIMAL(20,4), "
				+ "mainInRate DECIMAL(20,4), "
				+ "HugeIn DECIMAL(20,4), "
				+ "HugeInRate DECIMAL(20,4), "
				+ "BigIn DECIMAL(20,4), "
				+ "BigInRate DECIMAL(20,4), "
				+ "MidIn DECIMAL(20,4), "
				+ "MidInRate DECIMAL(20,4), SmallIn DECIMAL(20,4), SmallInRate DECIMAL(20,4))");
	}

	@Override
	protected String getConnectionUrl() {
		// return "jdbc:h2:./stockinfo";
		return "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
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
			set.addColumns(new String[] { "A,test\"", "\r\nB", "C" });
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
