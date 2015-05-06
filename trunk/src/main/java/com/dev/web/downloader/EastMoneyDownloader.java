package com.dev.web.downloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dev.web.CommUtils;

public class EastMoneyDownloader {

    private static final Logger LOGGER = Logger.getLogger(EastMoneyDownloader.class);
    private Map<String, Object> resultMap = new HashMap<String, Object>();
    private final List<String> stockIdList;
    private ExecutorService threadPool;
    private List<Future<DownloadTask>> taskResultList;

    public EastMoneyDownloader(List<String> stockIdList) {
        this.stockIdList = stockIdList;
    }

    public void download() {
        try {
            beforeDownload();
            submitDownloadTasks();
            awaitDownloadTasksToBeDone();
        } finally {
            clearDownloadState();
        }
    }

    private void beforeDownload() {
        threadPool = Executors.newFixedThreadPool(10);
        taskResultList = new ArrayList<Future<DownloadTask>>();
        resultMap.clear();
    }

    private void submitDownloadTasks() {
        for (String stockId : stockIdList) {
            Future<DownloadTask> taskResult = threadPool.submit(new DownloadTask(stockId));
            taskResultList.add(taskResult);
        }
        threadPool.shutdown();
    }

    private void awaitDownloadTasksToBeDone() {
        for (Future<DownloadTask> future : taskResultList) {
            try {
                DownloadTask task = future.get();
                resultMap.put(task.getStockId(), task.getResultObj());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                List<Runnable> noneExecuteTasks = threadPool.shutdownNow();
                List<Future<DownloadTask>> runningTaskResultList = new ArrayList<Future<DownloadTask>>(taskResultList);
                runningTaskResultList.removeAll(noneExecuteTasks);
                waitTaskDone(runningTaskResultList);
                throw new RuntimeException(e);
            }
        }
    }

    private void clearDownloadState() {
        threadPool = null;
        taskResultList = null;
    }

    private void waitTaskDone(List<Future<DownloadTask>> allTaskCopy) {
        for (Future<DownloadTask> future : allTaskCopy) {
            try {
                DownloadTask downloadTask = future.get();
                resultMap.put(downloadTask.getStockId(), downloadTask.getResultObj());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    class DownloadTask implements Callable<DownloadTask> {

        private final String stockId;
        private Object resultObj;

        public DownloadTask(String stockId) {
            this.stockId = stockId;
        }

        public String getStockId() {
            return stockId;
        }

        public Object getResultObj() {
            return resultObj;
        }

        public DownloadTask call() throws Exception {
            try {
                URL url = new URL(String.format("http://data.eastmoney.com/zjlx/%s.html", stockId));
                Document doc = Jsoup.parse(url, 10000);
                Elements trList = doc.select("table.tab1 tbody tr");
                List<List<String>> rows = new ArrayList<List<String>>();
                for (Element tr : trList) {
                    List<String> row = new ArrayList<String>();
                    for (Element td : tr.children()) {
                        row.add(CommUtils.formatNumber(td.text()));
                    }
                    if (rows.size() < 10) {
                        break;
                    }
                    rows.add(row);
                }
                resultObj = rows;
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Map<String, Object> getDownloaded() {
        return resultMap;

    }

    public List<String> getNoneDownloadedStockIds() {
        List<String> stockIdListCopy = new ArrayList<String>(stockIdList);
        stockIdListCopy.removeAll(resultMap.keySet());
        return stockIdListCopy;
    }
}
