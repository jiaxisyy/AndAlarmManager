package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.HoneyInfoMsg;
import com.babacit.alarm.msg.QueryHoneyInfosResponse;
import com.babacit.alarm.task.QueryHoneyInfosTask;
import com.babacit.alarm.task.TaskManager;

public class QueryHoneyInfosServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryHoneyInfosTask task = new QueryHoneyInfosTask(userId, alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof QueryHoneyInfosResponse) {
				QueryHoneyInfosResponse response = (QueryHoneyInfosResponse) obj;
				List<HoneyInfoMsg> honeyInfoMsgs = response.getHoneyInfoList();
				List<HoneyInfo> honeyInfos = new ArrayList<HoneyInfo>();
				if (honeyInfoMsgs != null && honeyInfoMsgs.size() > 0) {
					for (HoneyInfoMsg honeyInfoMsg : honeyInfoMsgs) {
						HoneyInfo honeyInfo = new HoneyInfo();
						honeyInfo.setAlarmCode(honeyInfoMsg.getAlarmCode());
						honeyInfo.setName(honeyInfoMsg.getName());
						honeyInfo.setNickName(honeyInfoMsg.getNickName());
						honeyInfo.setImgUrl(honeyInfoMsg.getImgUrl());
						honeyInfo.setGender(honeyInfoMsg.getSex());
						honeyInfo.setBirthday(honeyInfoMsg.getBirthday());
						honeyInfo.setSchoolName(honeyInfoMsg.getSchoolName());
						honeyInfo.setGradeName(honeyInfoMsg.getGradeName());
						honeyInfo.setRoleName(honeyInfoMsg.getRoleName());
						honeyInfo.setInterest(honeyInfoMsg.getInterest());
						honeyInfos.add(honeyInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(honeyInfos);
			}
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
