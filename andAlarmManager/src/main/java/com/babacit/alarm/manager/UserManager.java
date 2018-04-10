package com.babacit.alarm.manager;

import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.NotifyConstant;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.notify.NotifyManager;
import com.babacit.alarm.server.HeartBeatServer;

import android.content.Context;

public class UserManager {

	private static UserManager instance = new UserManager();
	private boolean flag = false;
	private SharedConfig config;
	private String alarmCode;
	/** 监控标识:监控中则为on,其他情况可为空 */
	private String monitor;
	private HeartBeatServer server;
	private HeartThread thread;
	private Object lock = new Object();

	private UserManager() {
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public void start(Context context, String alarmCode) {
		synchronized (lock) {
			if (config == null) {
				config = new SharedConfig(context);
			}
			this.alarmCode = alarmCode;
			this.monitor = null;
			if (!flag) {
				flag = true;
				thread = new HeartThread();
				thread.start();
			}
		}
	}

	public void stop() {
		synchronized (lock) {
			if (flag) {
				config = null;
				flag = false;
				thread = null;
				server = null;
			}
		}
	}
	
	/** 监控标识:监控中则为on,其他情况可为空 */
	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	private class HeartThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				sendHeartBeat();
				try {
					sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}

	private void sendHeartBeat() {
		synchronized (lock) {
			if (server == null) {
				server = new HeartBeatServer();
			}
			if (config != null) {
				server.start(config.getUserId(), alarmCode, monitor, callBack);
			}
		}
	}

	private RequestCallBack callBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			NotifyManager.getInstance().notify(NotifyConstant.HEART_HEAT, obj);
		}

		@Override
		public void onFail(Object object, int errCode) {
			NotifyManager.getInstance().notify(NotifyConstant.HEART_HEAT,
					errCode);
		}
	};
}
