package com.dev.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Main {
	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		System.out.println();
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		String newStockUrl = "http://data.eastmoney.com/xg/xg/default.html";
		String stockCenterUrl = "http://quote.eastmoney.com/center/list.html#33";
		HtmlPage webPage = webClient.getPage(stockCenterUrl);
		showPageTable(webPage);

		int page = 1;
		Page nextPage = webPage;
		showPageAsXml(nextPage, page);
		HtmlAnchor nextBtn = null;
		while ((nextBtn = (HtmlAnchor) webPage.getFirstByXPath(String.format(
				"//a[@data-page='%s']", ++page))) != null) {
			nextPage = nextBtn.click();
			showPageAsXml(nextPage, page);
		}
	}

	private static void showPageAsXml(Page nextPage, int page) {
		if (nextPage instanceof HtmlPage) {
			String asXml = ((HtmlPage) nextPage).asXml();
			try {
				File file = new File(String.format("save/stock%03d.html", page));
				System.out.println(file.getAbsolutePath());
				FileUtils.write(file, asXml, Charset
						.forName(((HtmlPage) nextPage).getPageEncoding()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void showPageTable(Page webPage) {
		Map<String, Integer> tableHeaderMap = new LinkedHashMap<String, Integer>();
		Map<Integer, List<String>> tableRowMap = new LinkedHashMap<Integer, List<String>>();
		if (webPage instanceof HtmlPage) {
			HtmlPage htmlPage = (HtmlPage) webPage;
			List<?> tableHeaderList = htmlPage.getByXPath("//th");
			for (int i = 0; i < tableHeaderList.size(); i++) {
				Object e = tableHeaderList.get(i);
				if (e instanceof DomNode) {
					DomNode cell = (DomNode) e;
					String header = CommonUtils.skipReturnAndNextLineChar(cell
							.asText());
					tableHeaderMap.put(header, i);
				}
			}
			List<?> rowObjList = htmlPage.getByXPath("//tbody/tr");
			for (int i = 0; i < rowObjList.size(); i++) {
				Object rowObj = rowObjList.get(i);
				if (rowObj instanceof DomNode) {
					List<?> cellObjList = ((DomNode) rowObj).getByXPath("./td");
					List<String> cellStrList = new ArrayList<String>(
							cellObjList.size());
					for (int j = 0; j < cellObjList.size(); j++) {
						Object cellObj = cellObjList.get(j);
						if (cellObj instanceof DomNode) {
							String cellStr = CommonUtils
									.skipReturnAndNextLineChar(((DomNode) cellObj)
											.asText());
							cellStrList.add(cellStr);
						}
					}
					tableRowMap.put(i, cellStrList);
				}
			}
		}
		System.out.println();
		for (Entry<String, Integer> headerEntry : tableHeaderMap.entrySet()) {
			System.out.print(headerEntry.getKey() + "\t");
		}
		for (Entry<Integer, List<String>> tableRowEntry : tableRowMap
				.entrySet()) {
			System.out.println();
			for (String cellStr : tableRowEntry.getValue()) {
				System.out.print(cellStr + "\t");
			}
		}
	}

	private static void showPageAsXml(HtmlPage htmlPage) throws IOException {
		String asXml = htmlPage.asXml();
		FileUtils.write(new File("new-stock.html"), asXml,
				Charset.forName(htmlPage.getPageEncoding()));
		System.out.println(asXml);
	}
}
