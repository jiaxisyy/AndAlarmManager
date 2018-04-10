package com.babacit.alarm.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.msg.BaseRequest;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.net.HttpClient;
import com.babacit.alarm.profile.ProfileTool;

import android.text.TextUtils;

public abstract class BaseTask<M extends BaseRequest, N extends BaseResponse>
		implements Runnable {

	protected M request;
	protected N response;

	private RequestCallBack requestCallBack = null;

	public abstract String getUrl();

	public abstract String getTaskId();

	protected int exec() {
		if (!ProfileTool.getInstance().isNetworkConnected()) {
			return ErrCode.NET_ERROR;
		}
		final String content = encode();
		Logger.d(getTaskId() + "; " + getUrl());
		Logger.d(getTaskId() + "; " + content);
		if (!TextUtils.isEmpty(content)) {
			HttpClient client = new HttpClient();
			String result = client.request(getUrl(), content);
			// String result = getTestData(getUrl());
			Logger.d(getTaskId() + "; " + result);
			if (!TextUtils.isEmpty(result)) {
				return decode(result);
			} else {
				return ErrCode.DECODE_ERROR;
			}
		} else {
			return ErrCode.ENCODE_ERROR;
		}
	}

	private String getTestData(String path) {
		return ProfileTool.getInstance().readTxtFromAsset(path);
	}

	protected int upload(String suffixName, byte[] bs) {
		if (!ProfileTool.getInstance().isNetworkConnected()) {
			return ErrCode.NET_ERROR;
		}
		HttpClient client = new HttpClient();
		String result = client.upload(getUrl(), suffixName, bs);
		// String result = getTestData(getUrl());
		Logger.d(getTaskId() + "; " + result);
		if (!TextUtils.isEmpty(result)) {
			return decode(result);
		} else {
			return ErrCode.DECODE_ERROR;
		}
	}

	private String encode() {
		if (request != null) {
			JSONObject json = new JSONObject();
			json = request.toJson(json);
			return json.toString();
		}
		return null;
	}

	public int decode(String result) {
		try {
			if (response != null) {
				JSONObject jsonObject = new JSONObject(result);
				response.parseJson(jsonObject);
				return response.getCode();
				// if (response.getCode() == ErrCode.SUCCESS)
				// return ErrCode.SUCCESS;
				// else {
				// return ErrCode.ERROR;
				// }
			}
		} catch (JSONException e) {
			// Logger.e("server result json decode error");
		}
		return ErrCode.DECODE_ERROR;
	}

	public void setCallBack(RequestCallBack callBack) {
		if (callBack != null) {
			this.requestCallBack = callBack;
		}
	}

	protected void notifySuccess(Object object) {
		TaskManager.getInstance().remove(this);
		if (requestCallBack != null) {
			requestCallBack.onSuccess(object);
		}
	}

	protected void notifyFailed(Object object, int errCode) {
		TaskManager.getInstance().remove(this);
		if (requestCallBack != null) {
			requestCallBack.onFail(object, errCode);
		}
	}

}
