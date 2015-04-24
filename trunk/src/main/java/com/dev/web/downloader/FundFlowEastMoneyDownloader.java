package com.dev.web.downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.dev.web.StockException;

public class FundFlowEastMoneyDownloader extends Downloader {

    public FundFlowEastMoneyDownloader(List<String> stockIdList) {
        super(stockIdList, new File("FundFlow1"), ".html");
    }

    @Override
    protected URL makeDownloadUrl(String stockId) {
        try {
            return new URL(String.format("http://data.eastmoney.com/zjlx/%s.html", stockId));
        } catch (MalformedURLException e) {
            throw new StockException(e);
        }
    }

    public static void main(String[] args) {
        List<String> stockIdList = Arrays.asList("600730");
        new FundFlowEastMoneyDownloader(stockIdList).download();
    }

}
