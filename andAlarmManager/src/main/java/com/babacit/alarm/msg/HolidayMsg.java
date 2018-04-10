package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class HolidayMsg extends BaseMsg {
	/** 节日Id */
	private int holidayId;
	/** 节日名称 */
	private String holidayName;
	/**
	 * 节日具体时间 格式：yyyy-mm-dd
	 */
	private String holidayTime;
	/**
	 * 日期类型 0：公历，1：农历
	 */
	private int dateType;

	public int getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getHolidayTime() {
		return holidayTime;
	}

	public void setHolidayTime(String holidayTime) {
		this.holidayTime = holidayTime;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("holidayId")) {
					holidayId = json.getInt("id");
				}
				if (json.has("holidayName")) {
					holidayName = json.getString("holidayName");
				}
				if (json.has("holidayTime")) {
					holidayTime = json.getString("holidayTime");
				}
				if (json.has("dateType")) {
					dateType = json.getInt("dateType");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}

}
