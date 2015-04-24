package com.dev.web.model;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.h2.tools.Csv;
import org.h2.tools.SimpleResultSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommonUtils;
import com.dev.web.DBTable;
import com.dev.web.StockException;

public abstract class FundFlowParser {
	public static void main(String[] args) {
		File pageFilesDir = new File("FundFlow");
		File csvFilesDir = new File("FundFlowCsv");
		CommonUtils.mkdirs(csvFilesDir);
		for (File file : pageFilesDir.listFiles()) {
			String fileExtension = CommonUtils.getFileExtension(file);
			if (!"html".equalsIgnoreCase(fileExtension)) {
				continue;
			}
			SimpleResultSet resultSet = FundFlowParser.parse(file);
			String nameWithoutExtension = CommonUtils
					.getFileNameWithoutExtension(file);
			File outCsvFile = new File(csvFilesDir, nameWithoutExtension
					+ ".csv");
			System.out.println(file);
			try {
				new Csv().write(CommonUtils.getAbsolutePath(outCsvFile),
						resultSet, "gbk");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static SimpleResultSet parse(File fundFlowPageFile) {
		try {
			CsvResultSet csvResultSet = new CsvResultSet();
			Document document = Jsoup.parse(fundFlowPageFile, "UTF-8");
			Elements tableHeaderElements = document.select("tr th");
			csvResultSet.addColumn("股票代码");
			for (int i = 0; i < tableHeaderElements.size(); i++) {
				Element element = tableHeaderElements.get(i);
				String tableHeader = StringUtils.trim(element.text());
				if (StringUtils.isEmpty(tableHeader)) {
					throw new StockException(String.format(
							"Find empty table header '%s' from file '%s'.",
							tableHeader, fundFlowPageFile.getAbsolutePath()));
				}
				csvResultSet.addColumn(tableHeader);
			}

			Elements tableRowElements = document
					.select("div.inner_box tbody tr");
			for (int i = 0; i < tableRowElements.size(); i++) {
				Element tableRowElement = tableRowElements.get(i);
				Elements cellElements = tableRowElement.select("td");
				ArrayList<String> tableRow = new ArrayList<String>();
				String stockId = CommonUtils
						.getFileNameWithoutExtension(fundFlowPageFile);
				tableRow.add("'" + stockId);
				for (int j = 0; j < cellElements.size(); j++) {
					Element cellElement = cellElements.get(j);
					tableRow.add(StringUtils.trimToEmpty(cellElement.text()));
				}
				csvResultSet.addRow(tableRow.toArray());
			}
			return csvResultSet;
		} catch (Exception e) {
			throw new StockException(String.format("Parse file '%s' failed.",
					fundFlowPageFile.getAbsolutePath()), e);
		}
	}
}
