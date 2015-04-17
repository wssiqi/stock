package com.dev.web.model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dev.web.TestUtil;

public class CsvCombinerTest {

    private File csvFile1;
    private File csvFile2;

    @Before
    public void copyTestDataFileToWorkDir() throws IOException {
        String csv1 = "000001.csv";
        String csv2 = "000002.csv";
        this.csvFile1 = new File(TestUtil.WS_HOME, csv1);
        this.csvFile2 = new File(TestUtil.WS_HOME, csv2);
        FileUtils.copyFile(new File(TestUtil.RESOURCE_HOME, csv1), csvFile1);
        FileUtils.copyFile(new File(TestUtil.RESOURCE_HOME, csv2), csvFile2);
    }

    @After
    public void deleteTestData() {
        FileUtils.deleteQuietly(csvFile1);
        FileUtils.deleteQuietly(csvFile2);
    }

    @Test
    public void testCombineTwoCsvFile() {
        CsvCombiner csvCombiner = new CsvCombiner();
        List<File> csvFiles = Arrays.asList(csvFile1, csvFile2);
        csvCombiner.combine(csvFiles, csvFile1);

    }

}
