package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class BaseInfo extends BaseMsg {

	/** 设备编号IMEI */
	private String deviceCode;
	/** 手机型号 */
	private String mobileModel;
	/** 手机厂商 */
	private String manufacture;
	/** 系统版本名 */
	private String osVersionName;
	/** 系统版本号 */
	private int osVersionCode;
	/** 应用包名 */
	private String appPackageName;
	/** 应用版本名称 */
	private String appVersionName;
	/** 应用版本号 */
	private int appVersionCode;
	/** 应用安装时间 */
	private long appInstallTime;
	/** 平台。0：android；1：ios */
	private int platform;
	/**
	 * 网络类型， 1：wifi，2：mobile
	 */
	private int dataNetworkType;
	/** Wifi的mac地址 */
	private String wifiMacAddress;
	/** Sim卡序列号 */
	private String IMSI;
	/** 渠道号 */
	private String channelCode;

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public int getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(int appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public long getAppInstallTime() {
		return appInstallTime;
	}

	public void setAppInstallTime(long appInstallTime) {
		this.appInstallTime = appInstallTime;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getOsVersionName() {
		return osVersionName;
	}

	public void setOsVersionName(String osVersionName) {
		this.osVersionName = osVersionName;
	}

	public int getOsVersionCode() {
		return osVersionCode;
	}

	public void setOsVersionCode(int osVersionCode) {
		this.osVersionCode = osVersionCode;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getDataNetworkType() {
		return dataNetworkType;
	}

	public void setDataNetworkType(int dataNetworkType) {
		this.dataNetworkType = dataNetworkType;
	}

	public String getWifiMacAddress() {
		return wifiMacAddress;
	}

	public void setWifiMacAddress(String wifiMacAddress) {
		this.wifiMacAddress = wifiMacAddress;
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json == null) {
			json = new JSONObject();
		}
		try {
			if (!TextUtils.isEmpty(deviceCode))
				json.put("deviceCode", deviceCode);
			if (!TextUtils.isEmpty(mobileModel))
				json.put("mobileModel", mobileModel);
			if (!TextUtils.isEmpty(manufacture))
				json.put("manufacture", manufacture);
			if (!TextUtils.isEmpty(osVersionName))
				json.put("osVersionName", osVersionName);
			json.put("osVersionCode", osVersionCode);
			if (!TextUtils.isEmpty(appPackageName))
				json.put("appPackageName", appPackageName);
			if (!TextUtils.isEmpty(appVersionName))
				json.put("appVersionName", appVersionName);
			json.put("appVersionCode", appVersionCode);
			json.put("appInstallTime", appInstallTime);
			json.put("platform", platform);
			json.put("dataNetworkType", dataNetworkType);
			if (!TextUtils.isEmpty(wifiMacAddress))
				json.put("wifiMacAddress", wifiMacAddress);
			if (!TextUtils.isEmpty(IMSI))
				json.put("IMSI", IMSI);
			if (!TextUtils.isEmpty(channelCode))
				json.put("channelCode", channelCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}
}
