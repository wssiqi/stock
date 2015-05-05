package com.dev.web.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dev.web.DBTable;

public class InfoDBTest {

	static InfoDB db = new InfoDB();

	@Test
	public void testname() throws Exception {
		db.execute("insert into stockhis(CODE,DATE,EPRICE,RATE,MAININ,MAININRATE,HUGEIN,HUGEINRATE,BIGIN,BIGINRATE,MIDIN,MIDINRATE,SMALLIN,SMALLINRATE) SELECT * FROM CSVREAD('fundflow1.csv')");
		DBTable table = db
				.executeQuery("select DISTINCT date from stockhis order by date");
		table.print();
		String sql = String
				.format("select * from (select * from stockhis where date='2015-05-04' and RATE<1 and MAININ>0) r0 inner join "
						+ "(select * from stockhis where date='2015-04-30' and RATE<0 and MAININ>0) r1 on r0.code=r1.code");
		table = db.executeQuery(sql);
		table.print();
	}
}
