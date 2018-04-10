package com.babacit.alarm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	private static SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat formatyyyyMM = new SimpleDateFormat(
			"yyyy-MM");
	private static SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat formatMMdd = new SimpleDateFormat("MM-dd");
	private static SimpleDateFormat formatMM = new SimpleDateFormat("MM");
	private static SimpleDateFormat formatdd = new SimpleDateFormat("dd");
	private static SimpleDateFormat formatWeek = new SimpleDateFormat("E");
	private static SimpleDateFormat formatHHmm = new SimpleDateFormat("HH:mm");

	public static String getDateStringYearMonthDay() {
		return formatyyyyMMdd.format(new Date());
	}

	public static String getDateStringYearMonthDay(Date date) {
		return formatyyyyMMdd.format(date);
	}

	public static String getDateStringYearMonth() {
		return formatyyyyMM.format(new Date());
	}

	public static String getDateStringYear() {
		return formatyyyy.format(new Date());
	}

	public static String getDateStringMonth() {
		return formatMM.format(new Date());
	}

	public static String getDateStringMonthDay() {
		return formatMMdd.format(new Date());
	}
	
	public static String getDateStringMonthDay(Date date) {
		return formatMMdd.format(date);
	}

	public static String getDateStringDay() {
		return formatdd.format(new Date());
	}
	
	public static String getDateStringDay(Date date) {
		return formatdd.format(date);
	}

	public static String getWeek() {
		return NumberUtils.weekStr(formatWeek.format(new Date()));
	}

	public static String getTime() {
		return formatHHmm.format(new Date());
	}

	/**
	 * 获取指定日期的后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getSpecialDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = formatyyyyMMdd.parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = formatyyyyMMdd.format(c.getTime());
		return dayAfter;
	}

	public static Date string2Date(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss a",
				Locale.ENGLISH);
		try {
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
