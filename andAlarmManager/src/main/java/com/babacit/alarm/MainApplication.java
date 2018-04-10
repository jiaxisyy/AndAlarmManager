package com.babacit.alarm;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.babacit.alarm.config.PropConfig;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.profile.ProfileTool;
import com.babacit.alarm.swvideo.WorkerThread;
import com.babacit.alarm.ui.view.LockPatternUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.utils.Log;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class MainApplication extends Application{

    public final static String DEFAULT_APPKEY = "23753850";
    private static final int READ_PHONE_STATE = 100100;

    {
        PlatformConfig.setWeixin("wx1d5e5f01c31eeb10", "e73cbbca2c7982935f763de0b2d5e59a");
        PlatformConfig.setQQZone("1104934061", "7bWmIZOpd5UtnGWy");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    private static MainApplication mInstance;
    private LockPatternUtils mLockPatternUtils;

    public static MainApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 开启debug日志
//		Config.DEBUG = true;

        mInstance = this;
        mLockPatternUtils = new LockPatternUtils(this);

        PropConfig.getInstance().init(this);
        Logger.initLogger(this);
        initImageLoader(this);
        initSocialSDK();


        FeedbackAPI.init(mInstance, DEFAULT_APPKEY);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setDebugMode(false);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("Device Token", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("Device Token", "失败了");
            }
        });
    }

    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    private void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY)
                .diskCacheSize(500 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 声网视频sdk
     */
    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    /**
     * 声网视频sdk
     */

    //友盟分享SDK
    private void initSocialSDK() {
        Log.LOG = false;
        UMShareConfig config = new UMShareConfig();
        config.isOpenShareEditActivity(true);
        UMShareAPI.get(MainApplication.this).setShareConfig(config);
    }


}
