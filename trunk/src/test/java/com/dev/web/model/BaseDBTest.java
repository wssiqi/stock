package com.dev.web.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.dev.web.StockException;

public class BaseDBTest {

    @Test
    public void buildInsertSqlWithNormalParameters() {
        BaseDB baseDB = new BaseDB();
        String insertSql = baseDB.buildInsert("tableA", Arrays.asList("Col1", "Col2"), Arrays.asList("val1", "val2"));
        assertEquals("insert into tableA (Col1,Col2) values(val1,val2)", insertSql);
    }

    @Test(expected = StockException.class)
    public void buildInsertSqlWithEmptyTableName() {
        BaseDB baseDB = new BaseDB();
        baseDB.buildInsert("", Arrays.asList("Col1", "Col2"), Arrays.asList("val1", "val2"));
    }

    @Test(expected = StockException.class)
    public void buildInsertSqlWithNullTableName() {
        BaseDB baseDB = new BaseDB();
        baseDB.buildInsert(null, Arrays.asList("Col1", "Col2"), Arrays.asList("val1", "val2"));
    }

    @Test
    public void buildSelectSqlWithNormalParameters() {
        BaseDB baseDB = new BaseDB();
        String selectSql = baseDB.buildSelect("tableA", Arrays.asList("Col1", "Col2"));
        assertEquals("select Col1,Col2 from tableA", selectSql);
    }
}
