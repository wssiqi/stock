package com.dev.web;

import java.sql.DriverManager;

public class TableRow {
	public static void main(String[] args) {

		try {
			String url = "jdbc:h2:/data/data/" + "com.example.hello"
					+ "/data/hello" + ";FILE_LOCK=FS" + ";PAGE_SIZE=1024"
					+ ";CACHE_SIZE=8192";
			Class.forName("org.h2.Driver");
			DriverManager.getConnection("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
