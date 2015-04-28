package com.dev.web.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.h2.tools.Csv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommUtils;
import com.dev.web.StockException;

public class HtmlTableParser {
	private final HtmlAdapter htmlAdapter;

	public HtmlTableParser(HtmlAdapter htmlAdapter) {
		this.htmlAdapter = htmlAdapter;
	}

	public static void main(String[] args) {
		File pageFilesDir = new File("FundFlow1");
		File csvFilesDir = new File("FundFlowCsv1");
		CommUtils.mkdirs(csvFilesDir);
		for (File htmlFile : pageFilesDir.listFiles()) {
			String fileExtension = CommUtils.getFileExtension(htmlFile);
			if (!"html".equalsIgnoreCase(fileExtension)) {
				continue;
			}
			HtmlTableParser parser = new HtmlTableParser(new HtmlAdapter());
			ResultSet resultSet = parser.getResultSet(htmlFile);
			String nameWithoutExtension = CommUtils
					.getFileNameWithoutExtension(htmlFile);
			File outCsvFile = new File(csvFilesDir, nameWithoutExtension
					+ ".csv");
			System.out.println(htmlFile);
			try {
				new Csv().write(CommUtils.getAbsolutePath(outCsvFile),
						resultSet, "gbk");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet getResultSet(File htmlFile) {
		htmlAdapter.setHtmlFile(htmlFile);
		try {
			Document document = Jsoup.parse(htmlFile, htmlAdapter
					.getHtmlFileCharset().name());

			CsvResultSet resultSet = new CsvResultSet();
			parseColumnNames(resultSet, document);
			parseRowValues(resultSet, document);
			return resultSet;
		} catch (Exception e) {
			throw new StockException(String.format("Parse file '%s' failed.",
					htmlFile.getAbsolutePath()), e);
		}
	}

	private void parseColumnNames(CsvResultSet resultSet, Document document) {
		Elements columnNameElements = document.select(htmlAdapter
				.getColumnNamesCssQuery());
		htmlAdapter.beforeParseColumnName(columnNameElements, resultSet);
		for (int columnNameIndex = 0; columnNameIndex < columnNameElements
				.size(); columnNameIndex++) {
			Element columnNameElement = columnNameElements.get(columnNameIndex);
			String columnName = htmlAdapter.parseColumnName(columnNameElement);
			if (!StringUtils.isEmpty(columnName)) {
				resultSet.addColumn(columnName);
			}
		}
	}

	private void parseRowValues(CsvResultSet resultSet, Document document) {
		Elements rowElements = document.select(htmlAdapter.getRowsCssQuery());
		for (int i = 0; i < rowElements.size(); i++) {
			Element rowElement = rowElements.get(i);
			Elements cellElements = rowElement.select(htmlAdapter
					.getCellsCssQuery());
			List<String> rowValueList = new ArrayList<String>();
			htmlAdapter.beforeParseCells(rowValueList);
			for (int j = 0; j < cellElements.size(); j++) {
				String cellValue = htmlAdapter.parseCellValue(cellElements
						.get(j));
				rowValueList.add(cellValue);
			}
			resultSet.addRow(rowValueList.toArray());
		}
	}
}
