package com.babacit.alarm.err;

public class ErrUtils {
	public static String getErrorReasonStr(int errCode) {
		String errStr = null;
		switch (errCode) {
		case -1:
			errStr = "处理失败！";
			break;
		case -2:
			errStr = "处理异常！";
			break;
		case ErrCode.NET_ERROR:
			errStr = "请检查您的网络！";
			break;
		case 10001:
			errStr = "签名错误！";
			break;
		case 10002:
			errStr = "验证码错误！";
			break;
		case 10003:
			errStr = "验证码失效！";
			break;
		case 20001:
			errStr = "用户已存在！";
			break;
		case 20002:
			errStr = "用户不存在！";
			break;
		case 20003:
			errStr = "密码错误！";
			break;
		case 20004:
			errStr = "分享次数已达上限！";
			break;
		case 20005:
			errStr = "监控次数达到上限！";
			break;
		case 20006:
			errStr = "用户已绑定此设备！";
			break;
		case 30001:
			errStr = "短信发送失败！";
			break;
		case 30002:
			errStr = "创建直播失败！";
			break;
		case 30003:
			errStr = "删除直播失败！";
			break;
		case 40001:
			errStr = "闹钟已存在！";
			break;
		case 40002:
			errStr = "闹钟不存在！";
			break;
		case 50001:
			errStr = "设备已存在！";
			break;
		case 50002:
			errStr = "设备不存在！";
			break;
		case 50003:
			errStr = "设备离线！";
			break;
		case 50004:
			errStr = "监控不存在！";
			break;
		case 60001:
			errStr = "消息地址为空！";
			break;
		case 60002:
			errStr = "消息已存在！";
			break;
		case 60003:
			errStr = "消息不存在！";
			break;
		default:
			break;
		}
		return errStr;
	}

}
