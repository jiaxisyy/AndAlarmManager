package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageMsg extends BaseMsg {
	/** 消息Id */
	private int id;
	/** 语音消息url */
	private String messageUrl;
	/** 语音时长，单位：秒 */
	private int voiceTime;
	/** 语音发送人 */
	private UserInfoMsg user;
	/**
	 * 创建时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public UserInfoMsg getUser() {
		return user;
	}

	public void setUser(UserInfoMsg user) {
		this.user = user;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("id")) {
					id = json.getInt("id");
				}
				if (json.has("messageUrl")) {
					messageUrl = json.getString("messageUrl");
				}
				if (json.has("voiceTime")) {
					voiceTime = json.getInt("voiceTime");
				}
				if (json.has("user")) {
					JSONObject userJson = json.getJSONObject("user");
					user = new UserInfoMsg();
					user.parseJson(userJson);
				}
				if (json.has("createTime")) {
					createTime = json.getString("createTime");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
