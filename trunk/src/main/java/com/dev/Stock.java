package com.dev;

import org.apache.commons.lang.StringUtils;

public class Stock {

    public String code;
    public String name;
    public String maket;

    public boolean isValid() {
        return !StringUtils.isEmpty(code) && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(maket)
                && code.matches("[603].+");
    }

    @Override
    public String toString() {
        return "Stock [code=" + code + ", name=" + name + ", maket=" + maket + "]";
    }
}
