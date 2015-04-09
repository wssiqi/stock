package com.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "日期", "股票代码" }) })
public class DailyRecordCsv {
	@Id
	@GeneratedValue
	private String id;
	@Column(name = "日期")
	private String date;
	@Column(name = "股票代码")
	private String stockId;
	@Column(name = "名称")
	private String stockName;
	@Column(name = "收盘价")
	private String closingPrice;
	@Column(name = "最高价")
	private String highestPrice;
	@Column(name = "最低价")
	private String lowestPrice;
	@Column(name = "开盘价")
	private String openingPrice;
	@Column(name = "前收盘")
	private String lastClosingPrice;
	@Column(name = "涨跌额")
	private String deltaPrice;
	@Column(name = "涨跌幅")
	private String deltaRate;
	@Column(name = "换手率")
	private String exchangeRate;
	@Column(name = "成交量")
	private String exchangeVol;
	@Column(name = "成交金额")
	private String exchangeFunds;
	@Column(name = "总市值")
	private String totalValue;
	@Column(name = "流通市值")
	private String tradableValue;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(String closingPrice) {
		this.closingPrice = closingPrice;
	}

	public String getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(String highestPrice) {
		this.highestPrice = highestPrice;
	}

	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(String openingPrice) {
		this.openingPrice = openingPrice;
	}

	public String getLastClosingPrice() {
		return lastClosingPrice;
	}

	public void setLastClosingPrice(String lastClosingPrice) {
		this.lastClosingPrice = lastClosingPrice;
	}

	public String getDeltaPrice() {
		return deltaPrice;
	}

	public void setDeltaPrice(String deltaPrice) {
		this.deltaPrice = deltaPrice;
	}

	public String getDeltaRate() {
		return deltaRate;
	}

	public void setDeltaRate(String deltaRate) {
		this.deltaRate = deltaRate;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getExchangeVol() {
		return exchangeVol;
	}

	public void setExchangeVol(String exchangeVol) {
		this.exchangeVol = exchangeVol;
	}

	public String getExchangeFunds() {
		return exchangeFunds;
	}

	public void setExchangeFunds(String exchangeFunds) {
		this.exchangeFunds = exchangeFunds;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getTradableValue() {
		return tradableValue;
	}

	public void setTradableValue(String tradableValue) {
		this.tradableValue = tradableValue;
	}

}
