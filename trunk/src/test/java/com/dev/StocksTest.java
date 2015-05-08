package com.dev;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;

public class StocksTest {

    @Test
    public void testGetAllStocks() throws Exception {
        File saveDir = new File("out");
        Logger.getLogger(getClass()).info(saveDir.mkdirs());
        Downloader.downloadFundFlow(saveDir);
    }

}
