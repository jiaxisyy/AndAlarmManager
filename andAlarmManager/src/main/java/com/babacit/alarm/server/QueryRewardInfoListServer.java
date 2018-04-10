package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.RewardInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.QueryRewardInfoListResponse;
import com.babacit.alarm.msg.RewardInfoMsg;
import com.babacit.alarm.task.QueryRewardInfoListTask;
import com.babacit.alarm.task.TaskManager;

public class QueryRewardInfoListServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryRewardInfoListTask task = new QueryRewardInfoListTask(userId,
				alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryRewardInfoListResponse) {
				QueryRewardInfoListResponse response = (QueryRewardInfoListResponse) obj;
				List<RewardInfoMsg> rewardInfoMsgs = response
						.getRewardInfoList();
				List<RewardInfo> rewardInfos = new ArrayList<RewardInfo>();
				if (rewardInfoMsgs != null && rewardInfoMsgs.size() > 0) {
					for (RewardInfoMsg rewardInfoMsg : rewardInfoMsgs) {
						RewardInfo rewardInfo = new RewardInfo();
						rewardInfo.setCurCount(rewardInfoMsg.getCurCount());
						rewardInfo.setEncourage(rewardInfoMsg.getEncourage());
						rewardInfo.setTargetCount(rewardInfoMsg
								.getTargetCount());
						rewardInfo.setTimeType(rewardInfoMsg.getTimeType());
						rewardInfos.add(rewardInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(rewardInfos);
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
