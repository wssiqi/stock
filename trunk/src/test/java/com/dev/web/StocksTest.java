package com.dev.web;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StocksTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		List<String> stockIdList = Stocks.getAllStockId();
		for (String stockId : stockIdList) {
			StockState stockState = StockState.get(stockId);
			System.out.println(stockState);
		}
	}

}
