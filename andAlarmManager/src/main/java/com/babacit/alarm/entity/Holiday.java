package com.babacit.alarm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Holiday")
public class Holiday implements Parcelable {
	/** 自增长主键的ID */
	@DatabaseField(generatedId = true)
	private int id;
	/** 节日Id */
	@DatabaseField
	private int holidayId;
	/** 节日名称 */
	@DatabaseField
	private String holidayName;
	/** 节日具体时间 格式：yyyy-mm-dd */
	@DatabaseField
	private String holidayTime;
	/** 日期类型 0：公历 1：农历 */
	@DatabaseField
	private int dateType;

	public Holiday() {}
	
	public int getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getHolidayTime() {
		return holidayTime;
	}

	public void setHolidayTime(String holidayTime) {
		this.holidayTime = holidayTime;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(holidayId);
		dest.writeString(holidayTime);
		dest.writeString(holidayName);
		dest.writeInt(dateType);
	}

	public static final Parcelable.Creator<Holiday> CREATEOR = new Creator<Holiday>() {

		@Override
		public Holiday createFromParcel(Parcel source) {
			return new Holiday(source);
		}

		@Override
		public Holiday[] newArray(int size) {
			return new Holiday[size];
		}
	};

	public Holiday(Parcel in) {
		this.holidayId = in.readInt();
		this.holidayName = in.readString();
		this.holidayTime = in.readString();
		this.dateType = in.readInt();
	}
}
