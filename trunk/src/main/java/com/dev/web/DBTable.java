package com.dev.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBTable {
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
			throw new RuntimeException(String.format(
					"Column name '%s' not found. Valid is '%s'", columnName,
					columnNameMap.keySet()));
		}
		List<String> columnValues = new ArrayList<String>(rowList.size());
		for (List<String> rowData : rowList) {
			columnValues.add(rowData.get(columnIndex));
		}
		return columnValues;
	}
}
