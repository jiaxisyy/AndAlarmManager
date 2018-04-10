package com.babacit.alarm.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class RingInfo implements Parcelable {
	private int ringType;
	private int ringCategory;
	private String ringName;
	private int ringId;
	private String ringUrl;
	private boolean check = false;

	public RingInfo() {
	}

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

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<RingInfo> CREATOR = new Creator<RingInfo>() {

		@Override
		public RingInfo createFromParcel(Parcel source) {
			return new RingInfo(source);
		}

		@Override
		public RingInfo[] newArray(int size) {
			return new RingInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ringType);
		dest.writeInt(ringCategory);
		dest.writeString(ringName);
		dest.writeInt(ringId);
		dest.writeString(ringUrl);
	}

	public RingInfo(Parcel in) {
		this.ringType = in.readInt();
		this.ringCategory = in.readInt();
		this.ringName = in.readString();
		this.ringId = in.readInt();
		this.ringUrl = in.readString();
	}

}
