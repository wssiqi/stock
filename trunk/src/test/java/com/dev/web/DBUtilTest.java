package com.dev.web;

import static org.junit.Assert.*;

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

	@Test
	public void test() {
		DBUtil.execute("drop table daily000001");
		DBTable dbtable = DBUtil.executeQuery("select * from daily000001");
		System.out.println();
	}

}
