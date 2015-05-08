package com.dev;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Downloader {

    private static final char CSV_SEPERATOR = ',';

    public static void downloadFundFlowToday(File saveDir) throws Exception {
        StringBuffer out = new StringBuffer();

        List<Stock> stockList = Stocks.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = Stocks.getStockList().get(i);
            Document html = downloadFundFlowHtml(stock);
            Elements rows = html.select("div.flash-data-cont ul");
            if (rows.size() < 5) {
                Logger.getLogger(Downloader.class).info("Skip " + stock.toString() + " no data");
                continue;
            }
            appendNewLine(out);
            appendStockCodeAndName(out, stock);
            appendStockCurrentPriceAndRate(out,stock);
            for (Element row : rows) {
                appendStockTodayFlowData(out, row);
            }

            if (i++ % 10 == 0) {
                File saveFile = new File(saveDir, Utils.getDateTime() + ".csv");
                saveToFile(out, saveFile);
            }

        }
    }

    private static void appendStockCurrentPriceAndRate(StringBuffer out, Stock stock) {
        // TODO Auto-generated method stub
        
    }

    private static void appendStockTodayFlowData(StringBuffer out, Element row) {
        Elements datas = row.select("li.data-val span");
        for (Element data : datas) {
            out.append(CSV_SEPERATOR).append(Utils.translateToNum(data.text()));
        }
    }

    public static void downloadFundFlow(File saveDir) throws Exception {
        StringBuffer out = new StringBuffer();

        List<Stock> stockList = Stocks.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = Stocks.getStockList().get(i);
            Document html = downloadFundFlowHtml(stock);
            Elements rows = html.select("table.tab1 tbody tr");
            if (rows.size() < 10) {
                Logger.getLogger(Downloader.class).info("Skip " + stock.toString() + " no data");
                continue;
            }
            for (Element row : rows) {
                appendNewLine(out);
                appendStockCodeAndName(out, stock);
                appendStockHistoryFlowData(out, row);
            }

            if (i++ % 10 == 0) {
                File saveFile = new File(saveDir, Utils.getDateTime() + ".csv");
                saveToFile(out, saveFile);
            }

        }
    }

    private static Document downloadFundFlowHtml(Stock stock) throws InterruptedException {
        String url = String.format("http://data.eastmoney.com/zjlx/%s.html", stock.code);
        Document html = null;
        do {
            try {
                Logger.getLogger(Downloader.class).info("Downloading... " + stock.code);
                html = Jsoup.parse(new URL(url), 3000);
            } catch (Exception e) {
                Logger.getLogger(Downloader.class).info("Wait 30 seconds...");
                Thread.sleep(30000);
            }
        } while (html == null);
        return html;
    }

    private static void appendNewLine(StringBuffer out) {
        out.append("\r\n");
    }

    private static void appendStockCodeAndName(StringBuffer out, Stock stock) {
        out.append(stock.code);
        out.append(CSV_SEPERATOR).append(stock.name);
    }

    private static void appendStockHistoryFlowData(StringBuffer out, Element row) {
        Elements datas = row.select("td");
        for (Element data : datas) {
            String text = data.text();
            out.append(CSV_SEPERATOR).append(Utils.translateToNum(text));
        }
    }

    private static void saveToFile(StringBuffer out, File saveFile) throws IOException {
        FileUtils.write(saveFile, out, "GBK");
        Logger.getLogger(Downloader.class).info("save to disk");
    }
}
