package com.dev.web.model;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.h2.tools.Csv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommUtils;
import com.dev.web.StockException;

public class HtmlTableParser {
    private static final Logger LOGGER = Logger.getLogger(HtmlTableParser.class);
    private final HtmlAdapter htmlAdapter;
    private CsvResultSet resultSet;

    public HtmlTableParser(HtmlAdapter htmlAdapter) {
        this.htmlAdapter = htmlAdapter;
    }

    public static void main(String[] args) {
        File inDir = new File("FundFlow1");
        File outDir = new File("FundFlowCsv1");
        CommUtils.mkdirs(outDir);

        CharArrayWriter out = new CharArrayWriter();
        for (File htmlFile : filterHtmlFiles(inDir)) {
            String stockId = CommUtils.getNameWithoutExtension(htmlFile);
            try {
                String htmlContent = FileUtils.readFileToString(htmlFile, "GBK");
                HtmlTableParser parser = new HtmlTableParser(new HtmlAdapter());
                LOGGER.info(stockId);
                parser.parse(stockId, htmlContent);
                new Csv().write(out, parser.getResultSet());
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        try {
            List<String> lines = IOUtils.readLines(new CharArrayReader(out.toCharArray()));
            Iterator<String> it = lines.iterator();
            if (!it.hasNext()) {
                return;
            }
            // skip header row
            it.next();
            while (it.hasNext()) {
                String line = it.next();
                if (line.contains("CODE") || line.contains("没有")) {
                    it.remove();
                }
            }
            FileUtils.writeLines(new File("out.csv"), "GBK", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File[] filterHtmlFiles(File inDir) {
        return inDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return "html".equalsIgnoreCase(CommUtils.getFileExtension(name));
            }
        });
    }

    public CsvResultSet getResultSet() {
        return resultSet;
    }

    public void parse(String id, String htmlContent) {
        resultSet = new CsvResultSet();
        htmlAdapter.setStockId(id);
        try {
            Document htmlDoc = Jsoup.parse(htmlContent);
            parseCols(htmlDoc);
            parseRows(htmlDoc);
        } catch (Exception e) {
            throw new StockException("Parse html content failed:\r\n" + htmlContent, e);
        }
    }

    private void parseCols(Document htmlDoc) {
        Elements columnNameElements = htmlDoc.select(htmlAdapter.getColumnNamesCssQuery());
        htmlAdapter.beforeParseColumnName(columnNameElements, resultSet);
        for (int columnNameIndex = 0; columnNameIndex < columnNameElements.size(); columnNameIndex++) {
            Element columnNameElement = columnNameElements.get(columnNameIndex);
            String columnName = htmlAdapter.parseColumnName(columnNameElement);
            if (!StringUtils.isEmpty(columnName)) {
                resultSet.addColumn(columnName);
            }
        }
    }

    private void parseRows(Document htmlDoc) {
        Elements rowElements = htmlDoc.select(htmlAdapter.getRowsCssQuery());
        for (int i = 0; i < rowElements.size(); i++) {
            Element rowElement = rowElements.get(i);
            Elements cellElements = rowElement.select(htmlAdapter.getCellsCssQuery());
            List<String> rowValueList = new ArrayList<String>();
            htmlAdapter.beforeParseCells(rowValueList);
            for (int j = 0; j < cellElements.size(); j++) {
                String cellValue = htmlAdapter.parseCellValue(cellElements.get(j));
                rowValueList.add(cellValue);
            }
            resultSet.addRow(rowValueList.toArray());
        }
    }
}
