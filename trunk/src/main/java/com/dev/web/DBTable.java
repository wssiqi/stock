package com.dev.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.h2.tools.SimpleResultSet;

public class DBTable {
    private static final int BUFFER_64K = 64 * 1024;
    Map<String, Integer> columnNameMap = new LinkedHashMap<String, Integer>();
    List<List<String>> rowList = new ArrayList<List<String>>();

    public Map<String, Integer> getColumnNameMap() {
        return columnNameMap;
    }

    public void addColumnName(String columnName) {
        columnNameMap.put(columnName, columnNameMap.size());
    }

    public void addRowData(List<String> rowData) {
        rowList.add(rowData);
    }

    public List<String> getColumnValues(String columnName) {
        Integer columnIndex = columnNameMap.get(columnName);
        if (columnIndex == null) {
            throw new RuntimeException(String.format("Column name '%s' not found. Valid is '%s'", columnName,
                    columnNameMap.keySet()));
        }
        List<String> columnValues = new ArrayList<String>(rowList.size());
        for (List<String> rowData : rowList) {
            columnValues.add(rowData.get(columnIndex));
        }
        return columnValues;
    }

    public List<List<String>> getRowList() {
        return Collections.unmodifiableList(rowList);
    }

    public void parseResultSet(ResultSet resultSet) {
        clear();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int colCnt = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= colCnt; i++) {
                addColumnName(resultSetMetaData.getColumnLabel(i));
            }
            if (!(resultSet instanceof SimpleResultSet)) {
                resultSet.beforeFirst();
            }
            while (resultSet.next()) {
                List<String> rowDataList = new ArrayList<String>();
                for (int i = 1; i <= colCnt; i++) {
                    rowDataList.add(resultSet.getString(i));
                }
                addRowData(rowDataList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clear() {
        columnNameMap.clear();
        rowList.clear();
    }

    public void writeToCSV(File csvFile) {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(csvFile), Charsets.UTF_8);
            out = new BufferedWriter(out, BUFFER_64K);
            writeColumnNames(out);
            writeRowValues(out);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(
                    String.format("write table data to csv '%s' failed.", csvFile.getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private void writeColumnNames(Writer out) throws Exception {
        Set<String> keySet = columnNameMap.keySet();
        if (keySet.isEmpty()) {
            return;
        }
        Iterator<String> it = keySet.iterator();
        writeValuesToOutWriter(out, it);
    }

    private void writeValuesToOutWriter(Writer out, Iterator<String> iterator) throws Exception {
        String firstValue = iterator.next();
        out.append(quote(firstValue));
        while (iterator.hasNext()) {
            String nextValue = iterator.next();
            out.append(',').append(quote(nextValue));
        }
        out.append(getLineSeperator());
    }

    private void writeRowValues(Writer out) throws Exception {
        for (List<String> row : rowList) {
            Iterator<String> it = row.iterator();
            writeValuesToOutWriter(out, it);
        }

    }

    private String getLineSeperator() {
        return System.getProperty("line.separator", "\n");
    }

    private String quote(String stringNeedQuoted) {
        if (stringNeedQuoted.indexOf(',') != -1 || stringNeedQuoted.indexOf('\"') != -1) {
            throw new StockException("DBTable columnName and cell value can not support ',' and '\"': "
                    + stringNeedQuoted);
        }
        return "\"" + stringNeedQuoted + "\"";
    }

}
