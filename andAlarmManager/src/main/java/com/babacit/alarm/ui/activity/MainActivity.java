package com.babacit.alarm.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.babacit.alarm.MainApplication;
import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.constant.NotifyConstant;
import com.babacit.alarm.db.dao.HolidayDao;
import com.babacit.alarm.db.dao.HoneyDao;
import com.babacit.alarm.db.dao.UserDao;
import com.babacit.alarm.entity.AlarmBaseInfo;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.HistoryInfo;
import com.babacit.alarm.entity.Holiday;
import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.manager.UserManager;
import com.babacit.alarm.msg.PushInfoMsg;
import com.babacit.alarm.msg.UploadMonitorStatusResponse;
import com.babacit.alarm.notify.INotifyListener;
import com.babacit.alarm.notify.NotifyManager;
import com.babacit.alarm.profile.ProfileTool;
import com.babacit.alarm.server.QueryAlarmBaseInfoServer;
import com.babacit.alarm.server.QueryAlarmClockListServer;
import com.babacit.alarm.server.QueryClockHistoryListServer;
import com.babacit.alarm.server.QueryHolidayServer;
import com.babacit.alarm.server.QueryHoneyInfosServer;
import com.babacit.alarm.server.QueryVoiceMsgServer;
import com.babacit.alarm.server.SendVoiceMsgServer;
import com.babacit.alarm.server.SwitchClockStatusServer;
import com.babacit.alarm.server.UpdateAlarmStatusServer;
import com.babacit.alarm.server.UploadMonitorStatusServer;
import com.babacit.alarm.swvideo.AGEventHandler;
import com.babacit.alarm.swvideo.ConstantApp;
import com.babacit.alarm.swvideo.EngineConfig;
import com.babacit.alarm.swvideo.MyEngineEventHandler;
import com.babacit.alarm.swvideo.SWVideoSurfaceView;
import com.babacit.alarm.swvideo.WorkerThread;
import com.babacit.alarm.swvideo.SWVideoSurfaceView.SWVideoSVListener;
import com.babacit.alarm.ui.adapter.AlarmAdapter;
import com.babacit.alarm.ui.adapter.HistoryAdapter;
import com.babacit.alarm.ui.adapter.HoneyAdapter;
import com.babacit.alarm.ui.adapter.VoiceMailAdapter;
import com.babacit.alarm.ui.dialog.ChooseAlarmTypeDlgFragment;
import com.babacit.alarm.ui.dialog.MyDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.view.MyGridView;
import com.babacit.alarm.ui.view.MyHorizontalScrollView;
import com.babacit.alarm.ui.view.RoundImageView;
import com.babacit.alarm.ui.view.SlidingMenu;
import com.babacit.alarm.update2.AiConfigConstant;
import com.babacit.alarm.utils.MediaPlayerUtil;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ScreenUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.buihha.audiorecorder.Mp3Recorder;
import com.example.hekd.kotlinapp.ai.DissectionNlu;
import com.example.hekd.kotlinapp.ai.VoiceRecognition;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kyleduo.switchbutton.SwitchButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.update.UmengUpdateAgent;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class MainActivity extends BaseActivity implements
        OnMyDialogClickListener, MediaScannerConnectionClient, AGEventHandler {
    private static final int READ_PHONE_STATE = 10010;
    private static final int PERMISSIONS_VOICE = 10011;
    private static final int PERMISSIONS_VOICE_IM = 10012;
    private static final int PERMISSIONS_CAMERA = 10013;

    /**
     * 打开相机对应的业务
     */
    private int permissions_type_camera = 0;
    /**
     * 打开语音对应的业务
     */
    private int permissions_type_voice = 0;
    /**
     * 申请权限后-添加设备
     */
    private static final int PERMISSIONS_TYPE_ADDDEVICE = 10020;
    /**
     * 申请权限后-视频监控
     */
    private static final int PERMISSIONS_TYPE_VIDEOMONITORING = 10021;
    /**
     * 申请权限后-语音聊天
     */
    private static final int PERMISSIONS_TYPE_VOICEIM = 10022;
    /**
     * 申请权限后-语音识别
     */
    private static final int PERMISSIONS_TYPE_VOICE = 10023;
    /**
     * 点击相册按钮进入相册数据
     */
    private String imagePath;
    private SharedConfig config;
    /**
     * 主界面顶部文本控件
     */
    private TextView mTvMainTitle;
    /**
     * 主界面宝贝图标
     */
    private RoundImageView mRivMainTabPortrait;
    private RoundImageView mRivPortraitOnLeftMenu;
    /**
     * 左侧SlidingMenu当前宝贝文本控件
     */
    private TextView mTvCurrHoneyName;
    /**
     * 底部多功能按钮
     */
    private ImageView mIvMultiFunc;
    private TextView mTvRecordHint;
    /**
     * 底部TAB布局
     */
    private RelativeLayout mRlMainTab1, mRlMainTab3, mRlMainTab4;
    /**
     * 底部TAB布局
     */
    private LinearLayout mRlMainTab2;
    /**
     * 左边滑动菜单
     */
    private SlidingMenu leftMenu;
    /**
     * 底部TAB容器
     */
    private MyHorizontalScrollView bottomHorizontalView;
    /**
     * 视频Layout
     */
    private FrameLayout mVideoLayout;
    /**
     * 底部RadioGroup组件
     */
    private RadioGroup radioGroup;
    private ListView mLvAlarm, mLvHistory;
    private PullToRefreshListView mLvVoiceMail;
    private ListView mLvHoney;
    private AlarmAdapter alarmAdapter;
    /**
     * 语音信箱适配器
     */
    private VoiceMailAdapter voiceMailAdapter;
    // private SoundMeter mSensor;
    // private AudioRecorder2Mp3Util util;
    private final Mp3Recorder recorder = new Mp3Recorder();
    private String voiceName;
    private int voiceTime;
    private static final int POLL_INTERVAL = 300;
    /**
     * 录音开始和结束时间
     */
    private long startVoiceT, endVoiceT;
    /**
     * 是否是点击留言信箱中的录音按钮
     */
    private boolean mNoVoiceBtnDown = false;
    /**
     * 录音提示的控件
     */
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort;
    private ImageView img1, sc_img1;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView volume;
    /**
     * 历史纪录适配器
     */
    private HistoryAdapter historyAdapter;
    /**
     * 宝贝列表适配器
     */
    private HoneyAdapter honeyAdapter;
    /**
     * 初始化查询宝贝请求是否成功
     */
    private boolean mIsQueryHoneyOK;
    /**
     * 当前宝贝
     */
    private HoneyInfo mCurrHoney;
    private RadioButton mRbFinished, mRbUnfinished;
    /**
     * 已完成历史纪录列表数据
     */
    private List<HistoryInfo> _finishedHistoryInfos = new ArrayList<HistoryInfo>();
    /**
     * 未完成历史纪录列表数据
     */
    private List<HistoryInfo> _unfinishedHistoryInfos = new ArrayList<HistoryInfo>();
    /**
     * 语音留言列表数据
     */
    private List<com.babacit.alarm.entity.Message> _voiceInfos = new ArrayList<com.babacit.alarm.entity.Message>();
    private com.babacit.alarm.entity.Message voiceInfo = new com.babacit.alarm.entity.Message();
    /**
     * 闹钟基本信息
     */
    private AlarmBaseInfo alarmBaseInfo;
    /**
     * 闹钟列表数据
     */
    private List<AlarmInfo> alarmInfos;
    /**
     * 宝贝列表数据
     */
    private List<HoneyInfo> honeyInfos = new ArrayList<HoneyInfo>();
    /**
     * 睡眠时间
     */
    private TextView mTvSleepDuration;
    /**
     * 报时时间段
     */
    private TextView mTvChimeDuration;
    /**
     * 电量百分比文本
     */
    private TextView mTvBatteryPercent;
    /**
     * 充电插头
     */
    private ImageView mIvPlug;
    /**
     * 电量百分比数值
     */
    private int percent = 0;
    /**
     * 电量百分比位图
     */
    private Bitmap mBmFullPercent;
    /**
     * 电量状态图片
     */
    private ImageView mIvBatteryPercent;
    /**
     * 进入相册
     */
    private ImageView mIvAlbum;
    /**
     * 抓屏
     */
    private ImageView mIvCapture;
    /**
     * 视频播放标识
     */
    private boolean videoFlag = false;
    /**
     * 寒暑假期
     */
    private TextView mTvSummerVacation, mTvWinterVacation;
    /** 终端触摸屏开关 */
//	private SwitchButton mSbTerminalTouchable;
    /**
     * 整点报时开关
     */
    private SwitchButton mSbChimeEveryHour;
    /**
     * 提示绑定终端对话框
     */
    private MyDialogFragment bind;
    /**
     * 右侧SlidingMenu
     */
    private com.jeremyfeinstein.slidingmenu.lib.SlidingMenu rightMenu;
    private MediaScannerConnection conn;
    private String scanPath;
    private static final String FILE_TYPE = "image/*";
    /**
     * 右侧SlidingMenu上的分享布局
     */
    private MyGridView mGvShare;
    /**
     * 监控视频倒计时自动关闭计时器
     */
    private Timer mVideoTimer;
    /**
     * 监控时长，单位秒
     */
    private int mMonitorTime;
    /**
     * 底部TAB文本
     */
    private int[] titles = {R.string.txt_tab_message, R.string.txt_tab_course,
            R.string.txt_tab_contacts_list, R.string.txt_tab_mine};
    /**
     * 底部TAB图片
     */
    private int[] images = {R.drawable.selector_main_tab_clock,
            R.drawable.selector_main_tab_history,
            R.drawable.selector_main_tab_mail,
            R.drawable.selector_main_tab_check_status};
    /**
     * 语音权限
     */
    String permissionsVoices[] = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 视频权限
     */
    String permissionsCamera[] = {Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };


    public static final int MSG_HIDE_RIGHT_SLIDING_MENU = 6001;
    public static final int MSG_SWITCH_CLOCK_STATUS = 6002;
    public static final int MSG_UPDATE_HISTORY_STATUS = 6003;
    public static final int MSG_CREATE_NEW_ALARM = 6004;
    public static final int MSG_HIDE_BOTH_MENU = 6005;
    public static final int MSG_BIND_TEMINAL_SUCCESS = 6006;

    public static final int RESULT_CODE_FROM_UPDATE_HONEY_INFO = 8000;
    public static final int RESULT_CODE_FROM_BIND_TERMINATION_SUCCESS = 8001;
    public static final int RESULT_CODE_FROM_BIND_TERMINATION_FAIL = 8002;
    public static final int RESULT_CODE_FROM_SLEEP_SETTING = 8003;
    public static final int RESULT_CODE_FROM_VACATION_SETTING = 8004;
    public static final int RESULT_CODE_FROM_CHIME_SETTING = 8005;
    public static final int RESULT_CODE_FROM_GETUP_ALARM = 8006;
    public static final int RESULT_CODE_FROM_HOMEWORK_ALARM = 8007;
    public static final int RESULT_CODE_FROM_BIRTHDAY_ALARM = 8008;
    public static final int RESULT_CODE_FROM_DAILY_ALARM = 8009;
    public static final int RESULT_CODE_FROM_EYEPROTECT_ALARM = 8010;
    public static final int RESULT_CODE_FROM_ANNIVERSARY_ALARM = 8011;
    public static final int RESULT_CODE_FROM_CUSTOM_ALARM = 8012;
    public static final int RESULT_CODE_FROM_WATER_MEDICINE_ALARM = 8013;
    public static final int RESULT_CODE_FROM_FESTIVAL_ALARM = 8014;
    public static final int RESULT_CODE_FROM_UNLOCK_GESTURE = 8015;
    public static final int RESULT_CODE_FROM_BIND_TEMINAL = 8016;

    public static final int RESULT_CODE_FROM_IMAGE = 8017;

    private static final int MSG_HEART_BEAT = 8999;
    private static final int MSG_HONEY_CHANGED = 9000;
    private static final int MSG_QUERY_HISTORY_SUCCESS = 9001;
    private static final int MSG_QUERY_HISTORY_FAIL = 9002;
    private static final int MSG_QUERY_ALARM_BASE_INFO_SUCCESS = 9003;
    private static final int MSG_QUERY_ALARM_BASE_INFO_FAIL = 9004;
    private static final int MSG_QUERY_ALARM_LIST_SUCCESS = 9005;
    private static final int MSG_QUERY_ALARM_LIST_FAIL = 9006;
    private static final int MSG_QUERY_HONEY_LIST_SUCCESS = 9007;
    private static final int MSG_QUERY_HONEY_LIST_FAIL = 9008;
    private static final int MSG_QUERY_VOICE_MAIL_LIST_SUCCESS = 9009;
    private static final int MSG_QUERY_VOICE_MAIL_LIST_FAIL = 9010;
    private static final int MSG_QUERY_HOLIDAY_LIST_SUCCESS = 9011;
    private static final int MSG_QUERY_HOLIDAY_LIST_FAIL = 9012;
    private static final int MSG_SEND_VOICE_SUCCESS = 9013;
    private static final int MSG_SEND_VOICE_FAIL = 9014;
    private static final int MSG_UPDATE_HONEY = 9015;
    private static final int MSG_APPLY_MONITOR_SUCCESS = 9016;
    private static final int MSG_APPLY_MONITOR_FAIL = 9017;
    private static final int MSG_UPLOAD_START_MONITOR_SUCCESS = 9018;
    private static final int MSG_UPLOAD_START_MONITOR_FAIL = 9019;
    private static final int MSG_UPDATAE_ALARM_BASE_FAIL = 9020;
    private static final int FLAG_MY_SCREEN = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

    private OnRefreshListener<ListView> pullToRefreshListener = new OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            new QueryVoiceMsgServer().start(config.getUserId(),
                    config.getAlarmCode(), 0, queryVoiceMsgCallBack);
        }
    };

    private INotifyListener listener = new INotifyListener() {
        @Override
        public void notify(int moduleId, Object obj) {
            Message msg = mHandler.obtainMessage(MSG_HEART_BEAT);
            msg.obj = obj;
            msg.sendToTarget();
        }
    };

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stopRecord();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = recorder.getVoiceLevel();
            Logger.d("level:" + recorder.getVoiceLevel());
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    /**
     * 查询历史记录回调
     */
    private RequestCallBack queryHistoryCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler.obtainMessage(MSG_QUERY_HISTORY_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            mHandler.sendEmptyMessage(MSG_QUERY_HISTORY_FAIL);
        }
    };

    /**
     * 查询闹钟基本信息回调
     */
    private RequestCallBack queryAlarmBaseInfoCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler
                    .obtainMessage(MSG_QUERY_ALARM_BASE_INFO_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            mHandler.sendEmptyMessage(MSG_QUERY_ALARM_BASE_INFO_FAIL);
        }
    };

    /**
     * 查询闹钟列表回调
     */
    private RequestCallBack queryAlarmListCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler.obtainMessage(MSG_QUERY_ALARM_LIST_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            mHandler.sendEmptyMessage(MSG_QUERY_ALARM_LIST_FAIL);
        }
    };

    /**
     * 查询宝贝列表回调
     */
    private RequestCallBack queryHoneyListCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler.obtainMessage(MSG_QUERY_HONEY_LIST_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message msg = mHandler.obtainMessage(MSG_QUERY_HONEY_LIST_FAIL);
            msg.arg1 = errCode;
            msg.sendToTarget();
        }
    };

    /**
     * 修改终端状态回调
     */
    private RequestCallBack updateAlarmStatusCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            new QueryAlarmBaseInfoServer().start(config.getUserId(),
                    config.getAlarmCode(), queryAlarmBaseInfoCallBack);
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message msg = mHandler.obtainMessage(MSG_UPDATAE_ALARM_BASE_FAIL);
            msg.arg1 = errCode;
            msg.sendToTarget();
        }
    };

    /**
     * 查询语音信箱回调
     */
    private RequestCallBack queryVoiceMsgCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler
                    .obtainMessage(MSG_QUERY_VOICE_MAIL_LIST_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message fail = mHandler
                    .obtainMessage(MSG_QUERY_VOICE_MAIL_LIST_FAIL);
            fail.arg1 = errCode;
            fail.sendToTarget();
        }
    };

    /**
     * 获取节日列表回调
     */
    private RequestCallBack queryHolidayCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler
                    .obtainMessage(MSG_QUERY_HOLIDAY_LIST_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            mHandler.sendEmptyMessage(MSG_QUERY_HOLIDAY_LIST_FAIL);
        }
    };

    /**
     * 更新闹钟状态回调
     */
    private RequestCallBack switchAlarmStatusCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            mHandler.sendEmptyMessage(MSG_SWITCH_CLOCK_STATUS);
        }

        @Override
        public void onFail(Object object, int errCode) {
            mHandler.sendEmptyMessage(MSG_SWITCH_CLOCK_STATUS);
        }
    };

    /**
     * 发送语音回调
     */
    private RequestCallBack sendVoiceMsgCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            Message msg = mHandler.obtainMessage(MSG_SEND_VOICE_SUCCESS);
            msg.obj = obj;
            msg.sendToTarget();
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message msg = mHandler.obtainMessage(MSG_SEND_VOICE_FAIL);
            msg.arg1 = errCode;
            msg.sendToTarget();
        }
    };

    /**
     * 尝试视频监控回调
     */
    private RequestCallBack applyMonitorCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            UploadMonitorStatusResponse response = (UploadMonitorStatusResponse) obj;
            mMonitorTime = response.getMonitorTime();
            if (mMonitorTime > 0)
                mHandler.sendEmptyMessage(MSG_APPLY_MONITOR_SUCCESS);
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message msg = mHandler.obtainMessage(MSG_APPLY_MONITOR_FAIL);
            msg.arg1 = errCode;
            msg.sendToTarget();
//			mMonitorTime = 600;
//			mHandler.sendEmptyMessage(MSG_APPLY_MONITOR_SUCCESS);
        }
    };

    /**
     * 视频开始监控回调
     */
    private RequestCallBack uploadMonitorStartCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            mHandler.sendEmptyMessage(MSG_UPLOAD_START_MONITOR_SUCCESS);
        }

        @Override
        public void onFail(Object object, int errCode) {
            Message msg = mHandler.obtainMessage(MSG_UPLOAD_START_MONITOR_FAIL);
            msg.arg1 = errCode;
            msg.sendToTarget();
        }
    };

    /**
     * 视频结束监控回调
     */
    private RequestCallBack uploadMonitorEndCallBack = new RequestCallBack() {
        @Override
        public void onSuccess(Object obj) {
            dismissProgress();
        }

        @Override
        public void onFail(Object object, int errCode) {
            dismissProgress();
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("create_alarm")) {
                mHandler.sendEmptyMessage(MSG_CREATE_NEW_ALARM);
            } else if (action.equals("bind_success")) {
                mHandler.sendEmptyMessage(MSG_BIND_TEMINAL_SUCCESS);
            } else if (action.equals("update_honey")) {
                mHandler.sendEmptyMessage(MSG_UPDATE_HONEY);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HEART_BEAT:
                    if (msg.obj != null && msg.obj instanceof HashMap<?, ?>) {
                        HashMap<Object, Object> map = (HashMap<Object, Object>) msg.obj;
                        String batteryPercent = (String) map.get("batteryPercent");
                        int chargeStatus = (Integer) map.get("chargeStatus");
                        List<PushInfoMsg> pushList = (List<PushInfoMsg>) map.get("pushList");
                        if (pushList != null && pushList.size() > 0) {
                            MyDialogFragment push = new MyDialogFragment();
                            Bundle args = new Bundle();
                            args.putString(Constant.DLG_CONTENT, pushList.get(0)
                                    .getPushMsg());
                            args.putString("url", pushList.get(0).getPushUrl());
                            args.putBoolean(Constant.DLG_NO_BUTTON, true);
                            push.setArguments(args);
                            push.setCancelable(true);
                            push.show(getFragmentManager(), "push");
                        }
                        mTvBatteryPercent.setText(batteryPercent + "%");
                        if (chargeStatus == 1) {
                            mIvPlug.setVisibility(View.VISIBLE);
                        } else {
                            mIvPlug.setVisibility(View.GONE);
                        }
                    }
                    break;
                case MSG_HIDE_RIGHT_SLIDING_MENU:
                    if (rightMenu.isMenuShowing()) {
                        rightMenu.toggle(true);
                    }
                    break;
                case MSG_HIDE_BOTH_MENU:
                    if (rightMenu.isMenuShowing()) {
                        rightMenu.toggle(true);
                    }
                    if (leftMenu.isActivated()) {
                        leftMenu.toggle();
                    }
                    break;
                case MSG_QUERY_HISTORY_SUCCESS:
                    dismissProgress();
                    List<HistoryInfo> historyInfos = (List<HistoryInfo>) msg.obj;
                    _finishedHistoryInfos.clear();
                    _unfinishedHistoryInfos.clear();
                    for (HistoryInfo historyInfo : historyInfos) {
                        String[] t = historyInfo.getDate().split(" ");
                        historyInfo.setDate(t[0]);
//					historyInfo.setTime(t[1]);
                        if (historyInfo.getFinishStatus() == 0) {
                            _unfinishedHistoryInfos.add(historyInfo);
                        } else {
                            _finishedHistoryInfos.add(historyInfo);
                        }
                    }
                    historyAdapter.notifyDataSetChanged();
                    break;
                case MSG_QUERY_ALARM_BASE_INFO_SUCCESS:
                    alarmBaseInfo = (AlarmBaseInfo) msg.obj;
                    refreshLeftMenu();
                    break;
                case MSG_QUERY_ALARM_BASE_INFO_FAIL:
                    break;
                case MSG_QUERY_ALARM_LIST_SUCCESS:
                    if (alarmInfos != null) {
                        alarmInfos.clear();
                    }
                    alarmInfos = (List<AlarmInfo>) msg.obj;
                    if (alarmInfos != null && alarmInfos.size() > 0) {
                        // sortAlarmInfos(alarmInfos);
                    }
                    alarmAdapter.updateList(alarmInfos);

                    dismissProgress();
                    break;
                case MSG_QUERY_ALARM_LIST_FAIL:
                    dismissProgress();
                    break;
                case MSG_QUERY_HISTORY_FAIL:
                    dismissProgress();
                    break;
                case MSG_QUERY_HONEY_LIST_SUCCESS:
                    mIsQueryHoneyOK = true;
                    honeyInfos.clear();
                    honeyInfos = (List<HoneyInfo>) msg.obj;
                    if (honeyInfos != null && honeyInfos.size() > 0) {
                        if (config.getAlarmCode().equals("")) {
                            config.setAlarmCode(honeyInfos.get(0).getAlarmCode());
                        }
                        initAlarms();
                        initAlarmBaseInfo();
                        queryHolidayInfos();
                        if (mRlMainTab3.getVisibility() == View.VISIBLE) {
                            initVoiceMailData();
                        } else if (mRlMainTab2.getVisibility() == View.VISIBLE) {
                            initHistory();
                        }
                        UserManager.getInstance().start(getApplicationContext(),
                                config.getAlarmCode());
                        registerHeartBeat();
                        honeyAdapter.updateList(honeyInfos);
                        HoneyDao.getInstance().deteleAll();
                        HoneyDao.getInstance().insertHoneyData(honeyInfos);
                        adjustHoneyListHeight();
                        refreshMainScene();
                    }
                    break;
                case MSG_QUERY_HONEY_LIST_FAIL:
                    ToastUtil.showToast(MainActivity.this,
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                case MSG_SWITCH_CLOCK_STATUS:
                    new QueryAlarmClockListServer().start(config.getUserId(),
                            config.getAlarmCode(), queryAlarmListCallBack);
                    break;
                case MSG_UPDATE_HISTORY_STATUS:
                    new QueryClockHistoryListServer().start(config.getUserId(),
                            config.getAlarmCode(), queryHistoryCallBack);
                    break;
                case MSG_QUERY_VOICE_MAIL_LIST_SUCCESS:
                    mLvVoiceMail.onRefreshComplete();
                    _voiceInfos = (List<com.babacit.alarm.entity.Message>) msg.obj;
                    voiceMailAdapter.updateList(_voiceInfos);
                    dismissProgress();
                    break;
                case MSG_QUERY_VOICE_MAIL_LIST_FAIL:
                    dismissProgress();
                    mLvVoiceMail.onRefreshComplete();
                    ToastUtil.showToast(MainActivity.this,
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                case MSG_QUERY_HOLIDAY_LIST_SUCCESS:
                    List<Holiday> _holidays = (List<Holiday>) msg.obj;
                    HolidayDao.getInstance().deteleAll();
                    HolidayDao.getInstance().insertHolidayData(_holidays);
                    break;
                case MSG_QUERY_HOLIDAY_LIST_FAIL:

                    break;
                case MSG_HONEY_CHANGED:
                    if (!config.getAlarmCode().equals("")) {
                        UserManager.getInstance().stop();
                        UserManager.getInstance().start(getApplicationContext(),
                                config.getAlarmCode());
                    }
                    initHoneys();
                    refreshMainScene();
                    break;
                case MSG_CREATE_NEW_ALARM:
                    new QueryAlarmClockListServer().start(config.getUserId(),
                            config.getAlarmCode(), queryAlarmListCallBack);
                    break;
                case MSG_BIND_TEMINAL_SUCCESS:
                    initHoneys();
                    if (rightMenu.isMenuShowing()) {
                        rightMenu.toggle(true);
                    }
                    break;
                case MSG_SEND_VOICE_SUCCESS:
                    new QueryVoiceMsgServer().start(config.getUserId(),
                            config.getAlarmCode(), 0, queryVoiceMsgCallBack);
                    mLvVoiceMail.getRefreshableView().setSelection(
                            voiceMailAdapter.getCount() - 1);
                case MSG_SEND_VOICE_FAIL:
                    // 发送语音失败后关闭转圈圈动作
                    dismissProgress();
                    ToastUtil.showToast(getApplicationContext(),
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                case MSG_UPDATE_HONEY:
                    // honeyAdapter.updateList(hInfos);
                    break;
                case MSG_APPLY_MONITOR_SUCCESS:
                    event().addEventHandler(MainActivity.this);
                    doConfigEngine(Constants.CLIENT_ROLE_AUDIENCE);
                    config().mUid = Integer.parseInt(config.getUserId());
                    worker().joinChannel(config.getAlarmCode(), config().mUid);
                    break;
                case MSG_APPLY_MONITOR_FAIL:
                    // 尝试视频监控失败关闭转圈圈动作
                    dismissProgress();
                    ToastUtil.showToast(getApplicationContext(),
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                case MSG_UPLOAD_START_MONITOR_SUCCESS:
                    UserManager.getInstance().setMonitor("on");
                    break;
                case MSG_UPLOAD_START_MONITOR_FAIL:
                    // 开始视频监控失败关闭转圈圈动作
                    dismissProgress();
                    ToastUtil.showToast(getApplicationContext(),
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                case MSG_UPDATAE_ALARM_BASE_FAIL:
                    mSbChimeEveryHour.setChecked(!mSbChimeEveryHour.isChecked(), false);
                    ToastUtil.showToast(getApplicationContext(),
                            ErrUtils.getErrorReasonStr(msg.arg1));
                    break;
                default:
                    break;
            }
        }
    };

    private UMShareListener mShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA arg0) {
            ToastUtil.showToast(MainActivity.this, "开始分享");
        }

        @Override
        public void onResult(SHARE_MEDIA arg0) {
            ToastUtil.showToast(MainActivity.this, "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA arg0, Throwable arg1) {
            ToastUtil.showToast(MainActivity.this, "分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA arg0) {
        }

    };
    private ImageView addNew_reminder;
    private VoiceRecognition voiceRecognition = new VoiceRecognition(MainActivity.this);
    private LinearLayout llAddHead;

    /**
     * 调整宝贝列表界面ListView的高度
     */
    private void adjustHoneyListHeight() {
        if (honeyAdapter != null) {
            int totalHeight = 0;
            ViewGroup.LayoutParams params = mLvHoney.getLayoutParams();
            // if (honeyAdapter.getCount() > 2) {
            // for (int i = 0; i < 2; i++) {
            // View item = honeyAdapter.getView(i, null, mLvHoney);
            // item.measure(0, 0);
            // totalHeight += item.getMeasuredHeight();
            // }
            // params.height = totalHeight
            // + (mLvHoney.getDividerHeight() * (2 - 1));
            // } else {
            // for (int i = 0; i < honeyAdapter.getCount(); i++) {
            // View item = honeyAdapter.getView(i, null, mLvHoney);
            // item.measure(0, 0);
            // totalHeight += item.getMeasuredHeight();
            // }
            // params.height = totalHeight
            // + (mLvHoney.getDividerHeight() * (honeyAdapter
            // .getCount() - 1));
            // }
            for (int i = 0; i < honeyAdapter.getCount(); i++) {
                View item = honeyAdapter.getView(i, null, mLvHoney);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }
            params.height = totalHeight
                    + (mLvHoney.getDividerHeight() * (honeyAdapter.getCount() - 1));
            mLvHoney.setLayoutParams(params);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        bindField();
        config = new SharedConfig(this);
        // mSensor = new SoundMeter();
        if (config.getLoginState() == 1) {
            if (config.getGestureState() == 1) {
                Intent gesture = new Intent(MainActivity.this,
                        UnlockGesturePasswordActivity.class);
                startActivityForResult(gesture, 15);
            }
            if (!config.getGuideState()) {
                Intent guide = new Intent(MainActivity.this,
                        GuideActivity.class);
                startActivity(guide);
            }
            initHoneys();
        } else {
            Intent login = new Intent(MainActivity.this,
                    LoginRegisterActivity.class);
            startActivity(login);
            finish();
        }
        UmengUpdateAgent.update(this);

        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }

            @Override
            public void onStart(SHARE_MEDIA arg0) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    /**
     * 声网视频sdk
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                ((MainApplication) getApplication()).initWorkerThread();
            }
        }, 500);
    }

    protected RtcEngine rtcEngine() {
        return ((MainApplication) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((MainApplication) getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((MainApplication) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((MainApplication) getApplication()).getWorkerThread().eventHandler();
    }

    private void doConfigEngine(int cRole) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int prefIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (prefIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            prefIndex = ConstantApp.DEFAULT_PROFILE_IDX;
        }
        int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];

        worker().configEngine(cRole, vProfile);
    }

    private void doLeaveChannel() {
        videoFlag = false;
        getWindow().clearFlags(FLAG_MY_SCREEN);
        worker().leaveChannel(config().mChannel);
        mVideoLayout.removeAllViews();
        event().removeEventHandler(MainActivity.this);
        mIvMultiFunc.setImageResource(R.drawable.icon_check_status_start);
        UserManager.getInstance().setMonitor(null);
        new UploadMonitorStatusServer().start(
                config.getUserId(), config.getAlarmCode(), 2,
                uploadMonitorEndCallBack);
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                //声网sdk接入文档说必须用RtcEngine.CreateRendererView，不过用此则不能截图
//                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                SurfaceView surfaceV = new SWVideoSurfaceView(getApplicationContext());
                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
                mVideoLayout.addView(surfaceV);
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
//            	rtcEngine().setRemoteVideoStreamType(uid, Constants.VIDEO_STREAM_HIGH);
                dismissProgress();
                //监控闹钟终端30秒后自动断掉视频连接
                if (mVideoTimer != null)
                    mVideoTimer.cancel();

                mVideoTimer = new Timer();
                mVideoTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (videoFlag)
                                    doLeaveChannel();
                            }
                        });
                    }
                }, mMonitorTime * 1000);
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }


    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        videoFlag = true;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                getWindow().addFlags(FLAG_MY_SCREEN);
                RtcEngine engine = rtcEngine();
                //麦克风静音,禁止往网络发送本地音频流
                engine.muteLocalAudioStream(true);
                //禁用本地视频,用于只看不发的视频场景
                engine.enableLocalVideo(false);
                //打开扬声器
                engine.setEnableSpeakerphone(true);
                //设定扬声器音量:最小为0,最大为255
                engine.setSpeakerphoneVolume(255);

                new UploadMonitorStatusServer().start(config.getUserId(),
                        config.getAlarmCode(), 1, uploadMonitorStartCallBack);
                mIvMultiFunc.setImageResource(R.drawable.icon_check_status_end);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onError(int err) {
        mVideoErrorHandler.sendEmptyMessage(err);
    }

    private Handler mVideoErrorHandler = new Handler() {
        public void handleMessage(Message msg) {
            ToastUtil.showToast(MainActivity.this, "视频连接失败error=" + msg.what);
            doLeaveChannel();
        }


    };

    /**
     * 声网视频sdk
     */

    private void refreshLeftMenu() {
        if (alarmBaseInfo.getSleepStartTime() != null
                && alarmBaseInfo.getSleepEndTime() != null) {
            mTvSleepDuration.setText(String.format("%s-%s",
                    alarmBaseInfo.getSleepStartTime(),
                    alarmBaseInfo.getSleepEndTime()));
        }
        mTvBatteryPercent.setText(alarmBaseInfo.getBatteryPercent() + "%");
        percent = alarmBaseInfo.getBatteryPercent() == null ? 0 : Integer
                .parseInt(alarmBaseInfo.getBatteryPercent());
        refreshBatteryPercent();
        if (alarmBaseInfo.getSummerVacationTime() != null
                && alarmBaseInfo.getWinterVacationTime() != null) {
            String[] summers = alarmBaseInfo.getSummerVacationTime().split(";");
            String[] winters = alarmBaseInfo.getWinterVacationTime().split(";");
            if (summers.length == 2)
                mTvSummerVacation.setText(summers[0] + "~" + summers[1]);
            if (winters.length == 2)
                mTvWinterVacation.setText(winters[0] + "~" + winters[1]);
        }
        if (alarmBaseInfo.getReportStartTime() != null
                && alarmBaseInfo.getReportEndTime() != null) {
            mTvChimeDuration.setText(alarmBaseInfo.getReportStartTime() + "-"
                    + alarmBaseInfo.getReportEndTime());
        }
        if (alarmBaseInfo.getReportStatus() == 1) {
            mSbChimeEveryHour.setChecked(true);
        } else {
            mSbChimeEveryHour.setChecked(false);
        }
//		if (alarmBaseInfo.getTouchScreenStatus() == 1) {
//			mSbTerminalTouchable.setChecked(true);
//		} else
//			mSbTerminalTouchable.setChecked(false);
        switch (alarmBaseInfo.getChargeStatus()) {
            case 0:
                mIvPlug.setVisibility(View.GONE);
                break;
            case 1:
                mIvPlug.setVisibility(View.VISIBLE);
                break;
            case 2:
                mIvPlug.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void queryHolidayInfos() {
        new QueryHolidayServer().start(config.getUserId(),
                config.getAlarmCode(), queryHolidayCallBack);
    }

    private void initAlarmBaseInfo() {
        new QueryAlarmBaseInfoServer().start(config.getUserId(),
                config.getAlarmCode(), queryAlarmBaseInfoCallBack);
    }

    private void initAlarms() {
        new QueryAlarmClockListServer().start(config.getUserId(),
                config.getAlarmCode(), queryAlarmListCallBack);
    }

    private void initHistory() {
        // 切换底部TAB默认选中已完成
        mRbFinished.setChecked(true);
        new QueryClockHistoryListServer().start(config.getUserId(),
                config.getAlarmCode(), queryHistoryCallBack);
        historyAdapter = new HistoryAdapter(this, _finishedHistoryInfos);
        historyAdapter.setMainHandler(mHandler);
        historyAdapter.notifyDataSetChanged();
        mLvHistory.setAdapter(historyAdapter);
    }

    private void initHoneys() {
        new QueryHoneyInfosServer().start(config.getUserId(), "",
                queryHoneyListCallBack);
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.sb_chime_every_hour_switch:
                    new UpdateAlarmStatusServer().start(config.getUserId(), config
                            .getAlarmCode(), 0, isChecked ? 1 : 0, updateAlarmStatusCallBack);
                    break;
                default:
                    break;
            }
        }
    };


    private OnClickListener click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_multi_func:
                    if (mRlMainTab1.getVisibility() == View.VISIBLE) {
                        // 判断是否开启语音解析
                        if (AiConfigConstant.Companion.getAI_OPEN_VOICE()) {
                            permissions_type_voice = PERMISSIONS_TYPE_VOICE;
                            requestVoicePermission(PERMISSIONS_TYPE_VOICE);
                        } else {
                            ChooseAlarmTypeDlgFragment d = new ChooseAlarmTypeDlgFragment();
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("grid_1", getResources()
                                    .getStringArray(R.array.base_alarm_type));
                            d.setArguments(bundle);
                            d.show(getFragmentManager(), "");
                        }
                    } else if (mRlMainTab2.getVisibility() == View.VISIBLE) {
                    } else if (mRlMainTab3.getVisibility() == View.VISIBLE) {

                        // ToastUtil.showToast(getApplicationContext(), "录制语音");
                    } else {
                        if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
                            ToastUtil.showToast(getApplicationContext(), "请检查您的网络!");
                            return;
                        }
                        permissions_type_camera = PERMISSIONS_TYPE_VIDEOMONITORING;
                        permissionsCamera(PERMISSIONS_TYPE_VIDEOMONITORING);
                    }
                    break;

                //新增的提醒按钮 SYY
                case R.id.iv_main_addNew_reminder:
                    ChooseAlarmTypeDlgFragment d = new ChooseAlarmTypeDlgFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("grid_1", getResources()
                            .getStringArray(R.array.base_alarm_type));
                    d.setArguments(bundle);
                    d.show(getFragmentManager(), "");
                    break;


                case R.id.rl_left_user_portrait:
                    Intent babyInfo = new Intent(MainActivity.this,
                            BabyInfoActivity.class);
                    startActivityForResult(babyInfo, 13);
                    break;
                case R.id.riv_main_tab_portrait:
                    toggleMenu();
                    break;
                // case R.id.layout_main_tab1:
                // if (leftMenu.isActivated()) {
                // toggleMenu();
                // }
                // if (rightMenu.isActivated()) {
                // rightMenu.toggle(true);
                // }
                // break;
                case R.id.btn_show_menu:
                    if (!rightMenu.isMenuShowing()) {
                        // Intent intent = new Intent(MainActivity.this,
                        // MainActivity.class);
                        // startActivity(intent);
                        rightMenu.toggle(true);
                    }
                    // if (rightMenu.isMenuShowing()) {
                    // leftMenu.setAlpha(0.5f);
                    // } else {
                    // leftMenu.setAlpha(1.0f);
                    // }
                    break;
                case R.id.rl_sleep_time:
                    Intent sleep = new Intent(MainActivity.this,
                            SleepSettingActivity.class);
                    sleep.putExtra("sleep", mTvSleepDuration.getText().toString());
                    startActivityForResult(sleep, 0);
                    break;
                case R.id.rl_chime_every_hour:
                    Intent chime = new Intent(MainActivity.this,
                            ChimeSettingActivity.class);
                    chime.putExtra("chime", mTvChimeDuration.getText().toString());
                    startActivityForResult(chime, 1);
                    break;
                case R.id.rl_little_red_flower:
                    Intent flower = new Intent(MainActivity.this,
                            LittleRedFlowerActivity.class);
                    startActivity(flower);
                    break;
                case R.id.rl_vacation:
                    Intent vacation = new Intent(MainActivity.this,
                            VacationSettingActivity.class);
                    vacation.putExtra("summer", mTvSummerVacation.getText()
                            .toString());
                    vacation.putExtra("winter", mTvWinterVacation.getText()
                            .toString());
                    startActivityForResult(vacation, 2);
                    break;
                case R.id.ll_add_devices:
                    permissions_type_camera = PERMISSIONS_TYPE_ADDDEVICE;
                    permissionsCamera(PERMISSIONS_TYPE_ADDDEVICE);
                    break;
                case R.id.ll_account_setting:
                    Intent setting = new Intent(MainActivity.this,
                            AccountSettingActivity.class);
                    startActivity(setting);
                    break;
                case R.id.ll_help:
                    Intent help = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(help);
                    break;
                case R.id.ll_about:
                    Intent about = new Intent(MainActivity.this,
                            AboutActivity.class);
                    startActivity(about);
                    break;
                case R.id.ll_feedback:
                    // ToastUtil.showToast(MainActivity.this, "意见反馈");
//				FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
//				agent.startFeedbackActivity();

                    FeedbackAPI.openFeedbackActivity();

                    break;
                case R.id.rb_finished_history:

                    historyAdapter.updateList(_finishedHistoryInfos);
                    mRbFinished.setChecked(true);
                    mRbUnfinished.setChecked(false);
                    break;
                case R.id.rb_unfinished_history:
                    historyAdapter.updateList(_unfinishedHistoryInfos);
                    mRbFinished.setChecked(false);
                    mRbUnfinished.setChecked(true);
                    break;
                case R.id.iv_album:
                    File folder = new File(Constant.CAPTURE_PATH);
                    String[] all = folder.list();
                    if (all != null && all.length > 0) {
                        scanPath = Constant.CAPTURE_PATH + File.separator + all[0];
//					startScan();
//					shareSingleImage(v,scanPath);
                        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                        getImage.addCategory(Intent.CATEGORY_OPENABLE);
                        getImage.setType("image/jpeg");
                        startActivityForResult(getImage, RESULT_CODE_FROM_IMAGE);
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "暂无截图");
                    }
                    break;
                case R.id.layout_video:
                case R.id.iv_capture:
                    if (!videoFlag) {
                        ToastUtil.showToast(getApplicationContext(), "尚未建立连接！");
                        return;
                    }
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断是否可以对sdcard进行操作
                        File dir = new File(Constant.CAPTURE_PATH);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        if (mVideoLayout.getChildCount() > 0) {
                            final SWVideoSurfaceView view = (SWVideoSurfaceView) mVideoLayout.getChildAt(0);
                            view.setSWVideoSVListener(new SWVideoSVListener() {

                                @Override
                                public void onDrawFrame(GL10 gl) {
                                    try {
                                        final String path = Constant.CAPTURE_PATH + File.separator + System.currentTimeMillis() + ".jpg";
                                        Bitmap bm = createBitmapFromGLSurface(0, 0, view.getWidth(), view.getHeight(), gl);
                                        FileOutputStream out = new FileOutputStream(path);
                                        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                        out.flush();
                                        out.close();

                                        // 发送广播通知更新图库，进入相册就可以找到截图了
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 如果是Android 4.4及以上版本
                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            File file = new File(path);
                                            Uri uri = Uri.fromFile(file);
                                            intent.setData(uri);
                                            MainActivity.this.sendBroadcast(intent);
                                        } else {
//										Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
//										Uri uri = Uri.parse("file://"+ Environment.getExternalStorageDirectory());
//										intent.setData(uri);
//										MainActivity.this.sendBroadcast(intent);
                                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                                                    + Environment.getExternalStorageDirectory())));
                                        }

                                        Intent capture = new Intent(MainActivity.this, ScreenCaptureActivity.class);
                                        capture.putExtra("title", "记忆猫截图");
                                        capture.putExtra("path", path);
                                        startActivityForResult(capture, 14);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {//将查看相册图标设置成最近的截图
                                                mIvAlbum.setImageBitmap(BitmapFactory.decodeFile(path));
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast(getApplicationContext(), "截图失败，请重试!");
                                            }
                                        });
                                    }
                                }
                            });
                        } else
                            ToastUtil.showToast(getApplicationContext(), "截图失败，请重试!");
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "SD卡不存在，或者不能对其进行保存操作!");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 申请相机权限
     */
    private void permissionsCamera(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> toApplyList = new ArrayList<String>();
            for (String perm : permissionsCamera) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                    toApplyList.add(perm);
                    //进入到这里代表没有权限.
                }
            }
            String tmpList[] = new String[toApplyList.size()];
            if (!toApplyList.isEmpty()) {
                ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), PERMISSIONS_CAMERA);
            } else {
                afterRequestPermission(type);
            }
        } else {
            afterRequestPermission(type);
        }
    }

    /**
     * 权限申请后的处理
     *
     * @param type
     */
    private void afterRequestPermission(int type) {
        switch (type) {
            case PERMISSIONS_TYPE_ADDDEVICE:
                Intent addDevice = new Intent(MainActivity.this,
                        MipcaActivityCapture.class);
                startActivityForResult(addDevice, 16);
                break;
            case PERMISSIONS_TYPE_VIDEOMONITORING:
                if (videoFlag) {
                    showProgressDialog("正在断开连接...", true);
                    doLeaveChannel();
                } else {
                    showProgressDialog("正在尝试建立连接...", true);
                    new UploadMonitorStatusServer().start(config.getUserId(),
                            config.getAlarmCode(), 0, applyMonitorCallBack);
                }
                break;
            case PERMISSIONS_TYPE_VOICE:
                voiceRecognition.initAll();
                break;
            case PERMISSIONS_TYPE_VOICEIM:
                MobclickAgent.onEvent(getApplicationContext(),
                        "check_voice_mail");
                mIvMultiFunc.setClickable(false);
                mTvRecordHint.setVisibility(View.VISIBLE);
                mIvMultiFunc
                        .setImageResource(R.drawable.icon_multi_func_record_voice_enable);
                setTabVisible(View.GONE, View.GONE,
                        View.VISIBLE, View.GONE);
                showProgressDialog("", true);
                initVoiceMailData();
                if (mIsQueryHoneyOK) {
                    if (honeyAdapter.getCount() < 1) {
                        bind = new MyDialogFragment();
                        Bundle args = new Bundle();
                        args.putString(Constant.DLG_CONTENT,
                                "未绑定终端，无法使用留言信箱功能！");
                        args.putString(Constant.DLG_CANCEL_TEXT,
                                "取消");
                        args.putString(Constant.DLG_CONFIRM_TEXT,
                                "绑定终端");
                        bind.setArguments(args);
                        bind.show(getFragmentManager(), "bind");
                    }
                } else {
                    initHoneys();
                }
                break;

        }

    }

    /**
     * 申请语音权限
     */
    private void requestVoicePermission(int type) {
        //23以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> toApplyList = new ArrayList<String>();
            for (String perm : permissionsVoices) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                    toApplyList.add(perm);
                    //进入到这里代表没有权限.
                }
            }
            String tmpList[] = new String[toApplyList.size()];
            if (!toApplyList.isEmpty()) {
                ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), PERMISSIONS_VOICE);
            } else {
                afterRequestPermission(type);
            }
        } else {
            afterRequestPermission(type);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int countDeniedVoice = 0;
        int countDeniedCamera = 0;
        int countReminder=0;
        if (requestCode == PERMISSIONS_VOICE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (!showRequestPermission&&countReminder==1) {
                        Toast.makeText(this, R.string.permission_reminder, Toast.LENGTH_SHORT).show();
                        countReminder++;
                    }
                    countDeniedVoice++;
                }
            }
            if (countDeniedVoice == 0) {
                afterRequestPermission(permissions_type_voice);
            }
        } else if (requestCode == PERMISSIONS_CAMERA) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (!showRequestPermission&&countReminder==1) {
                        Toast.makeText(this, R.string.permission_reminder, Toast.LENGTH_SHORT).show();
                        countReminder++;
                    }
                    countDeniedCamera++;
                }
            }
            if (countDeniedCamera == 0) {
                afterRequestPermission(permissions_type_camera);
            }
        }
    }


    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    private void startScan() {
        if (conn != null) {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(this, this);
        conn.connect();
    }

    public void shareSingleImage(View view, String imagePath) {
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Log.d("share", "uri:" + imageUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    private void refreshMainScene() {
        mCurrHoney = HoneyDao.getInstance().queryHoneyByCode(
                config.getAlarmCode());
        if (mCurrHoney != null) {
            mTvMainTitle.setText(mCurrHoney.getNickName());
            mTvCurrHoneyName.setText(mCurrHoney.getNickName());
            if (mCurrHoney.getImgUrl() != null) {
                ImageLoader.getInstance().displayImage(mCurrHoney.getImgUrl(),
                        mRivMainTabPortrait);
                ImageLoader.getInstance().displayImage(mCurrHoney.getImgUrl(),
                        mRivPortraitOnLeftMenu);
            } else {
                mRivMainTabPortrait
                        .setImageResource(R.drawable.portrait_default);
                mRivPortraitOnLeftMenu
                        .setImageResource(R.drawable.portrait_default);
            }
        }
    }

    private void bindField() {
        mTvMainTitle = (TextView) findViewById(R.id.txt_main_title);
        mIvMultiFunc = (ImageView) findViewById(R.id.iv_multi_func);
        mTvRecordHint = (TextView) findViewById(R.id.tv_record_voice_hint);
        mIvBatteryPercent = (ImageView) findViewById(R.id.iv_battery_percent);
        mIvPlug = (ImageView) findViewById(R.id.iv_charge_plug);
        mBmFullPercent = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_charge_percent);
        mTvBatteryPercent = (TextView) findViewById(R.id.tv_battery_percent);
        mRlMainTab1 = (RelativeLayout) findViewById(R.id.layout_main_tab1);
        mRlMainTab2 = (LinearLayout) findViewById(R.id.layout_main_tab2);
        mRlMainTab3 = (RelativeLayout) findViewById(R.id.layout_main_tab3);
        mRlMainTab4 = (RelativeLayout) findViewById(R.id.layout_main_tab4);
        mIvAlbum = (ImageView) findViewById(R.id.iv_album);
        mIvCapture = (ImageView) findViewById(R.id.iv_capture);
        //SYY
        addNew_reminder = findViewById(R.id.iv_main_addNew_reminder);
        addNew_reminder.setOnClickListener(click);
        llAddHead = findViewById(R.id.ll_main_head_Add);
        if (AiConfigConstant.Companion.getAI_OPEN_VOICE()) {
            llAddHead.setVisibility(View.VISIBLE);
        } else {
            llAddHead.setVisibility(View.GONE);
        }
        //SYY
        mIvAlbum.setOnClickListener(click);
        mIvCapture.setOnClickListener(click);
        mLvAlarm = (ListView) findViewById(R.id.lv_main_alarm);
        alarmAdapter = new AlarmAdapter(this, alarmInfos);
        alarmAdapter.setMainHandler(mHandler);
        mLvAlarm.setAdapter(alarmAdapter);

        mLvHistory = (ListView) findViewById(R.id.lv_main_history);
        mLvVoiceMail = (PullToRefreshListView) findViewById(R.id.lv_voice_mail);
        mLvVoiceMail.setMode(Mode.PULL_FROM_END);
        mLvVoiceMail.setOnRefreshListener(pullToRefreshListener);

        volume = (ImageView) this.findViewById(R.id.volume);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        img1 = (ImageView) this.findViewById(R.id.img1);
        sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
        del_re = (LinearLayout) this.findViewById(R.id.del_re);
        voice_rcd_hint_rcding = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_tooshort);
        mSbChimeEveryHour = (SwitchButton) findViewById(R.id.sb_chime_every_hour_switch);
//		mSbTerminalTouchable = (SwitchButton) findViewById(R.id.sb_terminal_touchable_swith);
        mSbChimeEveryHour.setOnCheckedChangeListener(onCheckedChangeListener);
//		mSbTerminalTouchable.setOnCheckedChangeListener(onCheckedChangeListener);
        setOnAlarmItemClickListener();
        setOnAlarmItemLongClickListener();
        mVideoLayout = (FrameLayout) findViewById(R.id.layout_video);
        mVideoLayout.setOnTouchListener(new DoubleClick());
        leftMenu = (SlidingMenu) findViewById(R.id.id_menu);
        leftMenu.setMainHandler(mHandler);
        mTvSleepDuration = (TextView) findViewById(R.id.tv_sleep_time_duration);
        mTvChimeDuration = (TextView) findViewById(R.id.tv_chime_duration);

        mTvSummerVacation = (TextView) findViewById(R.id.tv_summer_vacation);
        mTvWinterVacation = (TextView) findViewById(R.id.tv_winter_vacation);
        mTvCurrHoneyName = (TextView) findViewById(R.id.tv_curr_honey_name);

        rightMenu = new com.jeremyfeinstein.slidingmenu.lib.SlidingMenu(this);
        rightMenu
                .setMode(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.RIGHT);
        rightMenu.setBehindScrollScale(0.5f);
        rightMenu
                .setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_NONE);
        // rightMenu.setSlidingEnabled(true);
        rightMenu.setBehindOffset(ScreenUtils.getScreenWidth(this) * 2 / 5);
        rightMenu.attachToActivity(this,
                com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.RIGHT);
        rightMenu.setMenu(R.layout.layout_right_menu);
        mLvHoney = (ListView) findViewById(R.id.lv_honey);
        honeyAdapter = new HoneyAdapter(this, honeyInfos);
        mLvHoney.setAdapter(honeyAdapter);
        setOnHoneyItemClickListener();
        mGvShare = (MyGridView) findViewById(R.id.gv_share);
        setOnShareItemClickListener();

        bottomHorizontalView = (MyHorizontalScrollView) findViewById(R.id.bottom_horizontal);
        bottomHorizontalView.setParams(titles, images, 4, this);
        radioGroup = (RadioGroup) bottomHorizontalView
                .findViewById(R.id.hori_radio_group);
        setOnCheckedChangeListener();
        mIvMultiFunc.setOnClickListener(click);
        mIvMultiFunc.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        findViewById(R.id.rl_left_user_portrait).setOnClickListener(click);
        findViewById(R.id.btn_show_menu).setOnClickListener(click);
        findViewById(R.id.rl_sleep_time).setOnClickListener(click);
        mRivMainTabPortrait = (RoundImageView) findViewById(R.id.riv_main_tab_portrait);
        mRivMainTabPortrait.setOnClickListener(click);
        mRivPortraitOnLeftMenu = (RoundImageView) findViewById(R.id.riv_left_portrait);
        mRivMainTabPortrait.setScaleType(ScaleType.CENTER);
        mRivPortraitOnLeftMenu.setScaleType(ScaleType.CENTER);
        findViewById(R.id.rl_chime_every_hour).setOnClickListener(click);
        findViewById(R.id.rl_little_red_flower).setOnClickListener(click);
        findViewById(R.id.rl_vacation).setOnClickListener(click);

        findViewById(R.id.ll_add_devices).setOnClickListener(click);
        findViewById(R.id.ll_account_setting).setOnClickListener(click);
        findViewById(R.id.ll_help).setOnClickListener(click);
        findViewById(R.id.ll_about).setOnClickListener(click);
        findViewById(R.id.ll_feedback).setOnClickListener(click);

        mRbFinished = (RadioButton) findViewById(R.id.rb_finished_history);
        mRbUnfinished = (RadioButton) findViewById(R.id.rb_unfinished_history);
        mRbFinished.setOnClickListener(click);
        mRbUnfinished.setOnClickListener(click);

    }


    private void refreshBatteryPercent() {
        Bitmap bmp = Bitmap.createBitmap(mBmFullPercent, 0,
                mBmFullPercent.getHeight() * (100 - percent) / 100,
                mBmFullPercent.getWidth(), (mBmFullPercent.getHeight()
                        * percent / 100) == 0 ? 1 : (mBmFullPercent.getHeight()
                        * percent / 100));
        if (mIvBatteryPercent != null && bmp != null && !bmp.isRecycled()) {
            mIvBatteryPercent.setVisibility(View.VISIBLE);
            mIvBatteryPercent.setImageBitmap(bmp);
        }
    }

    private void setOnAlarmItemLongClickListener() {
        mLvAlarm.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                MyDialogFragment delete = new MyDialogFragment();
                Bundle args = new Bundle();
                args.putString(Constant.DLG_CONTENT, "是否删除此闹钟？");
                args.putString(Constant.DLG_CANCEL_TEXT, "取消");
                args.putString(Constant.DLG_CONFIRM_TEXT, "删除");
                args.putString("clock_id", alarmInfos.get(arg2).getClockId());
                delete.setArguments(args);
                delete.show(getFragmentManager(), "delete_alarm");
                return true;
            }
        });
    }

    private void setOnAlarmItemClickListener() {
        mLvAlarm.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                switch (alarmInfos.get(pos).getClockType()) {
                    case 1:
                        Intent getUp = new Intent(MainActivity.this,
                                GetupAlarmActivity.class);
                        getUp.putExtra("modify", true);
                        getUp.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(getUp, 3);
                        break;
                    case 2:
                        Intent homework = new Intent(MainActivity.this,
                                HomeworkRemindActivity.class);
                        homework.putExtra("modify", true);
                        homework.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(homework, 5);
                        break;
                    case 3:
                        Intent birthday = new Intent(MainActivity.this,
                                BirthdayManagerAlarmActivity.class);
                        birthday.putExtra("modify", true);
                        birthday.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(birthday, 6);
                        break;
                    case 4:
                        Intent daily = new Intent(MainActivity.this,
                                DailyRemindAlarmActivity.class);
                        daily.putExtra("modify", true);
                        daily.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(daily, 7);
                        break;
                    case 5:
                        Intent eyeProtect = new Intent(MainActivity.this,
                                EyesProtectActivity.class);
                        eyeProtect.putExtra("modify", true);
                        eyeProtect.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(eyeProtect, 8);
                        break;
                    case 6:
                        Intent anni = new Intent(MainActivity.this,
                                AnniversaryAlarmActivity.class);
                        anni.putExtra("modify", true);
                        anni.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(anni, 9);
                        break;
                    case 7:
                        Intent water = new Intent(MainActivity.this,
                                WaterAndMedicineActivity.class);
                        water.putExtra("modify", true);
                        water.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(water, 10);
                        break;
                    case 8:
                        Intent festival = new Intent(MainActivity.this,
                                FestivalRemindAlarmActivity.class);
                        festival.putExtra("modify", true);
                        festival.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(festival, 11);
                        break;
                    case 9:
                        Intent custom = new Intent(MainActivity.this,
                                CustomAlarmActivity.class);
                        custom.putExtra("modify", true);
                        custom.putExtra("alarm", alarmInfos.get(pos));
                        startActivityForResult(custom, 12);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setOnHoneyItemClickListener() {
        mLvHoney.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                config.setAlarmCode(honeyInfos.get(position).getAlarmCode());
                honeyAdapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(MSG_HONEY_CHANGED);
            }
        });
    }

    private void initShareGrid() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        int[] shareImgs = {R.drawable.icon_share_qq,
                R.drawable.icon_share_qzone, /*R.drawable.icon_share_qq_weibo,*/
                R.drawable.icon_share_wechat,
                R.drawable.icon_share_wechat_friends,
                R.drawable.icon_share_weibo};
        for (int i = 0; i < shareImgs.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", shareImgs[i]);
            items.add(map);
        }
        SimpleAdapter shareAdapter = new SimpleAdapter(this, items,
                R.layout.grid_image_item, new String[]{"img"},
                new int[]{R.id.iv_grid_item});
        mGvShare.setAdapter(shareAdapter);
    }

    private void setOnShareItemClickListener() {
        initShareGrid();
        mGvShare.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
                    ToastUtil.showToast(getApplicationContext(), "请检查您的网络!");
                    return;
                }
                switch (position) {
                    case 0://QQ
                        directShare(SHARE_MEDIA.QQ);
                        break;
                    case 1://QQ空间
                        directShare(SHARE_MEDIA.QZONE);
                        break;
//				case 2://腾讯微博
//					//TODO 要做
//					directShare(SHARE_MEDIA.TENCENT);
//					break;
                    case 2://微信
                        directShare(SHARE_MEDIA.WEIXIN);
                        break;
                    case 3://微信朋友圈
                        directShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                        break;
                    case 4://新浪微博
                        directShare(SHARE_MEDIA.SINA);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void directShare(SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(MainActivity.this, R.drawable.ic_launcher);
        ShareContent shareContent = new ShareContent();
        shareContent.subject = "记忆猫";
        shareContent.mText = getResources().getString(R.string.txt_product_name);
        UMWeb web = new UMWeb("http://download.babacit.net:8081");
        web.setTitle(shareContent.subject);//标题
        web.setThumb(thumb);  //缩略图,注意在新浪平台，缩略图属于必传参数，否则会报错
        web.setDescription(shareContent.mText);
        new ShareAction(MainActivity.this).setPlatform(platform)
                .setShareContent(shareContent)
                .withText(shareContent.subject)
                .withMedia(thumb)
                .withMedia(web)
                .setCallback(mShareListener)
                .share();
    }

    private void setOnCheckedChangeListener() {
        radioGroup
                .setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (radioGroup.getChildAt(checkedId) != null) {
                            switch (checkedId) {
                                case 0:
                                    MobclickAgent.onEvent(getApplicationContext(),
                                            "check_alarm_list");
                                    mIvMultiFunc.setClickable(true);
                                    mTvRecordHint.setVisibility(View.GONE);
                                    if (AiConfigConstant.Companion.getAI_OPEN_VOICE()) {
                                        mIvMultiFunc.setImageResource(R.drawable.icon_multi_func_record_voice_enable);
                                    } else {
                                        mIvMultiFunc.setImageResource(R.drawable.icon_multi_func_add_alarm);
                                    }
                                    setTabVisible(View.VISIBLE, View.GONE,
                                            View.GONE, View.GONE);
                                    showProgressDialog("", true);
                                    initAlarms();
                                    if (!mIsQueryHoneyOK) {
                                        initHoneys();
                                    }
                                    break;
                                case 1:
                                    MobclickAgent.onEvent(getApplicationContext(),
                                            "check_history");
                                    mIvMultiFunc.setClickable(true);
                                    mTvRecordHint.setVisibility(View.GONE);
                                    mIvMultiFunc
                                            .setImageResource(R.drawable.icon_multi_func_history);
                                    setTabVisible(View.GONE, View.VISIBLE,
                                            View.GONE, View.GONE);
                                    showProgressDialog("", true);
                                    initHistory();
                                    if (!mIsQueryHoneyOK) {
                                        initHoneys();
                                    }
                                    break;
                                case 2:
                                    permissions_type_voice = PERMISSIONS_TYPE_VOICEIM;
                                    requestVoicePermission(PERMISSIONS_TYPE_VOICEIM);
                                    break;
                                case 3:
                                    MobclickAgent.onEvent(getApplicationContext(),
                                            "check_status");
                                    mIvMultiFunc.setClickable(true);
                                    mTvRecordHint.setVisibility(View.GONE);
                                    if (videoFlag) {
                                        mIvMultiFunc
                                                .setImageResource(R.drawable.icon_check_status_end);
                                    } else
                                        mIvMultiFunc
                                                .setImageResource(R.drawable.icon_check_status_start);
                                    setTabVisible(View.GONE, View.GONE, View.GONE,
                                            View.VISIBLE);
                                    if (mIsQueryHoneyOK) {
                                        if (honeyAdapter.getCount() < 1) {
                                            bind = new MyDialogFragment();
                                            Bundle args1 = new Bundle();
                                            args1.putString(Constant.DLG_CONTENT,
                                                    "未绑定终端，无法使用视频查看功能！");
                                            args1.putString(Constant.DLG_CANCEL_TEXT,
                                                    "取消");
                                            args1.putString(Constant.DLG_CONFIRM_TEXT,
                                                    "绑定终端");
                                            bind.setArguments(args1);
                                            bind.show(getFragmentManager(), "bind");
                                        }
                                    } else {
                                        initHoneys();
                                    }
                                    File[] allFiles = new File(
                                            Constant.CAPTURE_PATH).listFiles();
                                    if (allFiles != null && allFiles.length > 0) {
                                        int max = 0;
                                        for (int i = 0; i < allFiles.length; i++) {
                                            if (allFiles[max].lastModified() < allFiles[i]
                                                    .lastModified()) {
                                                max = i;
                                            }
                                        }
                                        mIvAlbum.setImageBitmap(BitmapFactory.decodeFile(allFiles[max]
                                                .getAbsolutePath()));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

//	private void sortAlarmInfos(List<AlarmInfo> alarms) {
//		TimeTest timeTest = new TimeTest();
//		for (int i = 0; i < alarms.size(); i++) {
//			AlarmInfo a = alarms.get(i);
//			if (a.getTimeType() == 3) {
//				String day = DateUtils.getDateStringDay();
//				if (day.compareTo(a.getDate()) <= 0) {
//					a.setDate(DateUtils.getDateStringYearMonth() + "-"
//							+ a.getDate());
//				} else {
//					if (DateUtils.getDateStringMonth().equals("12")) {
//						a.setDate((Integer.parseInt(DateUtils
//								.getDateStringYear()) + 1)
//								+ "-01"
//								+ "-"
//								+ a.getDate());
//					} else {
//						a.setDate(DateUtils.getDateStringYear()
//								+ "-"
//								+ (Integer.parseInt(DateUtils
//										.getDateStringMonth()) + 1) + "-"
//								+ a.getDate());
//					}
//				}
//			} else if (a.getTimeType() == 5) {
//				String week = DateUtils.getWeek();
//				String[] weeks = a.getDay().split(",");
//				Arrays.sort(weeks);
//				if (week.compareTo(weeks[0]) <= 0) {
//					a.setDate(timeTest.getCurrDateOfWeek(weeks[0]));
//				} else if (week.compareTo(weeks[week.length() - 1]) > 0) {
//					a.setDate(timeTest.getNextDateOfWeek(weeks[0]));
//				}
//			} else if (a.getTimeType() == 2 && a.getClockType() != 8) {
//				if (a.getTimes().get(a.getTimes().size() - 1)
//						.compareTo(DateUtils.getTime()) < 0) {
//					a.setDate(timeTest.getTomorrowDate());
//				} else
//					a.setDate(DateUtils.getDateStringYearMonthDay());
//			}
//		}
//		Collections.sort(alarms, new Comparator<AlarmInfo>() {
//			@Override
//			public int compare(AlarmInfo lhs, AlarmInfo rhs) {
//				String a0 = lhs.getDate() + lhs.getTimes().get(0);
//				String a1 = rhs.getDate() + rhs.getTimes().get(0);
//				if (a0.compareTo(a1) <= 0) {
//					return -1;
//				} else
//					return 1;
//			}
//		});
//	}

    private void initVoiceMailData() {
        new QueryVoiceMsgServer().start(config.getUserId(),
                config.getAlarmCode(), 0, queryVoiceMsgCallBack);
        voiceMailAdapter = new VoiceMailAdapter(this, _voiceInfos);
        mLvVoiceMail.setAdapter(voiceMailAdapter);
    }

    private void setTabVisible(int v1, int v2, int v3, int v4) {
        mRlMainTab1.setVisibility(v1);
        mRlMainTab2.setVisibility(v2);
        mRlMainTab3.setVisibility(v3);
        mRlMainTab4.setVisibility(v4);
    }

    public void toggleMenu() {
        leftMenu.toggle();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (rightMenu.isMenuShowing()) {
                    rightMenu.toggle(true);
                    return false;
                } else if (leftMenu.getMenuState()) {
                    leftMenu.toggle();
                    return false;
                } else {
                    if (videoFlag) {
                        doLeaveChannel();
                    }

//				finish();
                }
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("create_alarm");
        registerReceiver(receiver, filter);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (leftMenu.getMenuState()) {
            toggleMenu();
        }
        if (rightMenu.isMenuShowing()) {
            rightMenu.toggle(true);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN)
            mNoVoiceBtnDown = false;

        if (mRlMainTab3.getVisibility() != View.VISIBLE || mNoVoiceBtnDown) {
            return super.dispatchTouchEvent(event);
        }

        int[] location = new int[2];
        mIvMultiFunc.getLocationInWindow(location);
        int[] del_location = new int[2];
        del_re.getLocationInWindow(del_location);
        int del_Y = del_location[1];
        int del_x = del_location[0];
        int multiX = location[0];
        int multiY = location[1];
        int multiWidth = mIvMultiFunc.getMeasuredWidth();
        int multiHeight = mIvMultiFunc.getMeasuredHeight();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            Logger.d("evnet.getY():" + event.getY() + "\nmultiY:" + multiY
                    + "\nmultiHeight:" + multiHeight);
            if (event.getY() > multiY && event.getX() > multiX
                    && multiY + multiHeight > event.getY()
                    && multiX + multiWidth > event.getX()) {//在中间按钮范围内
                if (!Environment.getExternalStorageDirectory().exists()) {
                    ToastUtil.showToast(getApplicationContext(), "没有SD卡");
                    mNoVoiceBtnDown = true;
                    return true;
                }

                startVoiceT = System.currentTimeMillis();
                voiceName = String.valueOf(startVoiceT);
                startRecord(voiceName);
                rcChat_popup.setVisibility(View.VISIBLE);
                voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                voice_rcd_hint_rcding.setVisibility(View.GONE);
                voice_rcd_hint_tooshort.setVisibility(View.GONE);
                // mHandler.postDelayed(new Runnable() {
                // public void run() {
                // if (!isShort) {
                voice_rcd_hint_loading.setVisibility(View.GONE);
                voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
                // }
                // }
                // }, 300);
                img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
            } else {
                mNoVoiceBtnDown = true;
                return super.dispatchTouchEvent(event);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            stopRecord();
            endVoiceT = System.currentTimeMillis();
            if (event.getY() >= del_Y
                    && event.getY() <= del_Y + del_re.getHeight()
                    && event.getX() >= del_x
                    && event.getX() <= del_x + del_re.getWidth()) {
                rcChat_popup.setVisibility(View.GONE);
                img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
            } else {
                voice_rcd_hint_rcding.setVisibility(View.GONE);
                voiceTime = (int) ((endVoiceT - startVoiceT) / 1000);
                if (voiceTime < 1) {
                    voice_rcd_hint_loading.setVisibility(View.GONE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            voice_rcd_hint_tooshort.setVisibility(View.GONE);
                            rcChat_popup.setVisibility(View.GONE);
                        }
                    }, 500);
                    return true;
                }
                mLvVoiceMail.getRefreshableView().setSelection(
                        voiceMailAdapter.getCount() - 1);

                voiceInfo.setMessageUrl(Constant.VOICE_PATH + File.separator
                        + voiceName);
                voiceInfo.setUser(UserDao.getInstance().queryUserByUserId(
                        config.getUserId()));
                voiceInfo.setVoiceTime(voiceTime);
                new SendVoiceMsgServer().start(config.getUserId(),
                        config.getAlarmCode(), Constant.VOICE_PATH
                                + File.separator + voiceName + ".mp3",
                        voiceTime, sendVoiceMsgCallBack);
            }
        }
        if (event.getY() < multiY) {// 手势按下的位置不在语音录制按钮的范围内
            Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
                    R.anim.cancel_rc);
            Animation mBigAnimation = AnimationUtils.loadAnimation(this,
                    R.anim.cancel_rc2);
            img1.setVisibility(View.GONE);
            del_re.setVisibility(View.VISIBLE);
            del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
            if (event.getY() >= del_Y
                    && event.getY() <= del_Y + del_re.getHeight()
                    && event.getX() >= del_x
                    && event.getX() <= del_x + del_re.getWidth()) {
                del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
                sc_img1.startAnimation(mLitteAnimation);
                sc_img1.startAnimation(mBigAnimation);
            }
        } else {
            img1.setVisibility(View.VISIBLE);
            del_re.setVisibility(View.GONE);
            del_re.setBackgroundResource(0);
        }
        return true;
    }


    private void startRecord(String name) {
        File dir = new File(Constant.VOICE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // if (util == null) {
        // util = new AudioRecorder2Mp3Util(null, Constant.VOICE_PATH
        // + File.separator + name + ".raw", Constant.VOICE_PATH
        // + File.separator + name + ".aac");
        // }
        // if (canClean) {
        // util.cleanFile(AudioRecorder2Mp3Util.MP3
        // | AudioRecorder2Mp3Util.RAW);
        // }
        // // mSensor.start(name);
        // util.startRecording();
        // canClean = true;
        try {
            recorder.startRecording(Constant.VOICE_PATH + File.separator + name + ".mp3");
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(MainActivity.this, "操作失败，请重试");
        }
    }

    private void stopRecord() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        // util.stopRecordingAndConvertFile();
        // util.cleanFile(AudioRecorder2Mp3Util.RAW);
        // util.close();
        // util = null;
        try {
            recorder.stopRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // mSensor.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取图片路径
        if (requestCode == RESULT_CODE_FROM_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            ContentResolver cr = getContentResolver();
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                        && DocumentsContract.isDocumentUri(
                        getApplicationContext(), uri)) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equalsIgnoreCase(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    imagePath = getDataColumn(getApplicationContext(),
                            contentUri, selection, selectionArgs);
                } else {
                    // 针对一些特殊机型比如红米note系列的手机获取到的图片路径为null
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        if (null == cursor) {
                            Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cursor.moveToFirst();
                        imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    } else {
                        imagePath = uri.getPath();
                    }
                }
                Intent intent = new Intent(MainActivity.this, ScreenCaptureActivity.class);
                intent.putExtra("title", "分享");
                intent.putExtra("path", imagePath);
                startActivityForResult(intent, 14);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {//将查看相册图标设置成最近的截图
                        mIvAlbum.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CODE_FROM_UPDATE_HONEY_INFO:
                refreshMainScene();
                break;
            case RESULT_CODE_FROM_BIND_TERMINATION_SUCCESS:
                bind.dismiss();
                break;
            case RESULT_CODE_FROM_BIND_TERMINATION_FAIL:
                break;
            case RESULT_CODE_FROM_SLEEP_SETTING:
                mTvSleepDuration.setText(data.getStringExtra("sleep_duration"));
                break;
            case RESULT_CODE_FROM_CHIME_SETTING:
                mTvChimeDuration.setText(data.getStringExtra("chime_duration"));
                break;
            case RESULT_CODE_FROM_VACATION_SETTING:
                mTvSummerVacation.setText(data.getStringExtra("summer"));
                mTvWinterVacation.setText(data.getStringExtra("winter"));
                break;
            case RESULT_CODE_FROM_GETUP_ALARM:
            case RESULT_CODE_FROM_HOMEWORK_ALARM:
            case RESULT_CODE_FROM_BIRTHDAY_ALARM:
            case RESULT_CODE_FROM_DAILY_ALARM:
            case RESULT_CODE_FROM_EYEPROTECT_ALARM:
            case RESULT_CODE_FROM_ANNIVERSARY_ALARM:
            case RESULT_CODE_FROM_CUSTOM_ALARM:
            case RESULT_CODE_FROM_WATER_MEDICINE_ALARM:
            case RESULT_CODE_FROM_FESTIVAL_ALARM:
                new QueryAlarmClockListServer().start(config.getUserId(),
                        config.getAlarmCode(), queryAlarmListCallBack);
                break;
            case RESULT_CODE_FROM_UNLOCK_GESTURE:
                finish();
                break;
            case RESULT_CODE_FROM_BIND_TEMINAL:
                if (rightMenu.isMenuShowing()) {
                    rightMenu.toggle(true);
                }
                initHoneys();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogConfirm(String tag, boolean cancelled,
                                CharSequence message) {
        if (message.toString().equals("bind")) {
            Intent intent = new Intent(MainActivity.this,
                    MipcaActivityCapture.class);
            startActivityForResult(intent, 4);
        } else if (message.toString().equals("dismiss")) {
            return;
        } else if (tag.equals("delete_alarm")) {
            new SwitchClockStatusServer().start(config.getUserId(),
                    config.getAlarmCode(), message.toString(), 2,
                    switchAlarmStatusCallBack);
        }
    }

    @Override
    protected void onDestroy() {
        Logger.d("mmmm: onDestroy");
        MediaPlayerUtil.getInstance().stop();
        if (config.getLoginState() == 1) {
            unregisterHeartBeat();
            unregisterReceiver(receiver);
        }
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    /**
     * 注册心跳
     */
    private void registerHeartBeat() {
        NotifyManager.getInstance().register(NotifyConstant.HEART_HEAT,
                listener);
    }

    /**
     * 取消注册心跳
     */
    private void unregisterHeartBeat() {
        NotifyManager.getInstance().unregister(NotifyConstant.HEART_HEAT,
                listener);
    }

    private void updateDisplay(double amp) {

        switch ((int) amp) {
            case 30:
            case 31:
            case 32:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 33:
                volume.setImageResource(R.drawable.amp2);
                break;
            case 34:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 35:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 36:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 37:
            case 38:
            case 39:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    int count = 0;
    long firClick = 0;
    long secClick = 0;

    class DoubleClick implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                count++;
                if (count == 1) {
                    firClick = System.currentTimeMillis();

                } else if (count == 2) {
                    secClick = System.currentTimeMillis();
                    if (secClick - firClick < 1000) {
                        // 双击事件
                        count = 0;
                        firClick = 0;
                        secClick = 0;

                        click.onClick(mIvCapture);
                    } else {
                        count = 0;
                        firClick = 0;
                        secClick = 0;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public void onMediaScannerConnected() {
        conn.scanFile(scanPath, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            conn = null;
        }
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
