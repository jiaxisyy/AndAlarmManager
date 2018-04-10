package com.babacit.alarm.constant;

import android.os.Environment;

public class Constant {

    /**1表示记忆猫,0表示我的宝贝*/
    public static final int CAT_OR_BABY = 0;
    public static final String DB_NAME = "alarm_db";
    public static final int FETCH_VERIFICATION_CODE_COUNT_DOWN = 30;

    public static final int REQUEST_CODE_FROM_GETUP_ALARM = 0;
    public static final int REQUEST_CODE_FROM_HOMEWORK_ALARM = 1;
    public static final int REQUEST_CODE_FROM_BIRTHDAY_ALARM = 2;
    public static final int REQUEST_CODE_FROM_DAILY_ALARM = 3;
    public static final int REQUEST_CODE_FROM_EYE_PROTECT_ALARM = 4;
    public static final int REQUEST_CODE_FROM_ANNI_ALARM = 5;
    public static final int REQUEST_CODE_FROM_CUSTOM_ALARM = 6;
    public static final int REQUEST_CODE_FROM_WATER_MEDICINE_ALARM = 7;
    public static final int REQUEST_CODE_FROM_FESTIVAL_ALARM = 8;

    public static final String PREFERENCE_UUID = "pref_1";
    public static final String PREFERENCE_LOGIN_SATTE = "pref_2";
    public static final String PREFERENCE_PORTRAIT_PATH = "pref_3";
    public static final String PREFERENCE_USER_ID = "pref_4";
    public static final String PREFERENCE_ALARM_CODE = "pref_5";
    public static final String PREFERENCE_RING_LATEST_UPDATE_TIME = "pref_6";
    public static final String PREFERENCE_HONEY_PORTRAIT_PATH = "pref_7";
    public static final String PREFERENCE_GESTURE_STATUS = "pref_8";
    public static final String PREFERENCE_VIDEO_TIMES = "pref_9";
    public static final String PREFERENCE_INITIAL_USER_SETTING = "pref_10";
    public static final String PREFERENCE_USER_PHONE = "pref_11";
    public static final String PREFERENCE_USER_PWD = "pref_12";
    public static final String PREFERENCE_GUIDE_STATE = "pref_13";
    /**
     * 真实地址 开始
     */
//	 public static final String URL_PRE = "http://172.16.4.71/api/";
//	修改为2017/10/26 http://api.vooxitech.net:8080 为信科技

    public static final String BASE_URL = "http://api.vooxitech.net:8080/";
    public static final String URL_PRE = "http://api.vooxitech.net:8080/alarm/api/";
    //    public static final String BASE_URL = "http://babacit.net:8080/";
//    public static final String URL_PRE = "http://babacit.net:8080/alarm/api/";
    public static final String FETCH_CODE_URL = URL_PRE + "getVerifyCode";
    public static final String PWD_SETTING_URL = URL_PRE + "pwdSetting";
    public static final String LOGIN_URL = URL_PRE + "login";
    public static final String QUERY_USER_INFO_URL = URL_PRE + "queryUserInfo";
    public static final String UPDATE_USER_INFO_URL = URL_PRE
            + "updateUserInfo";
    public static final String UPDATE_PHONE_NO_URL = URL_PRE + "updatePhoneNo";
    public static final String APPLY_BIND_ALARM_URL = URL_PRE
            + "applyBindAlarm";
    public static final String CONFIRM_BIND_ALARM_URL = URL_PRE
            + "confirmBindAlarm";
    public static final String QUERY_HONEY_INFO_URL = URL_PRE
            + "queryHoneyInfos";
    public static final String UPDATE_HONEY_INFO_URL = URL_PRE
            + "updateHoneyInfo";
    public static final String QUERY_ALARM_LIST_URL = URL_PRE
            + "queryAlarmClockList";
    public static final String UPDATE_ALARM_CLOCK_URL = URL_PRE
            + "updateAlarmClock";
    public static final String CREATE_ALARM_CLOCK_URL = URL_PRE
            + "createAlarmClock";
    public static final String UPDATE_HISTORY_INFO_URL = URL_PRE
            + "updateHistoryInfo";
    public static final String QUERY_HISTORY_INFO_URL = URL_PRE
            + "queryClockHistoryList";
    public static final String APPLY_MONITOR_URL = URL_PRE + "applyMonitor";
    public static final String QUERY_ALARM_BASE_INFO_URL = URL_PRE
            + "queryAlarmBaseInfo";
    public static final String UPDATE_SLEEP_TIME_URL = URL_PRE
            + "updateSleepTime";
    public static final String UPDATE_ALARM_STATUS_URL = URL_PRE
            + "updateAlarmStatus";
    public static final String UPDATE_VACATION_URL = URL_PRE + "updateVacation";
    public static final String REWARD_SETTING_URL = URL_PRE + "rewardSetting";
    public static final String QUERY_COMPLETION_PIE_INFO_URL = URL_PRE
            + "queryCompletionPieInfo";
    public static final String QUERY_COMPLETION_BAR_INFO_URL = URL_PRE
            + "queryCompletionBarInfo";
    public static final String QUERY_HOLIDAY_URL = URL_PRE + "queryHoliday";
    public static final String QUERY_RING_URL = URL_PRE + "queryRing";
    public static final String UPLOAD_SHARED_INFO_URL = URL_PRE
            + "uploadSharedInfo";

    public static final String QUERY_REWARD_LIST_URL = URL_PRE
            + "queryRewardInfoList";
    public static final String SWITCH_CLOCK_STATUS_URL = URL_PRE
            + "updateAlarmClockStatus";
    public static final String QUERY_VOICE_MSG_URL = URL_PRE + "queryMessage";
    public static final String SEND_VOICE_MSG_URL = URL_PRE + "sendMessage";
    public static final String HEART_BEAT_URL = URL_PRE + "queryHeartBeat";
    public static final String UPLOAD_RING_URL = URL_PRE + "uploadRing";
    public static final String UPLOAD_FILE_URL = URL_PRE + "uploadFile";
    public static final String UPLOAD_MONITOR_STATUS_URL = URL_PRE
            + "uploadMonitorStatus";
    public static final String UPDATE_REPORT_TIME_URL = URL_PRE
            + "updateReportTime";
    /** 真实地址 结束 */

    /**
     * 测试地址 开始
     */
    // public static final String FETCH_CODE_URL = "common.json";
    // public static final String PWD_SETTING_URL = "common.json";
    // public static final String LOGIN_URL = "login.json";
    // public static final String QUERY_USER_INFO_URL = "user.json";
    // public static final String UPDATE_USER_INFO_URL = "common.json";
    // public static final String UPDATE_PHONE_NO_URL = "common.json";
    // public static final String APPLY_BIND_ALARM_URL = "common.json";
    // public static final String CONFIRM_BIND_ALARM_URL = "common.json";
    // public static final String QUERY_HONEY_INFO_URL = "honey.json";
    // public static final String UPDATE_HONEY_INFO_URL = "common.json";
    // public static final String QUERY_ALARM_LIST_URL = "alarm.json";
    // public static final String UPDATE_ALARM_CLOCK_URL = "common.json";
    // public static final String CREATE_ALARM_CLOCK_URL = "common.json";
    // public static final String QUERY_CLOCK_HISTORY_LIST_URL = "common.json";
    // public static final String QUERY_HISTORY_INFO_URL = "history.json";
    // public static final String UPDATE_HISTORY_INFO_URL = "common.json";
    // public static final String APPLY_MONITOR_URL = "common.json";
    // public static final String QUERY_ALARM_BASE_INFO_URL = "alarm_base.json";
    // public static final String UPDATE_SLEEP_TIME_URL = "common.json";
    // public static final String UPDATE_ALARM_STATUS_URL = "common.json";
    // public static final String UPDATE_VACATION_URL = "common.json";
    // public static final String REWARD_SETTING_URL = "common.json";
    // public static final String QUERY_COMPLETION_PIE_INFO_URL = "pie.json";
    // public static final String QUERY_COMPLETION_BAR_INFO_URL = "bar.json";
    // public static final String QUERY_HOLIDAY_URL = "holiday.json";
    // public static final String QUERY_RING_URL = "ring.json";
    // public static final String UPLOAD_SHARED_INFO_URL = "common.json";
    //
    // public static final String QUERY_REWARD_LIST_URL = "reward.json";
    // public static final String SWITCH_CLOCK_STATUS_URL = "common.json";
    // public static final String QUERY_VOICE_MSG_URL = "voice_mail.json";
    // public static final String SEND_VOICE_MSG_URL = "common.json";
    // public static final String HEART_BEAT_URL = "common.json";
    // public static final String UPLOAD_RING_URL = "common.json";
    // public static final String UPLOAD_FILE_URL = "common.json";
    // public static final String MONITOR_FINISH_URL = "common.json";
    // public static final String UPDATE_REPORT_TIME_URL = "common.json";
    // public static final String UPLOAD_MONITOR_STATUS_URL = "common.json";
    // /**测试地址结束*/

    public final static String ROOT_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/AlarmManager";
    public static final String VOICE_PATH = ROOT_PATH + "/voice";
    public static final String CAPTURE_PATH = ROOT_PATH + "/记忆猫";
    public static final String PORTRAIT_PATH = ROOT_PATH + "/portrait";
    public final static String LOG_PATH = ROOT_PATH + "/log";
    public final static String PROP_PATH = ROOT_PATH + "/prop";

    public static final String DLG_TITLE = "dlg_title";
    public static final String DLG_CONTENT = "dlg_content";
    public static final String DLG_CONFIRM_TEXT = "dlg_confirm_text";
    public static final String DLG_CANCEL_TEXT = "dlg_cancel_text";
    public static final String DLG_ONE_BUTTON = "dlg_one_button";
    public static final String DLG_NO_BUTTON = "dlg_no_button";
}
