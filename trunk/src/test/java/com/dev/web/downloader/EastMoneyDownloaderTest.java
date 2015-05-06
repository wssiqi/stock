package com.dev.web.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.tools.Csv;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dev.web.CommUtils;
import com.dev.web.DateUtils;
import com.dev.web.model.CsvResultSet;
import com.dev.web.model.Stocks;

public class EastMoneyDownloaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDownload() throws Exception {
        Exception lastException = null;
        EastMoneyDownloader downloader = new EastMoneyDownloader(Stocks.getAllStockId());
        do {
            lastException = null;
            try {
                downloader.download();
            } catch (Exception e) {
                lastException = e;
            }
            Map<String, Object> downloaded = downloader.getDownloaded();
            System.out.println("Downloaded: " + downloaded.keySet());
            CsvResultSet rs = new CsvResultSet();
            for (String colName : new String[] { "ID", "DATE", "PRICE", "RATE", "MAIN_IN", "MAIN_RATE", "HUGE_IN",
                    "HUGE_RATE", "BIG_IN", "BIG_RATE", "MID_IN", "MID_RATE", "SMALL_IN", "SMALL_RATE" }) {
                rs.addColumn(colName);
            }
            for (Entry<String, Object> e : downloaded.entrySet()) {
                String stockId = e.getKey();
                @SuppressWarnings("unchecked")
                List<List<String>> rows = (List<List<String>>) e.getValue();
                for (List<String> row : rows) {
                    row.add(0, stockId);
                    rs.addRow(row.toArray());
                }
            }
            new Csv().write(DateUtils.getCurrentDateGMT8("yyyy-MM-dd-HHmmss") + ".csv", rs, "GBK");
            try {
                Thread.sleep(120000);
            } catch (Exception e) {
            }
            downloader = new EastMoneyDownloader(downloader.getNoneDownloadedStockIds());
        } while (lastException != null);
    }
}
