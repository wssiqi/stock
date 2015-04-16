package com.dev.web;

import java.io.File;

import org.junit.Test;

import com.dev.web.downloader.Downloader;
import com.dev.web.downloader.FundFlowDownloader;
import com.dev.web.model.Stocks;

public class DownloaderTest {

    @Test
    public void test() {
        Downloader downloader = new FundFlowDownloader(Stocks.getAllStockId());
        downloader.download();
    }
}
