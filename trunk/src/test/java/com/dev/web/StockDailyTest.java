package com.dev.web;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.h2.tools.SimpleResultSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StockDailyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		BigDecimal dec = new BigDecimal("1.76274100603e+11");
		System.out.println(dec);
		System.out.println(dec.add(dec));
		DBUtil.execute("drop table daily000001");
		SimpleResultSet set = new SimpleResultSet();
		DBUtil.execute("create table daily000001("
		// 交易日期
				+ "date date primary key, "
				// 股票代码
				+ "stockId char(10), "
				// 股票名称
				+ "stockName char(20), "
				// 开盘价
				+ "openingPrice decimal(20,2),"
				// 收盘价
				+ "closingPrice decimal(20,2),"
				// 最高价
				+ "highestPrice decimal(20,2),"
				// 最低价
				+ "lowestPrice decimal(20,2),"
				// 涨跌额
				+ "deltaPrice decimal(20,2),"
				// 涨跌幅
				+ "deltaRate decimal(20,2),"
				// 成交量
				+ "volume decimal(20,2),"
				// 成交金额
				+ "turnover decimal(20,2),"
				// 振幅(%)
				+ "amplitude decimal(20,2),"
				// 换手率(%)
				+ "turnoverRate decimal(20,2),"
				// 总市值
				+ "turnoverRate decimal(20,2),"
				// 流通市值
				+ "turnoverRate decimal(20,2),"
				// end
				+ ")");
		DBUtil.execute("insert into daily000001 values('2012-01-01')");
		DBTable table = DBUtil.executeQuery("select * from daily000001");
		System.out.println(table);
		// StockDaily stockDaily = StockDaily.get("000001");
	}
}
