package com.stock.entity;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

public class DailyRecordTest {

	@Test
	public void test() {
		@SuppressWarnings("deprecation")
		Session session = new AnnotationConfiguration().configure()
				.buildSessionFactory().getCurrentSession();
		
	}
}
