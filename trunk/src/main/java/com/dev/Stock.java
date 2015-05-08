package com.dev;

import org.apache.commons.lang.StringUtils;

public class Stock {

    public String code;
    public String name;
    public String maket;

    public boolean isValid() {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name) || StringUtils.isEmpty(maket)) {
            return false;
        }

        boolean isSHA = code.startsWith("600") || code.startsWith("601") || code.startsWith("603");
        boolean isSZA = code.startsWith("000") || code.startsWith("002");

        boolean isExit = code.contains("ST") || code.contains("退市");
        return !isExit && (isSHA || isSZA);
    }

    @Override
    public String toString() {
        return "Stock [code=" + code + ", name=" + name + ", maket=" + maket + "]";
    }
}
