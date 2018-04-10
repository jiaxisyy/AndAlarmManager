package com.babacit.alarm.msg;

import org.json.JSONObject;

public class PushInfoMsg extends BaseMsg {
	private int id;
	private int pushType;
	private String pushMsg;
	private String pushUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPushType() {
		return pushType;
	}

	public void setPushType(int pushType) {
		this.pushType = pushType;
	}

	public String getPushMsg() {
		return pushMsg;
	}

	public void setPushMsg(String pushMsg) {
		this.pushMsg = pushMsg;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("id")) {
					id = json.getInt("id");
				}
				if (json.has("pushType")) {
					pushType = json.getInt("pushType");
				}
				if (json.has("pushMsg")) {
					pushMsg = json.getString("pushMsg");
				}
				if (json.has("pushUrl")) {
					pushUrl = json.getString("pushUrl");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}

}
