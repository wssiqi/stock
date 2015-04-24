package com.dev.web.model;

import java.sql.Types;

import org.h2.tools.SimpleResultSet;

public class CsvResultSet extends SimpleResultSet {

    public static void main(String[] args) {
        new CsvResultSet().addColumn("abc");
    }
    public void addColumns(String... columnNames) {
        for (String columnName : columnNames) {
            addColumn(columnName);
        }
    }

    public void addColumn(String columnName) {
        addColumn(columnName, Types.VARCHAR, Integer.MAX_VALUE, 0);
    }
}
