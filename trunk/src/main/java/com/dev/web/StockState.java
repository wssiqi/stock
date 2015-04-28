package com.dev.web;

public class StockState {

	private final String 股票代码;
	private String 当前价;
	private String 涨跌额;
	private String 涨跌幅;
	private String 换手率;
	private String 更新时间;
	private String 昨收盘价;
	private String 今开盘价;
	private String 最高价;
	private String 最低价;
	private String 成交额;
	private String 成交量;
	private String 量比;
	private String 市盈率;
	private String 内盘;
	private String 外盘;

	public StockState(String 股票代码) {
		this.股票代码 = 股票代码;
		initPropertyFromWeb();
	}

	private void initPropertyFromWeb() {
		String stockPageUrl = String.format(
				"http://wap.eastmoney.com/StockInfo.aspx?stockcode=%s&vt=0",
				股票代码);
		String pageContent = CommUtils.getWebPageContent(stockPageUrl);
		this.当前价 = CommUtils.extractRegexGroup1(pageContent,
				"当前价：([0-9\\.]+)元");
		this.涨跌额 = CommUtils.extractRegexGroup1(pageContent,
				"涨跌额：([0-9\\.\\-]+)");
		this.涨跌幅 = CommUtils.extractRegexGroup1(pageContent,
				"涨跌幅：([0-9\\.\\-\\%]+)");
		this.换手率 = CommUtils.extractRegexGroup1(pageContent,
				"换手率：([0-9\\.\\-\\%]+)");
		this.更新时间 = CommUtils.extractRegexGroup1(pageContent,
				"更新时间：([0-9\\.\\-: ]+)");

		this.昨收盘价 = CommUtils.extractRegexGroup1(pageContent,
				"昨收盘：([0-9\\.\\-: ]+)");
		this.今开盘价 = CommUtils.extractRegexGroup1(pageContent,
				"今开盘：([0-9\\.\\-: ]+)");
		this.最高价 = CommUtils.extractRegexGroup1(pageContent,
				"最高价：([0-9\\.\\-: ]+)");
		this.最低价 = CommUtils.extractRegexGroup1(pageContent,
				"最低价：([0-9\\.\\-: ]+)");
		this.成交量 = CommUtils.extractRegexGroup1(pageContent,
				"成交量：([0-9\\.\\-: ]+)");
		this.成交额 = CommUtils.extractRegexGroup1(pageContent,
				"成交额：([0-9\\.\\-: ]+)");

		this.量比 = CommUtils.extractRegexGroup1(pageContent,
				"量比：([0-9\\.\\-: ]+)");
		this.市盈率 = CommUtils.extractRegexGroup1(pageContent,
				"市盈：([0-9\\.\\-: ]+)");
		this.内盘 = CommUtils.extractRegexGroup1(pageContent,
				"内盘：([0-9\\.\\-: ]+)");
		this.外盘 = CommUtils.extractRegexGroup1(pageContent,
				"外盘：([0-9\\.\\-: ]+)");
	}

	public static StockState get(String 股票代码) {
		return new StockState(股票代码);
	}

	public String get股票代码() {
		return 股票代码;
	}

	public String get当前价() {
		return 当前价;
	}

	public String get涨跌额() {
		return 涨跌额;
	}

	public String get涨跌幅() {
		return 涨跌幅;
	}

	public String get换手率() {
		return 换手率;
	}

	public String get更新时间() {
		return 更新时间;
	}

	public String get昨收盘价() {
		return 昨收盘价;
	}

	public String get今开盘价() {
		return 今开盘价;
	}

	public String get最高价() {
		return 最高价;
	}

	public String get最低价() {
		return 最低价;
	}

	public String get成交额() {
		return 成交额;
	}

	public String get成交量() {
		return 成交量;
	}

	public String get量比() {
		return 量比;
	}

	public String get市盈率() {
		return 市盈率;
	}

	public String get内盘() {
		return 内盘;
	}

	public String get外盘() {
		return 外盘;
	}

	@Override
	public String toString() {
		return "StockState [股票代码=" + 股票代码 + ", 当前价=" + 当前价 + ", 涨跌额=" + 涨跌额
				+ ", 涨跌幅=" + 涨跌幅 + ", 换手率=" + 换手率 + ", 更新时间=" + 更新时间
				+ ", 昨收盘价=" + 昨收盘价 + ", 今开盘价=" + 今开盘价 + ", 最高价=" + 最高价
				+ ", 最低价=" + 最低价 + ", 成交额=" + 成交额 + ", 成交量=" + 成交量 + ", 量比="
				+ 量比 + ", 市盈率=" + 市盈率 + ", 内盘=" + 内盘 + ", 外盘=" + 外盘 + "]";
	}

}
