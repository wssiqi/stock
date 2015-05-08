package com.dev;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public abstract class Utils {
    static Map<Character, BigDecimal> NUM_UNIT = new HashMap<Character, BigDecimal>();
    static {
        NUM_UNIT.put('-', new BigDecimal("0"));
        NUM_UNIT.put('%', new BigDecimal("1"));
        NUM_UNIT.put('万', new BigDecimal("10000"));
        NUM_UNIT.put('亿', new BigDecimal("100000000"));
    }

    public static List<String> getRegexMatchs(String regex, String str) {
        List<String> matchList = new ArrayList<String>();
        Matcher m = Pattern.compile(regex).matcher(str);
        while (m.find()) {
            matchList.add(m.group());
        }
        return matchList;
    }

    public static String getFirstMatch(String regex, String input) {
        Matcher m = Pattern.compile(regex).matcher(input);
        while (m.find()) {
            return m.group();
        }
        return "";
    }

    public static String translateToNum(String num) {
        num = StringUtils.trimToEmpty(num);
        if (num.isEmpty()) {
            num = "0";
        }

        int lastIndex = num.length() - 1;
        char lastChar = num.charAt(lastIndex);
        BigDecimal unitDecimal = NUM_UNIT.get(lastChar);
        if (unitDecimal == null) {
            return num;
        }

        String subStr = num.substring(0, lastIndex);
        if (subStr.isEmpty()) {
            return "0";
        }
        BigDecimal digitDecimal = new BigDecimal(subStr);
        return digitDecimal.multiply(unitDecimal).toString();
    }

    public static String getDateTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
