package com.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DailyRecord {
	private String date;
	@Column(length = 10)
	private String stockId;
}
