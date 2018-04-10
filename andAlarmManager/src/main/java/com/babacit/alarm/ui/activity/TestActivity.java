package com.babacit.alarm.ui.activity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.NotifyConstant;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.entity.RewardInfo;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.manager.UserManager;
import com.babacit.alarm.notify.INotifyListener;
import com.babacit.alarm.notify.NotifyManager;
import com.babacit.alarm.server.ApplyBindAlarmServer;
import com.babacit.alarm.server.ConfirmBindAlarmServer;
import com.babacit.alarm.server.CreateAlarmClockListServer;
import com.babacit.alarm.server.GetVerifyTypeServer;
import com.babacit.alarm.server.HeartBeatServer;
import com.babacit.alarm.server.LoginServer;
import com.babacit.alarm.server.PwdSettingServer;
import com.babacit.alarm.server.QueryAlarmBaseInfoServer;
import com.babacit.alarm.server.QueryAlarmClockListServer;
import com.babacit.alarm.server.QueryClockHistoryListServer;
import com.babacit.alarm.server.QueryCompletionBarInfoServer;
import com.babacit.alarm.server.QueryCompletionPieInfoServer;
import com.babacit.alarm.server.QueryHolidayServer;
import com.babacit.alarm.server.QueryHoneyInfosServer;
import com.babacit.alarm.server.QueryRewardInfoListServer;
import com.babacit.alarm.server.QueryRingServer;
import com.babacit.alarm.server.QueryUserInfoServer;
import com.babacit.alarm.server.QueryVoiceMsgServer;
import com.babacit.alarm.server.RewardSettingServer;
import com.babacit.alarm.server.SendVoiceMsgServer;
import com.babacit.alarm.server.UpdateAlarmClockListServer;
import com.babacit.alarm.server.UpdateAlarmStatusServer;
import com.babacit.alarm.server.UpdateHistoryInfoServer;
import com.babacit.alarm.server.UpdateHoneyInfosServer;
import com.babacit.alarm.server.UpdatePhoneNoServer;
import com.babacit.alarm.server.UpdateReportTimeServer;
import com.babacit.alarm.server.UpdateSleepTimeServer;
import com.babacit.alarm.server.UpdateUserInfoServer;
import com.babacit.alarm.server.UpdateVacationServer;
import com.babacit.alarm.server.UploadFileServer;
import com.babacit.alarm.server.UploadMonitorStatusServer;
import com.babacit.alarm.server.UploadRingServer;
import com.babacit.alarm.server.UploadSharedInfoServer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class TestActivity extends Activity implements OnClickListener {

	private String phoneNo = "13713553611";
	private String verifyCode = "";
	private String pwd = "123456";
	private String userId = "48";
	private String alarmCode = "c7c40e8b909f327";
	private UserInfo userInfo;
	private EditText editVerifyCode;
	private HoneyInfo honeyInfo;
	private List<AlarmInfo> alarmInfos;
	private List<RewardInfo> rewardInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// PowerManager powerManager = (PowerManager) this
		// .getSystemService(Context.POWER_SERVICE);
		// WakeLock wakeLock = powerManager.newWakeLock(
		// PowerManager.FULL_WAKE_LOCK, "My Lock");
		// wakeLock.acquire();
		setContentView(R.layout.activity_test);
		findViewById(R.id.btn_get_verify_code).setOnClickListener(this);
		editVerifyCode = (EditText) findViewById(R.id.edit_verify_code);
		findViewById(R.id.btn_pwd_setting).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_query_user_info).setOnClickListener(this);
		findViewById(R.id.btn_update_user_info).setOnClickListener(this);
		findViewById(R.id.btn_update_phoneno).setOnClickListener(this);
		findViewById(R.id.btn_applybindalarm).setOnClickListener(this);
		findViewById(R.id.btn_confirmbindalarm).setOnClickListener(this);
		findViewById(R.id.btn_queryhoneyinfos).setOnClickListener(this);
		findViewById(R.id.btn_updatehoneyinfo).setOnClickListener(this);
		findViewById(R.id.btn_queryalarmclocklist).setOnClickListener(this);
		findViewById(R.id.btn_updatealarmclock).setOnClickListener(this);
		findViewById(R.id.btn_createalarmclock).setOnClickListener(this);
		findViewById(R.id.btn_queryclockhistorylist).setOnClickListener(this);
		findViewById(R.id.btn_updatehistoryinfo).setOnClickListener(this);
		findViewById(R.id.btn_applymonitor).setOnClickListener(this);
		findViewById(R.id.btn_monitor_start).setOnClickListener(this);
		findViewById(R.id.btn_monitor_finish).setOnClickListener(this);
		findViewById(R.id.btn_queryalarmbaseinfo).setOnClickListener(this);
		findViewById(R.id.btn_updatesleeptime).setOnClickListener(this);
		findViewById(R.id.btn_updatealarmstatus).setOnClickListener(this);
		findViewById(R.id.btn_updatevacation).setOnClickListener(this);
		findViewById(R.id.btn_queryrewardinfolist).setOnClickListener(this);
		findViewById(R.id.btn_RewardSetting).setOnClickListener(this);
		findViewById(R.id.btn_querycompletionbar).setOnClickListener(this);
		findViewById(R.id.btn_querycompletionpieinfo).setOnClickListener(this);
		findViewById(R.id.btn_queryholiday).setOnClickListener(this);
		findViewById(R.id.btn_queryring).setOnClickListener(this);
		findViewById(R.id.btn_uploadfile).setOnClickListener(this);
		findViewById(R.id.btn_uploadring).setOnClickListener(this);
		findViewById(R.id.btn_uploadsharedinfo).setOnClickListener(this);
		findViewById(R.id.btn_heartbeat).setOnClickListener(this);
		findViewById(R.id.btn_send_voice_msg).setOnClickListener(this);
		findViewById(R.id.btn_query_voice_msgs).setOnClickListener(this);
		findViewById(R.id.btn_update_report_time).setOnClickListener(this);
		new SharedConfig(this).setUserId(userId);
		NotifyManager.getInstance().register(NotifyConstant.HEART_HEAT,
				listener);
		UserManager.getInstance().start(this, alarmCode);
	}

	private INotifyListener listener = new INotifyListener() {

		@Override
		public void notify(int moduleId, Object obj) {
			Logger.d("obj = " + obj);
		}
	};

	@Override
	protected void onDestroy() {
		NotifyManager.getInstance().unregister(NotifyConstant.HEART_HEAT,
				listener);
		UserManager.getInstance().stop();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		verifyCode = editVerifyCode.getEditableText().toString();
		switch (v.getId()) {
		case R.id.btn_get_verify_code: {
			getVerifyCode();
			break;
		}
		case R.id.btn_pwd_setting: {
			pwdSetting();
			break;
		}
		case R.id.btn_login: {
			login();
			break;
		}
		case R.id.btn_query_user_info: {
			queryUserInfo();
			break;
		}
		case R.id.btn_update_user_info: {
			updateUserInfo();
			break;
		}
		case R.id.btn_update_phoneno: {
			updatePhoneNo();
			break;
		}
		case R.id.btn_applybindalarm: {
			applyBindAlarm();
			break;
		}
		case R.id.btn_confirmbindalarm: {
			confirmBindAlarm();
			break;
		}
		case R.id.btn_queryhoneyinfos: {
			queryHoneyInfo();
			break;
		}
		case R.id.btn_updatehoneyinfo: {
			updateHoneyInfo();
			break;
		}
		case R.id.btn_queryalarmclocklist: {
			queryAlarmClock();
			break;
		}
		case R.id.btn_updatealarmclock: {
			updateAlarmClock();
			break;
		}
		case R.id.btn_createalarmclock: {
			createAlarmClock();
			break;
		}
		case R.id.btn_queryclockhistorylist: {
			queryClockHistory();
			break;
		}
		case R.id.btn_updatehistoryinfo: {
			updateHistoryInfo();
			break;
		}
		case R.id.btn_applymonitor: {
			applyMonitor();
			break;
		}
		case R.id.btn_monitor_start: {
			monitorStart();
			break;
		}
		case R.id.btn_monitor_finish: {
			monitorFinish();
			break;
		}
		case R.id.btn_queryalarmbaseinfo: {
			queryAlarmBaseInfo();
			break;
		}
		case R.id.btn_updatesleeptime: {
			updateSleepTime();
			break;
		}
		case R.id.btn_updatealarmstatus: {
			updateAlarmStatus();
			break;
		}
		case R.id.btn_updatevacation: {
			updateVacation();
			break;
		}
		case R.id.btn_queryrewardinfolist: {
			queryRewardInfoList();
			break;
		}
		case R.id.btn_RewardSetting: {
			rewardSetting();
			break;
		}
		case R.id.btn_querycompletionpieinfo: {
			queryPie();
			break;
		}
		case R.id.btn_querycompletionbar: {
			queryBar();
			break;
		}
		case R.id.btn_queryholiday: {
			queryHoliday();
			break;
		}
		case R.id.btn_queryring: {
			queryRing();
			break;
		}
		case R.id.btn_uploadsharedinfo: {
			updateSharedInfo();
			break;
		}
		case R.id.btn_uploadfile: {
			uploadFile();
			break;
		}
		case R.id.btn_uploadring: {
			uploadRing();
			break;
		}
		case R.id.btn_heartbeat: {
			heartBeat();
			break;
		}
		case R.id.btn_send_voice_msg: {
			sendVoiceMsg();
			break;
		}
		case R.id.btn_query_voice_msgs: {
			queryVoiceMsgs();
			break;
		}
		case R.id.btn_update_report_time: {
			updateReportTime();
			break;
		}
		default:
			break;
		}

	}

	private void getVerifyCode() {
		new GetVerifyTypeServer().start(phoneNo, 1, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void pwdSetting() {
		new PwdSettingServer().start(phoneNo, verifyCode, "000000", pwd, 2,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void login() {
		new LoginServer().start(phoneNo, pwd, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
				userId = (String) obj;
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void queryUserInfo() {
		new QueryUserInfoServer().start(userId, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
				userInfo = (UserInfo) obj;
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void updateUserInfo() {
		userInfo.setBirthday("2011-03-01");
		userInfo.setImgUrl("http://2.2.2.2/3.jpg");
		new UpdateUserInfoServer().start(1, userInfo, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void updatePhoneNo() {
		new UpdatePhoneNoServer().start(userId, "18719047468", pwd, phoneNo,
				verifyCode, new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void applyBindAlarm() {
		new ApplyBindAlarmServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void confirmBindAlarm() {
		new ConfirmBindAlarmServer().start(userId, alarmCode, verifyCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryHoneyInfo() {
		new QueryHoneyInfosServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
						List<HoneyInfo> honeyInfos = (List<HoneyInfo>) obj;
						honeyInfo = honeyInfos.get(honeyInfos.size() - 1);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateHoneyInfo() {
		if (honeyInfo == null) {
			honeyInfo = new HoneyInfo();
		}
		honeyInfo.setGender(0);
		honeyInfo.setImgUrl("http://1.1.1.1/1.jpg");
		honeyInfo.setAlarmCode(alarmCode);
		new UpdateHoneyInfosServer().start(userId, honeyInfo,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryAlarmClock() {
		new QueryAlarmClockListServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
						alarmInfos = (List<AlarmInfo>) obj;
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateAlarmClock() {
		if (alarmInfos != null && alarmInfos.size() > 0) {
			AlarmInfo alarmInfo = alarmInfos.get(alarmInfos.size() - 1);
			alarmInfo.setDate("2015-11-10");
			alarmInfos.clear();
			alarmInfos.add(alarmInfo);
		}
		new UpdateAlarmClockListServer().start(userId, alarmCode, alarmInfos,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void createAlarmClock() {
		if (alarmInfos == null) {
			alarmInfos = new ArrayList<AlarmInfo>();
		}
		AlarmInfo alarmInfo = new AlarmInfo();
		alarmInfo.setTitle("闹钟起床100");
		alarmInfo.setStatus(1);
		List<String> times = new ArrayList<String>();
		times.add("07:40");
		alarmInfo.setTimes(times);
		alarmInfo.setClockType(1);
		alarmInfo.setTimeType(1);
		alarmInfo.setDateType(0);
		alarmInfo.setHolidayFilter(2);
		alarmInfo.setDate("2015-11-08");
		alarmInfo.setDelayTime(5);
		RingInfo ringInfo = new RingInfo();
		ringInfo.setRingId(1);
		ringInfo.setRingCategory(1);
		ringInfo.setRingType(0);
		ringInfo.setRingName("铃声1");
		List<RingInfo> ringInfos = new ArrayList<RingInfo>();
		ringInfos.add(ringInfo);
		alarmInfo.setRingList(ringInfos);
		alarmInfo.setRemark("备注");
		alarmInfos.add(alarmInfo);
		new CreateAlarmClockListServer().start(userId, alarmCode, alarmInfos,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryClockHistory() {
		new QueryClockHistoryListServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateHistoryInfo() {
		new UpdateHistoryInfoServer().start(userId, alarmCode, "2", 1,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void applyMonitor() {
		new UploadMonitorStatusServer().start(userId, alarmCode, 0,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}
	private void monitorStart() {
		new UploadMonitorStatusServer().start(userId, alarmCode,1,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}
	private void monitorFinish() {
		new UploadMonitorStatusServer().start(userId, alarmCode,2,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryAlarmBaseInfo() {
		new QueryAlarmBaseInfoServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateSleepTime() {
		new UpdateSleepTimeServer().start(userId, alarmCode, "22:00", "08:00",
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateAlarmStatus() {
		new UpdateAlarmStatusServer().start(userId, alarmCode, 1, 1,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateVacation() {
		new UpdateVacationServer().start(userId, alarmCode, "07-10", "08-18",
				1, new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryRewardInfoList() {
		new QueryRewardInfoListServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
						rewardInfos = (List<RewardInfo>) obj;
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void rewardSetting() {
		for (RewardInfo rewardInfo : rewardInfos) {
			rewardInfo.setTargetCount(rewardInfo.getTargetCount() - 1);
		}
		new RewardSettingServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				}, rewardInfos);
	}

	private void queryPie() {
		new QueryCompletionPieInfoServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryBar() {
		new QueryCompletionBarInfoServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryHoliday() {
		new QueryHolidayServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryRing() {
		new QueryRingServer().start(userId, alarmCode, "",
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateSharedInfo() {
		new UploadSharedInfoServer().start(userId, alarmCode,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void uploadFile() {
		byte[] bs = null;
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/dcim/camera/IMG_20151031_104240.jpg";
			bs = toByteArray(path);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new UploadFileServer().start("jpg", bs, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void uploadRing() {
		byte[] bs = null;
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/MIUI/sound_recorder/123.mp3";
			bs = toByteArray(path);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new UploadRingServer().start("mp3", bs, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	private void heartBeat() {
		new HeartBeatServer().start(userId, alarmCode, null, new RequestCallBack() {

			@Override
			public void onSuccess(Object obj) {
				Logger.d("success, obj = " + obj);
			}

			@Override
			public void onFail(Object object, int errCode) {
				Logger.d("onFail, errCode = " + errCode);
			}
		});
	}

	public static byte[] toByteArray(String filename) throws IOException {

		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	private void sendVoiceMsg() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/RECORDING/record20151118165244.3gpp";
		new SendVoiceMsgServer().start(userId, alarmCode, path, 10,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void queryVoiceMsgs() {
		new QueryVoiceMsgServer().start(userId, alarmCode, 0,
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}

	private void updateReportTime() {
		new UpdateReportTimeServer().start(userId, alarmCode, "09:00", "18:00",
				new RequestCallBack() {

					@Override
					public void onSuccess(Object obj) {
						Logger.d("success, obj = " + obj);
					}

					@Override
					public void onFail(Object object, int errCode) {
						Logger.d("onFail, errCode = " + errCode);
					}
				});
	}
}
