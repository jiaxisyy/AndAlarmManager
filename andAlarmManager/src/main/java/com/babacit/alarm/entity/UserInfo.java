package com.babacit.alarm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "User")
public class UserInfo implements Parcelable {
	/** 自增长主键的ID */
	@DatabaseField(generatedId = true)
	private int id;
	/** 用户ID */
	@DatabaseField
	private String userId;
	/** 昵称 */
	@DatabaseField
	private String nickName;
	/** 头像地址 */
	@DatabaseField
	private String imgUrl;
	/** 姓名 */
	@DatabaseField
	private String name;
	/**
	 * 角色名称 爸爸、妈妈、爷爷、奶奶、自定义
	 */
	@DatabaseField
	private String roleInfo;
	/**
	 * 角色id
	 */
	@DatabaseField
	private int roleType;
	/**
	 * 生日 格式：yyyy-MM-dd
	 */
	@DatabaseField
	private String birthday;
	/** 手机号码 */
	@DatabaseField
	private String phoneNo;
	/** 职业 */
	@DatabaseField
	private int jobType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userId);
		dest.writeString(nickName);
		dest.writeString(imgUrl);
		dest.writeString(name);
		dest.writeString(roleInfo);
		dest.writeInt(roleType);
		dest.writeString(birthday);
		dest.writeString(phoneNo);
		dest.writeInt(jobType);
	}

	public static final Parcelable.Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
		@Override
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		@Override
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};

	public UserInfo(Parcel in) {
		this.userId = in.readString();
		this.nickName = in.readString();
		this.imgUrl = in.readString();
		this.name = in.readString();
		this.roleInfo = in.readString();
		this.roleType = in.readInt();
		this.birthday = in.readString();
		this.phoneNo = in.readString();
		this.jobType = in.readInt();
	}
	
	public UserInfo() {
	}
}
