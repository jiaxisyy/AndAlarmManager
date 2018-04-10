package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.RewardInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.RewardInfoMsg;
import com.babacit.alarm.task.RewardSettingTask;
import com.babacit.alarm.task.TaskManager;

public class RewardSettingServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode,
			RequestCallBack callBack, List<RewardInfo> rewardInfos) {
		mCallBack = callBack;
		List<RewardInfoMsg> rewardInfoMsgs = new ArrayList<RewardInfoMsg>();
		if (rewardInfos != null && rewardInfos.size() > 0) {
			for (RewardInfo rewardInfo : rewardInfos) {
				RewardInfoMsg rewardInfoMsg = new RewardInfoMsg();
				rewardInfoMsg.setCurCount(rewardInfo.getCurCount());
				rewardInfoMsg.setEncourage(rewardInfo.getEncourage());
				rewardInfoMsg.setTargetCount(rewardInfo.getTargetCount());
				rewardInfoMsg.setTimeType(rewardInfo.getTimeType());
				rewardInfoMsgs.add(rewardInfoMsg);
			}
		}
		RewardSettingTask task = new RewardSettingTask(userId, alarmCode,
				rewardInfoMsgs);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null)
				mCallBack.onSuccess(null);
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
