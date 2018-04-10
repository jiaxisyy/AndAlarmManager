package com.babacit.alarm.msg;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ApplyMonitorResponse extends BaseResponse {
//	/** 直播监控的url */
//	private String url;
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	@Override
//	public void parseJson(JSONObject json) {
//		if (json != null) {
//			try {
//				if (json.has("url"))
//					url = json.getString("url");
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		super.parseJson(json);
//	}
//}
