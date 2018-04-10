package com.babacit.alarm.entity;

public class DayCompletionInfo {
	/** 完成数 */
	private int completeCount;
	/**
	 * 周几 0：周一，1：周二……
	 */
	private int day;

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
