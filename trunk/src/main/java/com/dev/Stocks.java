package com.dev;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class Stocks {
    public static List<Stock> getStockList() {
        List<Stock> stockList = new ArrayList<Stock>();
        InputStream in = null;
        try {
            URL url = new URL("http://quote.eastmoney.com/stocklist.html");
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            String htmlContent = new String(out.toByteArray(), "GBK");
            // Match <li> xxx </li>
            // <li><a target="_blank"
            // href="http://quote.eastmoney.com/sz002736.html">国信证券(002736)</a></li>
            List<String> liList = Utils.getRegexMatchs("\\<li\\>[\\s\\S]+?\\</li\\>", htmlContent);
            for (String liHtml : liList) {
                Stock stock = new Stock();
                stock.code = Utils.getFirstMatch("(?<=\\().+(?=\\))", liHtml);
                stock.name = Utils.getFirstMatch("(?<=\\>)[^\\>]+?(?=\\()", liHtml);
                stock.maket = Utils.getFirstMatch(buildRegex("/", "[^/\\.]+", ".html"), liHtml);
                if (!stock.isValid()) {
                    continue;
                }
                stockList.add(stock); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return stockList;
    }

    private static String buildRegex(String lStr, String regex, String rStr) {
        return String.format("(?<=%s)%s(?=%s)", Pattern.quote(lStr), regex, Pattern.quote(rStr));
    }
}
