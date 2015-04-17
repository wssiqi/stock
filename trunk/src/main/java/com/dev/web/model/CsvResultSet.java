package com.dev.web.model;

import java.sql.Types;

import org.h2.tools.SimpleResultSet;

public class CsvResultSet extends SimpleResultSet {

    public void addColumn(String[] columnNames) {
        for (String columnName : columnNames) {
            addColumn(columnName);
        }
    }

    public void addColumn(String columnName) {
        addColumn(columnName, Types.VARCHAR, Integer.MAX_VALUE, 0);
    }
}
