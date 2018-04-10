package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HeartBeatResponse extends BaseResponse {
	/** 在线状态。0：离线；1：在线 */
	private int onlineStatus;
	/** 电量百分比 */
	private String batteryPercent;
	/**
	 * 充电状态 0：未充电；1：充电中； 2：充电完成
	 */
	private int chargeStatus;
	private List<PushInfoMsg> pushList= new ArrayList<PushInfoMsg>();

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
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

	public List<PushInfoMsg> getPushList() {
		return pushList;
	}

	public void setPushList(List<PushInfoMsg> pushList) {
		this.pushList = pushList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("onlineStatus"))
					onlineStatus = json.getInt("onlineStatus");
				if (json.has("batteryPercent"))
					batteryPercent = json.getString("batteryPercent");
				if (json.has("chargeStatus"))
					chargeStatus = json.getInt("chargeStatus");
				if (json.has("list")) {
					JSONArray pushJsons = json.getJSONArray("list");
					if (pushJsons != null && pushJsons.length() > 0) {
						for (int i = 0; i < pushJsons.length(); i++) {
							JSONObject pushJson = pushJsons.getJSONObject(i);
							if (pushJson != null) {
								PushInfoMsg push = new PushInfoMsg();
								push.parseJson(pushJson);
								pushList.add(push);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
