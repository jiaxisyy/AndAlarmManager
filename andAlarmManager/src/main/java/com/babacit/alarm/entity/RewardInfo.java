package com.babacit.alarm.entity;


public class RewardInfo {
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

}
