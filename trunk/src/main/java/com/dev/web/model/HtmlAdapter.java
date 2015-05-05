package com.dev.web.model;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommUtils;

public class HtmlAdapter {

	private Map<String, String> colMapping = new HashMap<String, String>();
	{
		colMapping.put("股票代码", "CODE");
		colMapping.put("日期", "DATE");
		colMapping.put("收盘价", "EPRICE");
		colMapping.put("涨跌幅", "RATE");
		colMapping.put("主力净流入净额", "MAININ");
		colMapping.put("主力净流入净占比", "MAININRATE");
		colMapping.put("超大单净流入净额", "HUGEIN");
		colMapping.put("超大单净流入净占比", "HUGEINRATE");
		colMapping.put("大单净流入净额", "BIGIN");
		colMapping.put("大单净流入净占比", "BIGINRATE");
		colMapping.put("中单净流入净额", "MIDIN");
		colMapping.put("中单净流入净占比", "MIDINRATE");
		colMapping.put("小单净流入净额", "SMALLIN");
		colMapping.put("小单净流入净占比", "SMALLINRATE");
	}
	private String stockId;

	public void setHtmlFile(File htmlFile) {
		stockId = "'" + CommUtils.getFileNameWithoutExtension(htmlFile);
	}

	public Charset getHtmlFileCharset() {
		return Charset.forName("GBK");
	}

	public String getRowsCssQuery() {
		return "table.tab1 tbody tr";
	}

	public String getCellsCssQuery() {
		return "td";
	}

	public String getColumnNamesCssQuery() {
		return "thead tr th";
	}

	public void beforeParseColumnName(Elements columnNameElements,
			CsvResultSet resultSet) {
		resultSet.addColumn(getNameInTable("股票代码"));

		Elements copy = columnNameElements.clone();
		Elements rowSpan = copy.select("th[rowspan]");
		for (Element columnNameElement : rowSpan) {
			String nameInPage = CommUtils.trim(columnNameElement.text());
			resultSet.addColumn(getNameInTable(nameInPage));
		}

		Elements categorys = copy.select("th[colspan]");
		copy.removeAll(rowSpan);
		copy.removeAll(categorys);
		Elements other = copy;
		int otherIndex = 0;
		for (int i = 0; i < categorys.size(); i++) {
			Element category = categorys.get(i);
			String prefix = CommUtils.trim(category.text());
			resultSet.addColumn(getNameInTable(prefix
					+ CommUtils.trim(other.get(otherIndex++).text())));
			resultSet.addColumn(getNameInTable(prefix
					+ CommUtils.trim(other.get(otherIndex++).text())));
		}
	}

	private String getNameInTable(String nameInPage) {
		return colMapping.get(nameInPage);
	}

	public String parseColumnName(Element element) {
		return null;
	}

	public void beforeParseCells(List<String> rowValueList) {
		rowValueList.add(stockId);
	}

	public String parseCellValue(Element cellElement) {
		String str = CommUtils.trim(cellElement.text());
		int indexOfPercent = str.lastIndexOf('%');
		if (indexOfPercent != -1) {
			str = str.substring(0, indexOfPercent);
		}
		int indexOf1E4 = str.lastIndexOf('万');
		if (indexOf1E4 != -1) {
			return String.valueOf(new BigDecimal(str.substring(0, indexOf1E4))
					.multiply(new BigDecimal(10000)));
		}
		int indexOf1E8 = str.lastIndexOf('亿');
		if (indexOf1E8 != -1) {
			return String.valueOf(new BigDecimal(str.substring(0, indexOf1E8))
					.multiply(new BigDecimal(100000000)));
		}

		if ("-".equals(str)) {
			str = "0";
		}
		return str;
	}
}
