package com.dev.web.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CsvModelTest {

    @Test
    public void test() {
        Table csvModel = new Table();
        List<String> headerList = Arrays.asList("1", "2", "3");
        csvModel.setParent(headerList);
        csvModel.addRow(headerList);
    }

}
