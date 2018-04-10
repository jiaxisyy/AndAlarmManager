package com.babacit.alarm.msg;

public class UploadFileRequest extends BaseRequest {
	/** 后缀名 */
	private String suffixName;
	private byte[] bs;

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public byte[] getBs() {
		return bs;
	}

	public void setBs(byte[] bs) {
		this.bs = bs;
	}

	@Override
	protected String getSign() {
		return null;
	}

}
