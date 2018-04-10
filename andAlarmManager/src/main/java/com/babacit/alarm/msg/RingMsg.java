package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class RingMsg extends BaseMsg {
	/** 铃声类型 1：闹钟铃声 2：提醒铃声 3：提醒自定义语音 */
	private int ringType;
	/** 铃声分类 1：铃声 2：音乐 3：语音 */
	private int ringCategory;
	/** 铃声名称 */
	private String ringName;
	/** 铃声id */
	private int ringId;
	/** 铃声url */
	private String ringUrl;

	public int getRingType() {
		return ringType;
	}

	public void setRingType(int ringType) {
		this.ringType = ringType;
	}

	public int getRingCategory() {
		return ringCategory;
	}

	public void setRingCategory(int ringCategory) {
		this.ringCategory = ringCategory;
	}

	public String getRingName() {
		return ringName;
	}

	public void setRingName(String ringName) {
		this.ringName = ringName;
	}

	public int getRingId() {
		return ringId;
	}

	public void setRingId(int ringId) {
		this.ringId = ringId;
	}

	public String getRingUrl() {
		return ringUrl;
	}

	public void setRingUrl(String ringUrl) {
		this.ringUrl = ringUrl;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("ringType")) {
					ringType = json.getInt("ringType");
				}
				if (json.has("ringCategory")) {
					ringCategory = json.getInt("ringCategory");
				}
				if (json.has("ringName")) {
					ringName = json.getString("ringName");
				}
				if (json.has("id")) {
					ringId = json.getInt("id");
				}
				if (json.has("ringUrl")) {
					ringUrl = json.getString("ringUrl");
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
			json.put("ringType", ringType);
			json.put("ringCategory", ringCategory);
			if (!TextUtils.isEmpty(ringName)) {
				json.put("ringName", ringName);
			}
			json.put("id", ringId);
			if (!TextUtils.isEmpty(ringUrl)) {
				json.put("ringUrl", ringUrl);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}
}
