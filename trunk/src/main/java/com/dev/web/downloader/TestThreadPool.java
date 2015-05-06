package com.dev.web.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
	static final class RunnableImpl implements Runnable {
		private final int threadId;

		public RunnableImpl(int threadId) {
			this.threadId = threadId;
		}

		public void run() {
			// Thread.sleep(1000);
			int sum = 0;
			for (int i = 0; i < 1000000000; i++) {
				sum += i;
			}
			throw new RuntimeException("");
		}
	}

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());
		List<Future<?>> futureList = new ArrayList<Future<?>>();
		for (int i = 0; i < 100; i++) {
			RunnableImpl task = new RunnableImpl(i);
			Future<?> submit = pool.submit(task);
			futureList.add(submit);
		}
		pool.shutdown();
		List<Runnable> shutdownNow = pool.shutdownNow();
		System.out.println(shutdownNow.size());
		try {
			System.out.println(System.currentTimeMillis());
			pool.awaitTermination(1, TimeUnit.DAYS);
			System.out.println(System.currentTimeMillis());
			futureList.removeAll(shutdownNow);
			for (Future<?> future : futureList) {
				System.out.println(future.get() + "");
			}
			System.out.println("Over");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
