package com.babacit.alarm.swvideo;

import java.io.File;
import java.util.Locale;

import com.babacit.alarm.R;
import com.babacit.alarm.logger.Logger;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

public class WorkerThread extends Thread {

    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;

    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;

    private static final int ACTION_WORKER_CONFIG_ENGINE = 0X2012;

//    private static final int ACTION_WORKER_PREVIEW = 0X2014;

    private static final class WorkerThreadHandler extends Handler {

        private WorkerThread mWorkerThread;

        WorkerThreadHandler(WorkerThread thread) {
            this.mWorkerThread = thread;
        }

        public void release() {
            mWorkerThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.mWorkerThread == null) {
                Logger.w("handler is already released! " + msg.what);
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mWorkerThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mWorkerThread.joinChannel(data[0], msg.arg1);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mWorkerThread.leaveChannel(channel);
                    break;
                case ACTION_WORKER_CONFIG_ENGINE:
                    Object[] configData = (Object[]) msg.obj;
                    mWorkerThread.configEngine((Integer) configData[0], (Integer) configData[1]);
                    break;
//                case ACTION_WORKER_PREVIEW:
//                    Object[] previewData = (Object[]) msg.obj;
//                    mWorkerThread.preview((boolean) previewData[0], (SurfaceView) previewData[1], (int) previewData[2]);
//                    break;
            }
        }
    }

    private WorkerThreadHandler mWorkerHandler;

    private boolean mReady;

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.d("wait for " + WorkerThread.class.getSimpleName());
        }
    }

    @Override
    public void run() {
    	Logger.d("start to run");
        Looper.prepare();

        mWorkerHandler = new WorkerThreadHandler(this);

        ensureRtcEngineReadyLock();

        mReady = true;

        // enter thread looper
        Looper.loop();
    }

    private RtcEngine mRtcEngine;

//    private AgoraYuvEnhancer mVideoEnhancer = null;

    public final void enablePreProcessor() {
//        if (mEngineConfig.mClientRole == Constants.CLIENT_ROLE_BROADCASTER) {
//            if (Constant.PRP_ENABLED) {
//                if (mVideoEnhancer == null) {
//                    mVideoEnhancer = new AgoraYuvEnhancer(mContext);
//                    mVideoEnhancer.SetLighteningFactor(Constant.PRP_DEFAULT_LIGHTNESS);
//                    mVideoEnhancer.SetSmoothnessFactor(Constant.PRP_DEFAULT_SMOOTHNESS);
//                    mVideoEnhancer.StartPreProcess();
//                }
//            }
//        }
    }

    public final void setPreParameters(float lightness, float smoothness) {
//        if (mEngineConfig.mClientRole == Constants.CLIENT_ROLE_BROADCASTER) {
//            if (Constant.PRP_ENABLED) {
//                if (mVideoEnhancer == null) {
//                    mVideoEnhancer = new AgoraYuvEnhancer(mContext);
//                }
//                mVideoEnhancer.StartPreProcess();
//            }
//        }

        Constant.PRP_DEFAULT_LIGHTNESS = lightness;
        Constant.PRP_DEFAULT_SMOOTHNESS = smoothness;

//        if (mVideoEnhancer != null) {
//            mVideoEnhancer.SetLighteningFactor(Constant.PRP_DEFAULT_LIGHTNESS);
//            mVideoEnhancer.SetSmoothnessFactor(Constant.PRP_DEFAULT_SMOOTHNESS);
//        }
    }

    public final void disablePreProcessor() {
//        if (mVideoEnhancer != null) {
//            mVideoEnhancer.StopPreProcess();
//            mVideoEnhancer = null;
//        }
    }

    public final void joinChannel(final String channel, int uid) {
        if (Thread.currentThread() != this) {
            Logger.w("joinChannel() - worker thread asynchronously " + channel + " " + uid);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_JOIN_CHANNEL;
            envelop.obj = new String[]{channel};
            envelop.arg1 = uid;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mRtcEngine.joinChannel(null, channel, "OpenLive", uid);

        mEngineConfig.mChannel = channel;

        enablePreProcessor();
        Logger.d("joinChannel " + channel + " " + uid);
    }

    public final void leaveChannel(String channel) {
        if (Thread.currentThread() != this) {
            Logger.w("leaveChannel() - worker thread asynchronously " + channel);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_LEAVE_CHANNEL;
            envelop.obj = channel;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }

        disablePreProcessor();

        int clientRole = mEngineConfig.mClientRole;
        mEngineConfig.reset();
        Logger.d("leaveChannel " + channel + " " + clientRole);
    }

    private EngineConfig mEngineConfig;

    public final EngineConfig getEngineConfig() {
        return mEngineConfig;
    }

    private final MyEngineEventHandler mEngineEventHandler;

    public final void configEngine(int cRole, int vProfile) {
        if (Thread.currentThread() != this) {
            Logger.w("configEngine() - worker thread asynchronously " + cRole + " " + vProfile);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_CONFIG_ENGINE;
            envelop.obj = new Object[]{cRole, vProfile};
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mEngineConfig.mClientRole = cRole;
        mEngineConfig.mVideoProfile = vProfile;

        mRtcEngine.setVideoProfile(mEngineConfig.mVideoProfile, false);

//        mRtcEngine.setClientRole(cRole, "");

        Logger.d("configEngine " + cRole + " " + mEngineConfig.mVideoProfile);
    }

//    public final void preview(boolean start, SurfaceView view, int uid) {
//        if (Thread.currentThread() != this) {
//            Logger.w("preview() - worker thread asynchronously " + start + " " + view + " " + (uid & 0XFFFFFFFFL));
//            Message envelop = new Message();
//            envelop.what = ACTION_WORKER_PREVIEW;
//            envelop.obj = new Object[]{start, view, uid};
//            mWorkerHandler.sendMessage(envelop);
//            return;
//        }
//
//        ensureRtcEngineReadyLock();
//        if (start) {
//            mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
//            mRtcEngine.startPreview();
//        } else {
//            mRtcEngine.stopPreview();
//        }
//    }

    public static String getDeviceID(Context context) {
        // XXX according to the API docs, this value may change after factory reset
        // use Android id as device id
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private RtcEngine ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            String appId = mContext.getString(R.string.private_app_id);
            if (TextUtils.isEmpty(appId)) {
                throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
            }
            try {
                mRtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
                mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
                mRtcEngine.enableVideo();
                mRtcEngine.setParameters(String.format(Locale.US, "{\"rtc.log_file\":\"%s\"}", Environment.getExternalStorageDirectory()
                        + File.separator + mContext.getPackageName() + "/log/agora-rtc.log"));
            } catch (Exception e) {
                e.printStackTrace();
            }


//            mRtcEngine.enableDualStreamMode(true);
        }
        return mRtcEngine;
    }

    public MyEngineEventHandler eventHandler() {
        return mEngineEventHandler;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    public final void exit() {
        if (Thread.currentThread() != this) {
            Logger.w("exit() - exit app thread asynchronously");
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }

        mReady = false;

        // TODO should remove all pending(read) messages

//        mVideoEnhancer = null;

        Logger.d("exit() > start");

        // exit thread looper
        Looper.myLooper().quit();

        mWorkerHandler.release();

        Logger.d("exit() > end");
    }

    public WorkerThread(Context context) {
        this.mContext = context;

        this.mEngineConfig = new EngineConfig();
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        this.mEngineConfig.mUid = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_UID, 0);

        this.mEngineEventHandler = new MyEngineEventHandler(mContext, this.mEngineConfig);
    }
}
