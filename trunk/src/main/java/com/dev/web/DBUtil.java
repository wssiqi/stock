package com.dev.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBUtil {
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBUtil.class).warn(e.getMessage(), e);
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
            throw new RuntimeException(String.format("执行SQL失败:'%s'", sql), e);
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
            DBTable dbTable = new DBTable();
            dbTable.parseResultSet(resultSet);
            return dbTable;
        } catch (Exception e) {
            throw new RuntimeException(String.format("执行SQL失败:'%s'", querySql), e);
        } finally {
            CommonUtils.closeQuietly(statement);
            CommonUtils.closeQuietly(connection);
        }
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
