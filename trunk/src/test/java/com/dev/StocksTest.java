package com.dev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class StocksTest {

    private static final char CSV_SEPERATOR = ',';

    @Test
    public void testGetAllStocks() throws Exception {
        File saveDir = new File("out");
        System.out.println(saveDir.mkdirs());
        download(saveDir);
    }

    private void newLine(StringBuffer out) {
        out.append("\r\n");
    }

    private void download(File saveDir) throws InterruptedException, FileNotFoundException, IOException {
        List<Stock> stockList = Stocks.getStockList();
        File outFile = new File(saveDir, getDateTime() + ".csv");
        StringBuffer outBuffer = new StringBuffer();
        int writeCnt = 0;
        for (Stock stock : stockList) {
            String url = String.format("http://data.eastmoney.com/zjlx/%s.html", stock.code);
            Document html = null;
            do {
                try {
                    System.out.println("Downloading... " + stock.code);
                    html = Jsoup.parse(new URL(url), 3000);
                } catch (Exception e) {
                    System.out.println("Wait 30 seconds...");
                    Thread.sleep(30000);
                }
            } while (html == null);
            Elements rows = html.select("table.tab1 tbody tr");
            if (rows.size() < 10) {
                System.out.println("Skip " + stock.toString() + " no data");
                continue;
            }
            for (Element row : rows) {
                newLine(outBuffer);
                outBuffer.append(stock.code);
                outBuffer.append(CSV_SEPERATOR).append(stock.name);
                Elements datas = row.select("td");
                for (Element data : datas) {
                    String text = data.text();
                    outBuffer.append(CSV_SEPERATOR).append(Utils.translateToNum(text));
                }
            }
            writeCnt++;
            if (writeCnt % 10 == 0) {
                FileUtils.write(outFile, outBuffer, "GBK");
                System.out.println("save to disk");
            }
        }
    }

    private String getDateTime() {
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return dateTime;
    }

}
