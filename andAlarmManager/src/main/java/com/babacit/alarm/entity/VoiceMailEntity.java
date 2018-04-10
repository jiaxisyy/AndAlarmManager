package com.babacit.alarm.entity;

public class VoiceMailEntity {
	private String name;

	private String date;

	private String text;

	private String time;

	private boolean isComMeg = true;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public VoiceMailEntity() {

	}

	public VoiceMailEntity(String name, String date, String text,
			boolean isComMsg) {
		this.name = name;
		this.date = date;
		this.text = text;
		this.isComMeg = isComMsg;
	}
}