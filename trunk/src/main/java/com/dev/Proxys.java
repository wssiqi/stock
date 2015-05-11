package com.dev;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Proxys {

    private static final String PROXY_LIST = "proxy.list";
    public static List<Proxy> PROXYLIST = new ArrayList<Proxy>();
    static{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROXY_LIST));
            PROXYLIST = (List<Proxy>) ois.readObject();
            ois.close();
        } catch (Exception e) {
        }
    }

    static class Proxy implements Comparable<Proxy>, Serializable {
        private static final long serialVersionUID = 1L;
        public String ip;
        public String port;
        public String protocol;
        public long speed;

        @Override
        public String toString() {
            return "Proxy [ip=" + ip + ", port=" + port + ", protocol=" + protocol + ", speed=" + speed + "ms]";
        }

        public int compareTo(Proxy o) {
            return (int) (speed - o.speed);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Proxy> list = getProxyList();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROXY_LIST));
        oos.writeObject(list);
        oos.close();
    }

    private static List<Proxy> getProxyList() {
        List<Proxy> proxyList = new ArrayList<Proxys.Proxy>();
        Document html = getHtmlContent("http://www.xici.net.co/");
        Elements trs = html.select("table#ip_list tbody tr");
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            if (tds.isEmpty()) {
                continue;
            }
            Proxy proxy = new Proxy();
            proxy.ip = tds.get(1).text();
            proxy.port = tds.get(2).text();
            proxy.protocol = tds.get(5).text();
            try {
                long s = System.currentTimeMillis();
                boolean isStock = "socks4/5".equals(proxy.protocol);
                if (isStock) {
                    continue;
                }
                URLConnection conn = new URL("http://m.baidu.com/").openConnection(new java.net.Proxy(Type.HTTP,
                        new InetSocketAddress(proxy.ip, Integer.valueOf(proxy.port))));
                conn.setConnectTimeout(1000);
                conn.setReadTimeout(2000);
                if (!conn.getHeaderFields().isEmpty()) {
                    long e = System.currentTimeMillis();
                    proxy.speed = e - s;
                    proxyList.add(proxy);
                    Logger.getLogger(Proxys.class).info("add proxy to proxy list " + proxy);
                }
            } catch (Exception e) {
            }
        }
        Collections.sort(proxyList);
        return proxyList;
    }

    private static Document getHtmlContent(String url) {
        int i = 10;
        Exception lastException = null;
        do {
            try {
                return Jsoup.parse(new URL(url), 10000);
            } catch (Exception e) {
                lastException = e;
            }
        } while (i-- > 0);
        throw new RuntimeException(lastException);
    }
}
