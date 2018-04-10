package com.babacit.alarm.entity;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmInfo implements Parcelable {
	/** 闹钟的id */
	private String clockId;
	/** 标题 */
	private String title;
	/** 闹钟状态：0：关闭 1：开启 */
	private int status;
	/** 时间列表 */
	private List<String> times;
	/**
	 * 闹钟类型 1：起床闹钟；2：作业闹钟； 3：生日管家；4：日常提醒； 5：眼保健操；6：纪念日； 7：喝水/吃药；8：节日提醒； 9：自定义
	 */
	private int clockType;
	/**
	 * 周期类型 1：仅一次；2：每天； 3：每月；4：每年 5：自定义
	 */
	private int timeType;
	/**
	 * 当timetype为自定义时，此字段放周几，0：周一，1：周二… 可以保存多个，用逗号隔开 如： 0,3,4,5
	 */
	private String day;
	/** 0：公历 1：农历 */
	private int dateType;
	/** 节假日Id */
	private int holidayId;
	/** 1：仅寒暑假提醒 2：寒暑假不提醒 3：节假日不提醒 */
	private int holidayFilter;
	/** 日期，表示某一天的闹钟 */
	private String date;
	/** 延时时间，即小睡（单位：分钟） */
	private int delayTime;
	/** 提前提醒时间 */
	private int earlyTime;
	/** 铃声 */
	private List<RingInfo> ringList;
	/**
	 * 是否需要提醒， 0：不需要，1：需要
	 */
	private int needRemind;
	/** 备注 */
	private String remark;
	private String sortTime;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getTimes() {
		return times;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	public int getClockType() {
		return clockType;
	}

	public void setClockType(int clockType) {
		this.clockType = clockType;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public int getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}

	public int getHolidayFilter() {
		return holidayFilter;
	}

	public void setHolidayFilter(int holidayFilter) {
		this.holidayFilter = holidayFilter;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getEarlyTime() {
		return earlyTime;
	}

	public void setEarlyTime(int earlyTime) {
		this.earlyTime = earlyTime;
	}

	public List<RingInfo> getRingList() {
		return ringList;
	}

	public void setRingList(List<RingInfo> ringList) {
		this.ringList = ringList;
	}

	public int getNeedRemind() {
		return needRemind;
	}

	public void setNeedRemind(int needRemind) {
		this.needRemind = needRemind;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSortTime() {
		return sortTime;
	}

	public void setSortTime(String sortTime) {
		this.sortTime = sortTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(clockId);
		dest.writeString(title);
		dest.writeInt(status);
		dest.writeInt(clockType);
		dest.writeInt(timeType);
		dest.writeString(day);
		dest.writeInt(dateType);
		dest.writeInt(holidayId);
		dest.writeInt(delayTime);
		dest.writeInt(earlyTime);
		dest.writeInt(holidayFilter);
		dest.writeString(date);
		dest.writeInt(needRemind);
		dest.writeString(remark);
		dest.writeList(times);
		dest.writeTypedList(ringList);
		dest.writeString(sortTime);
	}

	public static final Parcelable.Creator<AlarmInfo> CREATOR = new Creator<AlarmInfo>() {
		@Override
		public AlarmInfo createFromParcel(Parcel source) {
			return new AlarmInfo(source);
		}

		@Override
		public AlarmInfo[] newArray(int size) {
			return new AlarmInfo[size];
		}

	};

	public AlarmInfo(Parcel in) {
		this.clockId = in.readString();
		this.title = in.readString();
		this.status = in.readInt();
		this.clockType = in.readInt();
		this.timeType = in.readInt();
		this.day = in.readString();
		this.dateType = in.readInt();
		this.holidayId = in.readInt();
		this.delayTime = in.readInt();
		this.earlyTime = in.readInt();
		this.holidayFilter = in.readInt();
		this.date = in.readString();
		this.needRemind = in.readInt();
		this.remark = in.readString();
		this.times = in.readArrayList(List.class.getClassLoader());
		List<RingInfo> rings = new ArrayList<RingInfo>();
		in.readTypedList(rings, RingInfo.CREATOR);
		this.ringList = rings;
		this.sortTime = in.readString();

		// this.ringList = in.readArrayList(List.class.getClassLoader());
	}

	public AlarmInfo() {
	}
}
