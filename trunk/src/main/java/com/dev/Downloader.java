package com.dev;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Downloader {

    private static final Logger LOGGER = Logger.getLogger(Downloader.class);

    static {
        System.setProperty("http.proxyHost", "10.144.1.10");
        System.setProperty("http.proxyPort", "8080");
    }
    private static final char CSV_SEPERATOR = ',';

    private static List<Stock> stockList;

    public static void downloadRealtimeFundFlow(File saveDir) throws Exception {
        StringBuffer out = new StringBuffer();
        out.append("code,name,price,rate,in,inrate,in0,rate0,in1,rate1,in2,rate2,in3,rate3");
        stockList = Stocks.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = Stocks.getStockList().get(i);
            Document html = downloadHtml(stock, String.format("http://data.eastmoney.com/zjlx/%s.html", stock.code));
            Elements rows = html.select("div.flash-data-cont ul");
            if (rows.size() < 5) {
                LOGGER.info("Skip " + stock.toString() + " no data");
                continue;
            }
            appendNewLine(out);
            appendStockCodeAndName(out, stock);
            appendStockPriceAndRateToday(out, stock);
            for (Element row : rows) {
                appendStockTodayFlowData(out, row);
            }

            if (i++ % 20 == 0) {
                File saveFile = new File(saveDir, Utils.getDateTime() + ".csv");
                saveToFile(out, saveFile);
            }
        }
    }

    private static void appendStockPriceAndRateToday(StringBuffer out, Stock stock) {
        try {
            Document htmlDoc = downloadHtml(stock, String.format("http://3g.163.com/3gstock/quotes/stock/%s%s.html",
                    getStockIdPrefixHardCodeBy163(stock), stock.code));
            String priceAndRate = htmlDoc.select("div.content span").get(1).text();
            priceAndRate = StringUtils.trim(priceAndRate);
            out.append(CSV_SEPERATOR)
                    .append(Utils.translateToNum(priceAndRate.substring(0, priceAndRate.indexOf('('))));
            out.append(CSV_SEPERATOR).append(
                    Utils.translateToNum(priceAndRate.substring(priceAndRate.indexOf(')') + 1, priceAndRate.length())));
        } catch (Exception e) {
            LOGGER.error("Get price and rate failed " + stock, e);
        }
    }

    private static String getStockIdPrefixHardCodeBy163(Stock stock) {
        char firstChar = stock.code.charAt(0);
        switch (firstChar) {
        case '0':
        case '3':
            return "1";
        case '6':
            return "0";
        default:
            throw new IllegalArgumentException(String.format("Stock code should start with 0/3/6, but current is <%s>",
                    stock.toString()));
        }
    }

    private static void appendStockTodayFlowData(StringBuffer out, Element row) {
        Elements datas = row.select("li.data-val span");
        for (Element data : datas) {
            out.append(CSV_SEPERATOR).append(Utils.translateToNum(data.text()));
        }
    }

    public static void downloadFundFlow(File saveDir) throws Exception {
        StringBuffer out = new StringBuffer();
        out.append("code,name,date,price,rate,in,inrate,in0,rate0,in1,rate1,in2,rate2,in3,rate3");

        List<Stock> stockList = Stocks.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = Stocks.getStockList().get(i);
            Document html = downloadHtml(stock, String.format("http://data.eastmoney.com/zjlx/%s.html", stock.code));
            Elements rows = html.select("table.tab1 tbody tr");
            if (rows.size() < 10) {
                LOGGER.info("Skip " + stock.toString() + " no data");
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

    private static Document downloadHtml(Stock stock, String url) throws InterruptedException {
        Document html = null;
        do {
            try {
                LOGGER.info("Downloading... " + stock.code);
                html = Jsoup.parse(new URL(url), 3000);
            } catch (Exception e) {
                LOGGER.info("Wait 30 seconds...");
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
        LOGGER.info("save to disk");
    }
}
