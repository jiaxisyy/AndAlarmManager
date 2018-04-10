package com.babacit.alarm.entity;

public class AlarmBaseInfo {
	/** 睡眠开始时间 */
	private String sleepStartTime;
	/** 睡眠结束时间 */
	private String sleepEndTime;
	/** 电量百分比 */
	private String batteryPercent;
	/**
	 * 充电状态 0：未充电；1：充电中； 2：充电完成
	 */
	private int chargeStatus;
	/**
	 * 终端触屏状态 0：不可用；1：可用
	 */
	private int touchScreenStatus;
	/**
	 * 整点报时状态 0：不可用；1：可用
	 */
	private int reportStatus;
	/** 睡眠开始时间 */
	private String reportStartTime;
	/** 睡眠结束时间 */
	private String reportEndTime;
	/** 暑假时间 */
	private String summerVacationTime;
	/** 寒假时间 */
	private String winterVacationTime;

	public String getSleepStartTime() {
		return sleepStartTime;
	}

	public void setSleepStartTime(String sleepStartTime) {
		this.sleepStartTime = sleepStartTime;
	}

	public String getSleepEndTime() {
		return sleepEndTime;
	}

	public void setSleepEndTime(String sleepEndTime) {
		this.sleepEndTime = sleepEndTime;
	}

	public String getBatteryPercent() {
		return batteryPercent;
	}

	public void setBatteryPercent(String batteryPercent) {
		this.batteryPercent = batteryPercent;
	}

	public int getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(int chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public int getTouchScreenStatus() {
		return touchScreenStatus;
	}

	public void setTouchScreenStatus(int touchScreenStatus) {
		this.touchScreenStatus = touchScreenStatus;
	}

	public int getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReportStartTime() {
		return reportStartTime;
	}

	public void setReportStartTime(String reportStartTime) {
		this.reportStartTime = reportStartTime;
	}

	public String getReportEndTime() {
		return reportEndTime;
	}

	public void setReportEndTime(String reportEndTime) {
		this.reportEndTime = reportEndTime;
	}

	public String getSummerVacationTime() {
		return summerVacationTime;
	}

	public void setSummerVacationTime(String summerVacationTime) {
		this.summerVacationTime = summerVacationTime;
	}

	public String getWinterVacationTime() {
		return winterVacationTime;
	}

	public void setWinterVacationTime(String winterVacationTime) {
		this.winterVacationTime = winterVacationTime;
	}

}
