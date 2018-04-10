package com.babacit.alarm.profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.babacit.alarm.msg.BaseInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ProfileTool {
	private static ProfileTool instance;
	private Context mContext;
	private String versionName;
	private int versionCode;
	private long firstInstallTime;
	private String pkgName;
	private String IMSI;
	private String IMEI;
	private String brand;
	private String osVersionCode;
	private String model;
	private int screenWidth;
	private int screenHeight;
	private float density;
	private int densityDpi;
	private String channelCode;

	public static ProfileTool getInstance() {
		if (instance == null) {
			instance = new ProfileTool();
		}
		return instance;
	}

	private ProfileTool() {

	}

	public void init(Context context) {
		this.mContext = context;
		initPkgInfo();
		initSimInfo();
		initDeviceInfo();
	}

	/**
	 * 初始化应用相关的信息
	 * 
	 * @param context
	 * @return
	 */
	public void initPkgInfo() {
		try {
			String pkName = mContext.getPackageName();
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pInfo;
			pInfo = pm.getPackageInfo(pkName, 0);

			this.pkgName = pkName;
			this.versionName = pInfo.versionName;
			this.versionCode = pInfo.versionCode;
			if (android.os.Build.VERSION.SDK_INT > 9) {
				this.firstInstallTime = pInfo.firstInstallTime;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取通道编号
	 * 
	 * @return
	 */
	public String getChannelCode() {
		if (!TextUtils.isEmpty(channelCode)) {
			return channelCode;
		}
		if (mContext == null) {
			return null;
		}
		ApplicationInfo appInfo;
		try {
			appInfo = mContext.getPackageManager().getApplicationInfo(
					mContext.getPackageName(), PackageManager.GET_META_DATA);
			if (appInfo != null) {
				Bundle bundle = appInfo.metaData;
				if (bundle != null) {
					channelCode = String.valueOf(bundle.get("WX_CHANNEL_CODE"));
					if (channelCode != null) {
						channelCode = channelCode.toString().trim();
					}
				}
			}
			return channelCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取sim卡信息
	 * 
	 * @return
	 */
	public void initSimInfo() {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		IMSI = tm.getSubscriberId() != null ? tm.getSubscriberId() : "";
	}

	public void initDeviceInfo() {

		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = tm.getDeviceId();
		brand = android.os.Build.BRAND;
		model = android.os.Build.MODEL;
		osVersionCode = android.os.Build.VERSION.SDK_INT + "";
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		density = dm.density;
		densityDpi = dm.densityDpi;
	}

	public boolean isNetworkConnected() {
		if (mContext != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public String getVersionName() {
		return versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public long getFirstInstallTime() {
		return firstInstallTime;
	}

	public String getPkgName() {
		return pkgName;
	}

	public String getIMSI() {
		return IMSI;
	}

	public String getIMEI() {
		return IMEI;
	}

	public String getBrand() {
		return brand;
	}

	public String getOsVersionCode() {
		return osVersionCode;
	}

	public String getModel() {
		return model;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public float getDensity() {
		return density != 0 ? density : 2;
	}

	public int getDensityDpi() {
		return densityDpi != 0 ? densityDpi : 240;
	}

	public String getMobileRP() {
		if (screenWidth > screenHeight) {
			return screenWidth + "*" + screenHeight;
		} else {
			return screenHeight + "*" + screenWidth;
		}
	}

	public BaseInfo getBaseInfo() {
		BaseInfo baseInfo = new BaseInfo();
		if (mContext == null) {
			return baseInfo;
		}
		String pkName = mContext.getPackageName();

		PackageManager pm = mContext.getPackageManager();
		baseInfo.setAppPackageName(pkName);
		PackageInfo pInfo;
		try {
			pInfo = pm.getPackageInfo(pkName, 0);
			baseInfo.setAppVersionName(pInfo.versionName);
			baseInfo.setAppVersionCode(pInfo.versionCode);
			baseInfo.setAppInstallTime(pInfo.firstInstallTime);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		baseInfo.setDeviceCode(deviceId);
		baseInfo.setManufacture(android.os.Build.MANUFACTURER);
		baseInfo.setMobileModel(android.os.Build.MODEL);
		baseInfo.setOsVersionName(android.os.Build.VERSION.RELEASE);
		baseInfo.setOsVersionCode(android.os.Build.VERSION.SDK_INT);
		baseInfo.setPlatform(0);
		tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		baseInfo.setIMSI(tm.getSubscriberId());
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		baseInfo.setDataNetworkType(0);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				// 说明网络是通的 则根据不同类型 获取设置信息
				switch (info.getType()) {
				case ConnectivityManager.TYPE_WIFI: {
					baseInfo.setDataNetworkType(1);
					WifiManager wifiManager = (WifiManager) mContext
							.getSystemService(Context.WIFI_SERVICE);
					if (wifiManager != null) {
						WifiInfo wifi = wifiManager.getConnectionInfo();
						if (wifi != null)
							baseInfo.setWifiMacAddress(wifi.getMacAddress());
					}
				}
					break;
				case ConnectivityManager.TYPE_MOBILE: {
					baseInfo.setDataNetworkType(2);
				}
					break;
				}
			}
			String channelCode = getChannelCode();
			baseInfo.setChannelCode(channelCode);
		}
		return baseInfo;
	}

	/**
	 * whether packageName is system application
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean isSystemApplication(Context context, String packageName) {
		if (context == null) {
			return false;
		}

		return isSystemApplication(context.getPackageManager(), packageName);
	}

	/**
	 * whether packageName is system application
	 * 
	 * @param packageManager
	 * @param packageName
	 * @return <ul>
	 *         <li>if packageManager is null, return false</li>
	 *         <li>if package name is null or is empty, return false</li>
	 *         <li>if package name not exit, return false</li>
	 *         <li>if package name exit, but not system app, return false</li>
	 *         <li>else return true</li>
	 *         </ul>
	 */
	public boolean isSystemApplication(PackageManager packageManager,
			String packageName) {
		if (packageManager == null || packageName == null
				|| packageName.length() == 0) {
			return false;
		}

		try {
			ApplicationInfo app = packageManager.getApplicationInfo(
					packageName, 0);
			return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isExist(String packageName) {
		if (mContext == null) {
			return false;
		}
		try {
			if (mContext.getPackageManager().getPackageInfo(packageName, 0) != null) {
				return true;
			}

		} catch (NameNotFoundException e) {
		}
		return false;
	}

	public boolean isMyActivity() {
		if (mContext == null) {
			return false;
		}
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runingtaskinfos = am.getRunningTasks(1);
		// 代表的就是 最近一个被打开的任务
		RunningTaskInfo recenttask = runingtaskinfos.get(0);
		ComponentName topActivity = recenttask.topActivity; // 用户即将看到的activity的界面
		String packname = topActivity.getPackageName();
		if (mContext.getPackageName().equals(packname)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是横屏还是竖屏
	 * 
	 * @return 0：竖屏，1：横屏
	 */
	public int getScreenOrientation() {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		if (width > height) {
			return 1;
		}
		return 0;
	}

	/**
	 * 获取当前网络类型，-1：无网络，0，mobile，1：wifi
	 * 
	 * @return
	 */
	public int getNetType() {
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();

			if (info != null) {
				return info.getType();
			}
		}
		return -1;
	}

	public boolean isConnection() {
		return getNetType() != -1;
	}

	public String readTxtFromAsset(String filePath) {
		try {
			String encoding = "UTF-8";
			AssetManager assetManager = mContext.getAssets();
			InputStream is = assetManager.open(filePath);
			if (is != null) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(is, encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = "";
				String s;
				while ((s = bufferedReader.readLine()) != null) {
					lineTxt = lineTxt + s;
				}
				read.close();
				return lineTxt;
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return null;
	}
}
