package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmBaseInfoMsg extends BaseMsg {
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

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("sleepStartTime")) {
					sleepStartTime = json.getString("sleepStartTime");
				}
				if (json.has("sleepEndTime")) {
					sleepEndTime = json.getString("sleepEndTime");
				}
				if (json.has("batteryPercent")) {
					batteryPercent = json.getString("batteryPercent");
				}
				if (json.has("chargeStatus")) {
					chargeStatus = json.getInt("chargeStatus");
				}
				if (json.has("touchScreenStatus")) {
					touchScreenStatus = json.getInt("touchScreenStatus");
				}
				if (json.has("reportStatus")) {
					reportStatus = json.getInt("reportStatus");
				}
				if (json.has("reportStartTime")) {
					reportStartTime = json.getString("reportStartTime");
				}
				if (json.has("reportEndTime")) {
					reportEndTime = json.getString("reportEndTime");
				}
				if (json.has("summerVacationTime")) {
					summerVacationTime = json.getString("summerVacationTime");
				}
				if (json.has("winterVacationTime")) {
					winterVacationTime = json.getString("winterVacationTime");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
