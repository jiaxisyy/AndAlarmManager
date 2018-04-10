package com.babacit.alarm.task;

import java.util.List;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.RewardInfoMsg;
import com.babacit.alarm.msg.RewardSettingRequest;

public class RewardSettingTask extends
		BaseTask<RewardSettingRequest, BaseResponse> {

	private final String TASKID = "22_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 奖励信息 */
	private List<RewardInfoMsg> mRewardInfoList;

	public RewardSettingTask(String userId, String alarmCode,
			List<RewardInfoMsg> rewardInfoMsgs) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mRewardInfoList = rewardInfoMsgs;
	}

	@Override
	public void run() {
		request = new RewardSettingRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setRewardInfoList(mRewardInfoList);
		response = new BaseResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.REWARD_SETTING_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
