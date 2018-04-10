package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.UploadFileRequest;
import com.babacit.alarm.msg.UploadFileResponse;

public class UploadFileTask extends
		BaseTask<UploadFileRequest, UploadFileResponse> {

	private final String TASKID = "33_" + hashCode();

	private String mSuffixName;

	private byte[] mBs;

	public UploadFileTask(String suffixName, byte[] bs) {
		mSuffixName = suffixName;
		mBs = bs;
	}

	@Override
	public void run() {
		response = new UploadFileResponse();
		int errCode = upload(mSuffixName, mBs);
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPLOAD_FILE_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
