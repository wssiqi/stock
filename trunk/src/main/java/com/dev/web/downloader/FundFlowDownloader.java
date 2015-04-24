package com.dev.web.downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.dev.web.StockException;
import com.dev.web.model.Stocks;

public class FundFlowDownloader extends Downloader {

    public FundFlowDownloader(List<String> stockIdList) {
        super(stockIdList, new File("FundFlow"), ".html");
    }

    @Override
    protected URL makeDownloadUrl(String stockId) {
        try {
            return new URL(String.format("http://quotes.money.163.com/trade/lszjlx_%s.html", stockId));
        } catch (MalformedURLException e) {
            throw new StockException(e);
        }
    }

    public static void main(String[] args) {
        new FundFlowDownloader( Stocks.getAllStockId()).download();
    }

}
