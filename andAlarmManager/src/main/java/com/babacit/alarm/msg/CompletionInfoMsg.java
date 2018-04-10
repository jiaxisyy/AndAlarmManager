package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class CompletionInfoMsg extends BaseMsg {
	/** 完成数 */
	private int completeCount;
	/** 未完成数 */
	private int incompleteCount;
	/**
	 * 类型 1：本周；2：本月；3：本年
	 */
	private int type;
	private int index;

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public int getIncompleteCount() {
		return incompleteCount;
	}

	public void setIncompleteCount(int incompleteCount) {
		this.incompleteCount = incompleteCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("completeCount")) {
					completeCount = json.getInt("completeCount");
				}
				if (json.has("incompleteCount")) {
					incompleteCount = json.getInt("incompleteCount");
				}
				if (json.has("type")) {
					type = json.getInt("type");
				}
				if (json.has("index")) {
					index = json.getInt("index");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
