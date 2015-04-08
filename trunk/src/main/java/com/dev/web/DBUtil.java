package com.dev.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		String sql = "CREATE TABLE stock(id int, name  char(10))";
		execute(sql);
	}

	public static void execute(String sql) {
		Connection connection = null;
		Statement statement = null;
		try {
			String dbSourceUrl = "jdbc:h2:./stockdb";
			connection = DriverManager.getConnection(dbSourceUrl);
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.closeQuietly(statement);
			CommonUtils.closeQuietly(connection);
		}
	}

	public static void executeQuery(String querySql) {
		Connection connection = null;
		Statement statement = null;
		try {
			String dbSourceUrl = "jdbc:h2:./stockdb";
			connection = DriverManager.getConnection(dbSourceUrl);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(querySql);
			showResultSet(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.closeQuietly(statement);
			CommonUtils.closeQuietly(connection);
		}
	}

	private static void showResultSet(ResultSet resultSet) throws SQLException {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int colCnt = resultSetMetaData.getColumnCount();
		for (int i = 1; i <= colCnt; i++) {
			System.out.print(resultSetMetaData.getColumnLabel(i) + "\t");
		}
		System.out.println();
		resultSet.beforeFirst();
		while (resultSet.next()) {
			for (int i = 1; i <= colCnt; i++) {
				System.out.print(resultSet.getString(i) + "\t");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// DBUtil.init();
		// DBUtil.executeQuery("select TABLE_NAME from  INFORMATION_SCHEMA.tables");
		DBUtil.execute("insert into stock values(600035,'楚天高速')");
		DBUtil.executeQuery("select * from  stock");
	}

	public static String buildInsert() {
		return null;
	}
}
