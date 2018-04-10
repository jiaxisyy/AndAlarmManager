package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.QueryRewardInfoListResponse;
import com.babacit.alarm.msg.QueryRingResponse;
import com.babacit.alarm.msg.RingMsg;
import com.babacit.alarm.task.QueryRingTask;
import com.babacit.alarm.task.TaskManager;

public class QueryRingServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String lastTime,
			RequestCallBack callBack) {
		mCallBack = callBack;
		QueryRingTask task = new QueryRingTask(userId, alarmCode, lastTime);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryRingResponse) {
				QueryRingResponse response = (QueryRingResponse) obj;
				List<RingMsg> ringMsgs = response.getRingList();
				List<RingInfo> ringInfos = new ArrayList<RingInfo>();
				if (ringMsgs != null && ringMsgs.size() > 0) {
					for (RingMsg ringMsg : ringMsgs) {
						RingInfo ringInfo = new RingInfo();
						ringInfo.setRingCategory(ringMsg.getRingCategory());
						ringInfo.setRingId(ringMsg.getRingId());
						ringInfo.setRingName(ringMsg.getRingName());
						ringInfo.setRingType(ringMsg.getRingType());
						ringInfo.setRingUrl(ringMsg.getRingUrl());
						ringInfos.add(ringInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(ringInfos);
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
