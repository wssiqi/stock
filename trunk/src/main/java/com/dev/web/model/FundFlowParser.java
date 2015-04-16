package com.dev.web.model;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.DBTable;
import com.dev.web.StockException;

public abstract class FundFlowParser {
    public static void main(String[] args) {
        DBTable parse = FundFlowParser.parse(new File("FundFlow/000001.html"));
        System.out.println(parse);
    }

    private static DBTable parse(File fundFlowPageFile) {
        try {
            DBTable table = new DBTable();
            Document document = Jsoup.parse(fundFlowPageFile, "UTF-8");
            Elements tableHeaderElements = document.select("tr th");
            for (int i = 0; i < tableHeaderElements.size(); i++) {
                Element element = tableHeaderElements.get(i);
                String tableHeader = element.text();
                int indexOf = tableHeader.indexOf('（');
                if (indexOf != -1) {
                    tableHeader = tableHeader.substring(0, indexOf);
                }
                tableHeader = StringUtils.trim(tableHeader);
                if (StringUtils.isEmpty(tableHeader)) {
                    throw new StockException(String.format("Find empty table header '%s' from file '%s'.", tableHeader,
                            fundFlowPageFile.getAbsolutePath()));
                }
                table.addColumnName(tableHeader);
            }

            Elements tableRowElements = document.select("div.inner_box tbody tr");
            for (int i = 0; i < tableRowElements.size(); i++) {
                Element tableRowElement = tableRowElements.get(i);
                Elements cellElements = tableRowElement.select("td");
                ArrayList<String> tableRow = new ArrayList<String>();
                table.addRowData(tableRow);
                for (int j = 0; j < cellElements.size(); j++) {
                    Element cellElement = cellElements.get(i);
                    tableRow.add(StringUtils.trimToEmpty(cellElement.text()));
                }
            }
            return table;
        } catch (Exception e) {
            throw new StockException(String.format("Parse file '%s' failed.", fundFlowPageFile.getAbsolutePath()), e);
        }
    }
}
