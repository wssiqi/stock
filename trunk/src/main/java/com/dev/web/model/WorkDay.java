package com.dev.web.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkDay {
	private static List<String> workDayList = Arrays.asList("2015-02-02",
			"2015-02-03", "2015-02-04", "2015-02-05", "2015-02-06",
			"2015-02-09", "2015-02-10", "2015-02-11", "2015-02-12",
			"2015-02-13", "2015-02-16", "2015-02-17", "2015-02-25",
			"2015-02-26", "2015-02-27", "2015-03-02", "2015-03-03",
			"2015-03-04", "2015-03-05", "2015-03-06", "2015-03-09",
			"2015-03-10", "2015-03-11", "2015-03-12", "2015-03-13",
			"2015-03-16", "2015-03-17", "2015-03-18", "2015-03-19",
			"2015-03-20", "2015-03-23", "2015-03-24", "2015-03-25",
			"2015-03-26", "2015-03-27", "2015-03-30", "2015-03-31",
			"2015-04-01", "2015-04-02", "2015-04-03", "2015-04-07",
			"2015-04-08", "2015-04-09", "2015-04-10", "2015-04-13",
			"2015-04-14", "2015-04-15", "2015-04-16", "2015-04-17",
			"2015-04-20");

	public static void main(String[] args) {
		String s = WorkDay.getNextWorkDay("");
	}

	private static String getNextWorkDay(String currentDay) {
		int dayIndexWhichBeforeCurrent = -1;
		for (int i = 0; i < workDayList.size(); i++) {
			String workDay = workDayList.get(i);
			int compareResult = workDay.compareToIgnoreCase(currentDay);
			if (compareResult < 0) {
				dayIndexWhichBeforeCurrent = i;
			} else if (compareResult == 0) {
				getNextWorkDay(compareResult);
			}
		}
		return null;
	}

	private static String getNextWorkDay(int currentDayIndex) {
		if (currentDayIndex > workDayList.size() - 1) {
			return null;
		}

		return null;
	}
}
