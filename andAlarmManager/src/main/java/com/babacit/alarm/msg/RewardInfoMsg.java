package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class RewardInfoMsg extends BaseMsg {
	/** 目前奖励数 */
	private int curCount;
	/** 目标奖励数 */
	private int targetCount;
	/**
	 * 周期类型 1：周；2：月；3：年
	 */
	private int timeType;
	/** 鼓励内容 */
	private String encourage;

	public int getCurCount() {
		return curCount;
	}

	public void setCurCount(int curCount) {
		this.curCount = curCount;
	}

	public int getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public String getEncourage() {
		return encourage;
	}

	public void setEncourage(String encourage) {
		this.encourage = encourage;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("curCount")) {
					curCount = json.getInt("curCount");
				}
				if (json.has("targetCount")) {
					targetCount = json.getInt("targetCount");
				}
				if (json.has("timeType")) {
					timeType = json.getInt("timeType");
				}
				if (json.has("encourage")) {
					encourage = json.getString("encourage");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json == null) {
			json = new JSONObject();
		}
		try {
			json.put("curCount", curCount);
			json.put("targetCount", targetCount);
			json.put("timeType", timeType);
			if (!TextUtils.isEmpty(encourage)) {
				json.put("encourage", encourage);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}
}
