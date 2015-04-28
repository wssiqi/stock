package com.dev.web.model;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommUtils;

public class HtmlAdapter {

	private String stockId;

	public void setHtmlFile(File htmlFile) {
		stockId = "'" + CommUtils.getFileNameWithoutExtension(htmlFile);
	}

	public Charset getHtmlFileCharset() {
		return Charset.forName("GBK");
	}

	public String getRowsCssQuery() {
		return null;
	}

	public String getCellsCssQuery() {
		return null;
	}

	public String getColumnNamesCssQuery() {
		return "thead tr th";
	}

	public void beforeParseColumnName(Elements columnNameElements,
			CsvResultSet resultSet) {
		resultSet.addColumn("股票代码");

		Elements copy = columnNameElements.clone();
		Elements rowSpan = copy.select("th[rowspan]");
		for (Element columnNameElement : rowSpan) {
			resultSet.addColumn(CommUtils.trim(columnNameElement.text()));
		}

		Elements categorys = copy.select("th[colspan]");
		copy.removeAll(rowSpan);
		copy.removeAll(categorys);
		Elements other = copy;
		int otherIndex = 0;
		for (int i = 0; i < categorys.size(); i++) {
			Element category = categorys.get(i);
			String prefix = CommUtils.trim(category.text());
			resultSet.addColumn(prefix
					+ CommUtils.trim(other.get(otherIndex++).text()));
			resultSet.addColumn(prefix
					+ CommUtils.trim(other.get(otherIndex++).text()));
		}
	}

	public String parseColumnName(Element element) {
		return null;
	}

	public void beforeParseCells(List<String> rowValueList) {
		rowValueList.add(stockId);
	}

	public String parseCellValue(Element cellElement) {
		return CommUtils.trim(cellElement.text());
	}
}