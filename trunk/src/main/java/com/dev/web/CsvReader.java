package com.dev.web;

import java.io.File;

public class CsvReader {

	private String querySql;

	public CsvReader(File csvFile) {
		querySql = String.format("SELECT * FROM CSVREAD('%s');",
				csvFile.getAbsolutePath());
		System.out.println(querySql);
	}

	public DBTable readDataAsTable() {
		return DBUtil.executeQuery(querySql);
	}

}
