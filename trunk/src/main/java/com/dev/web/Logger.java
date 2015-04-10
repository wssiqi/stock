package com.dev.web;

public class Logger {

	private static Logger ins = new Logger();

	private Logger() {
	}

	public static Logger getLogger(Class<?> clazz) {
		return ins;
	}

	public void warn(String message) {
		System.err.println(message);
	}

	public void info(String message) {
		warn(message);
	}

}
