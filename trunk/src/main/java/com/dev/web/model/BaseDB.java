package com.dev.web.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dev.web.CommUtils;
import com.dev.web.DBTable;
import com.dev.web.StockException;

public class BaseDB {
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(BaseDB.class).warn(e.getMessage(), e);
        }
    }

    public void execute(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            String dbSourceUrl = getConnectionUrl();
            connection = DriverManager.getConnection(dbSourceUrl);
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException(String.format("执行SQL失败:'%s'", sql), e);
        } finally {
            CommUtils.closeQuietly(statement);
            CommUtils.closeQuietly(connection);
        }
    }

    public DBTable executeQuery(String querySql) {
        Connection connection = null;
        Statement statement = null;
        try {
            String dbSourceUrl = getConnectionUrl();
            connection = DriverManager.getConnection(dbSourceUrl);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            DBTable dbTable = new DBTable();
            dbTable.parseResultSet(resultSet);
            return dbTable;
        } catch (Exception e) {
            throw new RuntimeException(String.format("执行SQL失败:'%s'", querySql), e);
        } finally {
            CommUtils.closeQuietly(statement);
            CommUtils.closeQuietly(connection);
        }
    }

    protected String getConnectionUrl() {
        return "jdbc:h2:./stockdb";
    }

    public static void main(String[] args) {
        BaseDB baseDB = new BaseDB();
        baseDB.execute("DELETE FROM STOCK WHERE ID='600035'");
        baseDB.executeQuery("SELECT * FROM  STOCK");
    }

    public String buildInsert(String tableName, List<String> columnNames, List<String> values) {
        checkParameters(tableName, columnNames, values);
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ").append(tableName).append(" (");
        appendStringList(columnNames, buffer);
        buffer.append(") VALUES(");
        appendStringList(values, buffer);
        buffer.append(")");
        return buffer.toString();
    }

    public String buildSelect(String tableName, List<String> columnNames) {
        checkParameters(tableName, columnNames);
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT ");
        if (columnNames == null || columnNames.isEmpty()) {
            buffer.append('*');
        } else {
            for (String columnName : columnNames) {
                buffer.append(columnName).append(',');
            }
            buffer.setLength(buffer.length() - 1);
        }
        buffer.append(" FROM ").append(tableName);
        return buffer.toString();
    }

    private void checkParameters(String tableName, List<String> columnNames) {
        if (StringUtils.isEmpty(StringUtils.trim(tableName))) {
            throw new StockException("Table name can not be null or empty.");
        }
        if (columnNames == null || columnNames.isEmpty()) {
            throw new StockException("Column names can not be null or empty.");
        }
    }

    private void appendStringList(List<String> stringList, StringBuffer buffer) {
        for (String string : stringList) {
            buffer.append(string).append(',');
        }
        buffer.setLength(buffer.length() - 1);// remove last ,
    }

    private void checkParameters(String tableName, List<String> columnNames, List<String> values) {
        checkParameters(tableName, columnNames);
        if (values == null || values.isEmpty()) {
            throw new StockException("values can not be null or empty.");
        }
        if (columnNames.size() != values.size()) {
            throw new StockException("Column names size and values size not equal.");
        }
    }
}
