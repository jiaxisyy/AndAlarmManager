package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.UploadRingRequest;
import com.babacit.alarm.msg.UploadRingResponse;

public class UploadRingTask extends
		BaseTask<UploadRingRequest, UploadRingResponse> {

	private final String TASKID = "32_" + hashCode();

	private String mSuffixName;

	private byte[] mBs;

	public UploadRingTask(String suffixName, byte[] bs) {
		mSuffixName = suffixName;
		mBs = bs;
	}

	@Override
	public void run() {
		response = new UploadRingResponse();
		int errCode = upload(mSuffixName, mBs);
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPLOAD_RING_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
