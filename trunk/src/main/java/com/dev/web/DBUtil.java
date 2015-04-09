package com.dev.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		String sql = "CREATE TABLE stock(id char(10) primary key, name char(20))";
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

	public static DBTable executeQuery(String querySql) {
		Connection connection = null;
		Statement statement = null;
		try {
			String dbSourceUrl = "jdbc:h2:./stockdb";
			connection = DriverManager.getConnection(dbSourceUrl);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(querySql);
			return showResultSet(resultSet);
		} catch (Exception e) {
			throw new RuntimeException(String.format("执行SQL失败:'%s'", querySql),
					e);
		} finally {
			CommonUtils.closeQuietly(statement);
			CommonUtils.closeQuietly(connection);
		}
	}

	private static DBTable showResultSet(ResultSet resultSet)
			throws SQLException {
		DBTable dbTable = new DBTable();
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int colCnt = resultSetMetaData.getColumnCount();
		for (int i = 1; i <= colCnt; i++) {
			dbTable.addColumnName(resultSetMetaData.getColumnLabel(i));
		}
		resultSet.beforeFirst();
		while (resultSet.next()) {
			List<String> rowDataList = new ArrayList<String>();
			for (int i = 1; i <= colCnt; i++) {
				rowDataList.add(resultSet.getString(i));
			}
			dbTable.addRowData(rowDataList);
		}
		return dbTable;
	}

	public static void main(String[] args) {
		DBUtil.init();
		DBUtil.execute("delete from stock where id='600035'");
		DBUtil.executeQuery("select * from  stock");
	}

	public static String buildInsert() {
		return null;
	}
}
