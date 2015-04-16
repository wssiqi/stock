package com.dev.web.model;

/**
 * 日期 收盘价 涨跌幅 换手率 资金流入（万元） 资金流出（万元） 净流入（万元） 主力流入（万元） 主力流出（万元） 主力净流入（万元）
 * 
 * @author qsi
 * 
 */
public class FundFlowModel {
    enum Type {
        日期, 收盘价, 涨跌幅, 换手率, 资金流入, 资金流出, 净流入, 主力流入, 主力流出, 主力净流入
    }

    private String date;
    private String closingPrice;
    private String deltaPriceRate;
    private String turnOverRate;
    private String inFlow;
    private String outFlow;
    private String netInFlow;
    private String inFlowForMain;
    private String outFlowForMain;
    private String netInFlowMain;

    public static void main(String[] args) {
        System.out.println(Type.日期);

    }
}
