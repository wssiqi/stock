package com.dev.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class LocalStock {
	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		File dir = new File("easyMoney");
		for (File file : dir.listFiles()) {
			parseStockId(file);
		}
		DBUtil.executeQuery("select * from stock");
	}

	private static void parseStockId(File file) throws IOException {
		Document document = Jsoup.parse(file, null);
		Elements select = document.select("table.table-data tbody tr");
		for (Element element : select) {
			Elements tds = element.select("td");
			if (tds.size() > 2) {
				String stockId = tds.get(1).text();
				String stockName = tds.get(2).text();
				System.out.print(stockId + "\t");
				System.out.println(stockName);
				String sql = String.format(
						"insert into stock values('%s','%s')", stockId,
						stockName);
				DBUtil.execute(sql);
			}
		}
	}
}
