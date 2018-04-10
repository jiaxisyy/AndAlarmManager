package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class AlarmClockMsg extends BaseMsg {
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
	private List<RingMsg> ringList;
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

	public List<RingMsg> getRingList() {
		return ringList;
	}

	public void setRingList(List<RingMsg> ringList) {
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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getSortTime() {
		return sortTime;
	}

	public void setSortTime(String sortTime) {
		this.sortTime = sortTime;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json == null) {
			json = new JSONObject();
		}
		try {
			if (!TextUtils.isEmpty(clockId)) {
				json.put("clockId", clockId);
			}
			if (!TextUtils.isEmpty(title)) {
				json.put("title", title);
			}
			if (TextUtils.isEmpty(day)) {
				json.put("day", "");
			}else{
				json.put("day", day);
			}
			json.put("status", status);
			if (times != null && times.size() > 0) {
				JSONArray timeJsons = new JSONArray();
				for (String time : times) {
					timeJsons.put(time);
				}
				json.put("times", timeJsons);
			}
			json.put("clockType", clockType);
			json.put("timeType", timeType);
			json.put("dateType", dateType);
			json.put("holidayId", holidayId);
			json.put("holidayFilter", holidayFilter);
			if (!TextUtils.isEmpty(date)) {
				json.put("date", date);
			}
			json.put("delayTime", delayTime);
			json.put("earlyTime", earlyTime);
			if (ringList != null && ringList.size() > 0) {
				JSONArray ringJsons = new JSONArray();
				for (RingMsg ringEntity : ringList) {
					ringJsons.put(ringEntity.toJson(null));
				}
				json.put("ringList", ringJsons);
			}
			json.put("needRemind", needRemind);
			if (!TextUtils.isEmpty(remark)) {
				json.put("remark", remark);
			}
			if (!TextUtils.isEmpty(sortTime)) {
				json.put("sortTime", sortTime);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("clockId")) {
					clockId = json.getString("clockId");
				}
				if (json.has("title")) {
					title = json.getString("title");
				}
				if (json.has("status")) {
					status = json.getInt("status");
				}
				if (json.has("times")) {
					JSONArray timeJsons = json.getJSONArray("times");
					if (timeJsons != null && timeJsons.length() > 0) {
						times = new ArrayList<String>();
						for (int i = 0; i < timeJsons.length(); i++) {
							String time = timeJsons.getString(i);
							times.add(time);
						}
					}
				}
				if (json.has("clockType")) {
					clockType = json.getInt("clockType");
				}
				if (json.has("timeType")) {
					timeType = json.getInt("timeType");
				}
				if (json.has("day")) {
					day = json.getString("day");
				}
				if (json.has("dateType")) {
					dateType = json.getInt("dateType");
				}
				if (json.has("holidayFilter")) {
					holidayFilter = json.getInt("holidayFilter");
				}
				if (json.has("holidayId")) {
					holidayId = json.getInt("holidayId");
				}
				if (json.has("date")) {
					date = json.getString("date");
				}
				if (json.has("delayTime")) {
					delayTime = json.getInt("delayTime");
				}
				if (json.has("earlyTime")) {
					earlyTime = json.getInt("earlyTime");
				}
				if (json.has("ringList")) {
					JSONArray ringJsons = json.getJSONArray("ringList");
					if (ringJsons != null && ringJsons.length() > 0) {
						ringList = new ArrayList<RingMsg>();
						for (int i = 0; i < ringJsons.length(); i++) {
							JSONObject ringJson = ringJsons.getJSONObject(i);
							RingMsg ringEntity = new RingMsg();
							ringEntity.parseJson(ringJson);
							ringList.add(ringEntity);
						}
					}
				}
				if (json.has("needRemind")) {
					needRemind = json.getInt("needRemind");
				}
				if (json.has("remark")) {
					remark = json.getString("remark");
				}
				if (json.has("sortTime")) {
					sortTime = json.getString("sortTime");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
