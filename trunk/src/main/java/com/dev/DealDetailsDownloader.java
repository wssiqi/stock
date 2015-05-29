package com.dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class DealDetailsDownloader {
    // "http://quotes.money.163.com/cjmx/2015/20150528/1000100.xls";
    private static final String URL_FORMAT = "http://quotes.money.163.com/cjmx/%s/%s/%s%s.xls";

    public void download(String stockId, String date) {
        validateStockId(stockId);
        validateDate(date);
        String urlLink = makeUrlLink(stockId, date);
        String outPath = makeSaveToFile(stockId, date);
        downloadContent(urlLink, outPath);
    }

    private String makeSaveToFile(String stockId, String date) {
        File xlsOutFile = new File(String.format("download/deal_details/%s/%s/%s.xls", getYear(date), date, stockId));
        File xlsOutFolder = xlsOutFile.getParentFile();
        if (!xlsOutFolder.exists()) {
            xlsOutFolder.mkdirs();
        }

        return xlsOutFile.getAbsolutePath();
    }

    private void downloadContent(String urlLink, String outPath) {
        if (new File(outPath).exists()) {
            return;
        }
        String tmpOutPath = outPath + ".tmp";
        RuntimeException lastException = null;
        for (int retry = 0; retry < 3; retry++) {
            lastException = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                URL url = new URL(urlLink);
                Proxy proxy = getProxy();
                URLConnection conn = (proxy == null ? url.openConnection() : url.openConnection(proxy));
                in = conn.getInputStream();
                out = new FileOutputStream(tmpOutPath);
                IOUtils.copy(in, out);
            } catch (Exception e) {
                lastException = new RuntimeException(String.format("%s -> %s failed.", urlLink, outPath), e);
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }
        if (lastException == null) {
            new File(tmpOutPath).renameTo(new File(outPath));
        } else {
            throw lastException;
        }
    }

    private String makeUrlLink(String stockId, String date) {
        String stockIdPrefix = stockId.charAt(0) == '0' ? "1" : "0";
        String urlLink = String.format(URL_FORMAT, getYear(date), date, stockIdPrefix, stockId);
        return urlLink;
    }

    private String getYear(String date) {
        return date.substring(0, 4);
    }

    private Proxy getProxy() {
        return new Proxy(Type.HTTP, new InetSocketAddress("10.144.1.10", 8080));
    }

    private void validateStockId(String stockId) {
        if (stockId == null || !stockId.matches("[06][0-9]{5}")) {
            throw new RuntimeException(String.format("Stock id should be 0xxxxx or 6xxxxx, but was '%s'", stockId));
        }
    }

    private void validateDate(String date) {
        if (date == null || !date.matches("201[0-9]{5}")) {
            throw new RuntimeException(String.format("Date should be similar to 20140102, but was '%s'", date));
        }
    }

}
