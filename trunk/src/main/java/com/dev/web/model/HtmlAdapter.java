package com.dev.web.model;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommonUtils;
import com.dev.web.StockException;

public class HtmlAdapter {

    private File htmlFile;

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public Charset getHtmlFileCharset() {
        return Charset.forName("GBK");
    }

    public String getRowsCssQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCellsCssQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnNamesCssQuery() {
        return "thead tr th";
    }

    public void beforeParseColumnName(Elements columnNameElements, CsvResultSet resultSet) {
        resultSet.addColumn("股票代码");
        for (Element element : columnNameElements) {
            
        }
    }

    public String parseColumnName(Element element) {
        return null;
    }

    public void beforeParseCells(List<String> rowValueList) {
        rowValueList.add("'" + CommonUtils.getFileNameWithoutExtension(htmlFile));
    }

    public String parseCellValue(Element cellElement) {
        return StringUtils.trimToEmpty(cellElement.text());
    }
}
