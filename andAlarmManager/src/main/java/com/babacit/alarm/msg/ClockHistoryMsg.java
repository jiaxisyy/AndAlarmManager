package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class ClockHistoryMsg extends BaseMsg {
	/** 记录id */
	private String clockId;
	/** 标题 */
	private String title;
	/**
	 * 闹钟完成时间 格式：yyyy-MM-dd HH:mm
	 */
	private String date;
	/**
	 * 状态 0：未完成，1：已完成
	 */
	private int finishStatus;
	/** 奖励状态：0：无需奖励 1：未奖励 2：已奖励 */
	private int rewardStatus;

	public String getClockId() {
		return clockId;
	}

	public void setClockId(String clockId) {
		this.clockId = clockId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(int finishStatus) {
		this.finishStatus = finishStatus;
	}

	public int getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(int rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("clockId")) {
					clockId = json.getString("clockId");
				}
				if (json.has("title")) {
					title = json.getString("title");
				}
				if (json.has("date")) {
					date = json.getString("date");
				}
				if (json.has("finishStatus")) {
					finishStatus = json.getInt("finishStatus");
				}
				if (json.has("rewardStatus")) {
					rewardStatus = json.getInt("rewardStatus");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}

}
