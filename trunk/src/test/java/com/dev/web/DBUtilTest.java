package com.dev.web;

import java.io.File;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
//		DBUtil.execute("drop table dailyrecordcsv");
//		SessionFactory factory = new AnnotationConfiguration().configure()
//				.buildSessionFactory();
		// Session session = factory.openSession();
		// Query query = session.createSQLQuery("select * from test");
		// List list = query.list();
		// System.out.println();
		DBTable table = DBUtil.executeQuery("select * from dailyrecordcsv");
		System.out.println(table);
		DBUtil.execute("insert into dailyrecordcsv(日期,股票代码,名称,收盘价,最高价,最低价,开盘价,前收盘,涨跌额,涨跌幅,换手率,成交量,成交金额,总市值,流通市值) select * from CSVREAD('daily-utf8/000001.csv')");
		// DBTable dbTable = new CsvReader(new File("daily-utf8/000001.csv"))
		// .readDataAsTable();
		// System.out.println(dbTable);
	}

}
