package com.dev.web;

import java.io.File;

public abstract class TestUtil {
    /** 资源文件根目录 */
    public static String RESOURCE_HOME = new File(TestUtil.class.getClassLoader().getResource("").getPath())
            .getAbsolutePath();
    /** 测试数据文件Workspace目录 */
    public static String WS_HOME = new File(RESOURCE_HOME, "workspace").getAbsolutePath();

    public static void main(String[] args) {
        System.out.println(RESOURCE_HOME);
        File csv = new File(RESOURCE_HOME, "000001.csv");
        System.out.println(csv.exists());
    }
}
