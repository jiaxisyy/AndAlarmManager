package com.babacit.alarm.entity;


public class Message {
	/** 消息Id */
	private int id;
	/** 语音消息url */
	private String messageUrl;
	/** 语音时长，单位：秒 */
	private int voiceTime;
	/** 语音发送人 */
	private UserInfo user;
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

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
