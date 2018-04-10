package com.babacit.alarm.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.babacit.alarm.config.PropConfig;
import com.babacit.alarm.constant.Constant;

import android.content.Context;
import android.util.Log;

/**
 * The class <code>Logger</code>日志记录工具类
 * 
 * @author likai
 * @version 1.0
 */
public class Logger {
	private static String TAG = null;
	private static Logger logger = new Logger();

	public static final int VERBOSE = 1;
	/** 日志级别DEBUG,设置此值将打印DEBUG,INFO,WARN,ERROR的日志 */
	public static final int DEBUG = 2;
	/** 日志级别INFO,设置此值将打印INFO,WARN,ERROR的日志 */
	public static final int INFO = 3;
	/** 日志级别WARN,设置此值将打印WARN,ERROR的日志 */
	public static final int WARN = 4;
	/** 日志级别ERROR,设置此值将打印ERROR的日志 */
	public static final int ERROR = 5;
	/** 日志级别NONE,即不输出日志 */
	public static final int NONE = 6;
	/** 上下文 */
	private static Context context = null;
	/** 是否追加，true：追加，false：覆盖 */
	private static boolean append = false;
	/** 日志文件的存储位置，默认sdcard */
	private static PlaceMode placeMode = PlaceMode.MODE_SDCARD;
	/** 文件路径 */
	private static String path = null;
	/** 打印日志的文件 */
	private static File logFile = null;
	/** 每个文件的最大值，单位byte */
	private static long max = 0;
	/** 输出时间戳的格式 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
	/** 日志头时间戳开关 */
	private boolean timeSwitch = true;
	/** 日志文件的输出流 */
	private static FileOutputStream fos = null;
	/** 日志文件的writer */
	private static OutputStreamWriter writer = null;
	/** 日志级别VERBOSE,设置此值将打印全部日志 */
	/** 日志级别，默认全部打印 */
	private static int level = 1;
	public static boolean logCat = false;

	/**
	 * 
	 * The class <code>PlaceMode</code>文件的存储介质
	 * 
	 * @author likai
	 * @version 1.0
	 */
	public enum PlaceMode {
		/** sdcard卡上指定目录下 */
		MODE_SDCARD,
		/** data/data/包名/file下 */
		MODE_MEMORY
	}

	public Logger() {

	}

	/**
	 * 获取日志打印工具
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            文件的存储地址（包含文件名），如果在placeMode为MODE_MEMORY时,则此值只需要名称即可
	 * @param placeMode
	 *            文件存储位置
	 * @param append
	 *            是否追加
	 * @param max
	 *            一个文件的最大值，单位byte；当为0的时候表示无限制,
	 * @param logLevel
	 *            日志输出级别
	 * @return 实例
	 */
	public static void initLogger(Context context) {
		try {
			PropConfig.getInstance().init(context);
			boolean logCat = PropConfig.getInstance().getLogcat();
			String path = Constant.LOG_PATH + "/log_"
					+ context.getPackageName() + ".txt";
			logCat = true;
			if (!logCat) {
				return;
			}
			Logger.context = context;
			TAG = context.getPackageName();
			Logger.placeMode = PlaceMode.MODE_SDCARD;
			Logger.path = path;
			Logger.append = true;
			Logger.max = 2 * 1024 * 1024;
			Logger.level = Logger.VERBOSE;
			Logger.logCat = logCat;
			try {
				init();
			} catch (IOException e) {
				throw e;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 初始化文件
	 */
	private static void init() throws IOException {
		if (Logger.placeMode != PlaceMode.MODE_MEMORY) {
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				logFile = new File(Logger.path);
				if (!logFile.exists()) {
					if (logFile.getParentFile().exists()) {
						logFile.getParentFile().delete();
					}
					logFile.getParentFile().mkdirs();
					logFile.createNewFile();
					fos = new FileOutputStream(logFile, Logger.append);
					writer = new OutputStreamWriter(fos, "UTF-8");
				} else {
					judgeSize();
				}
			} else {
				Log.v(TAG, "sdcard is unavailable");
			}
		} else {
			logFile = new File(context.getFilesDir() + File.separator
					+ Logger.path);
			if (!logFile.exists()) {
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
				fos = new FileOutputStream(logFile, Logger.append);
				writer = new OutputStreamWriter(fos, "UTF-8");
			} else {
				judgeSize();
			}
		}
	}

	/**
	 * 判断文件大小是否大于阀值，如超过法制，则将其重命名，并重新创建该文件
	 */
	private static void judgeSize() throws IOException {
		if (max != 0 && logFile.length() > Logger.max) {
			int index = 0;
			File back = null;
			// while (true) {
			back = new File(logFile.getAbsolutePath() + "." + index);
			if (!back.exists()) {
				logFile.renameTo(back);
				logFile.delete();
				logFile.createNewFile();
			} else {
				back.delete();
				logFile.renameTo(back);
				logFile.delete();
				logFile.createNewFile();
			}
			// index++;
			// }
			// if (index > 0) {
			// File deleteFile = new File(logFile.getAbsolutePath() + "." + 0);
			// if (deleteFile.exists())
			// deleteFile.delete();
			// }
		}
		fos = new FileOutputStream(logFile, Logger.append);
		writer = new OutputStreamWriter(fos, "UTF-8");
	}

	public static void v(String msg) {
		if (logger != null)
			logger.v(msg, Logger.logCat, Logger.logCat);
	}

	/**
	 * 打印verbos日志
	 * 
	 * @param msg
	 *            具体的内容
	 * @param logcat
	 *            true：在logcat中也输出，false，在logcat中不输出
	 * @param fileLog
	 *            是否在文件中输出
	 */
	public synchronized void v(String msg, boolean logcat, boolean fileLog) {
		if (writer == null) {
			return;
		}
		if (msg != null && Logger.level <= Logger.VERBOSE) {
			StringBuilder builder = new StringBuilder();
			if (timeSwitch)
				builder.append(dateFormat.format(new Date()) + " ");
			builder.append("[V]:");
			builder.append(msg);
			if (logcat)
				Log.v(TAG, builder.toString());
			if (fileLog)
				write(builder);
		}
	}

	public static void d(String msg) {
		if (logger != null)
			logger.d(msg, Logger.logCat, Logger.logCat);
	}

	/**
	 * 打印debug日志
	 * 
	 * @param msg
	 *            具体的内容
	 * @param logcat
	 *            true：在logcat中也输出，false，在logcat中不输出
	 * @param fileLog
	 *            是否在文件中输出
	 */
	public synchronized void d(String msg, boolean logcat, boolean fileLog) {
		if (writer == null) {
			return;
		}
		if (msg != null && Logger.level <= Logger.DEBUG) {
			StringBuilder builder = new StringBuilder();
			if (timeSwitch)
				builder.append(dateFormat.format(new Date()) + " ");
			builder.append("[D]:");
			builder.append(msg);
			if (logcat)
				Log.d(TAG, builder.toString());
			if (fileLog)
				write(builder);
		}
	}

	public static void i(String msg) {
		if (logger != null)
			logger.i(msg, Logger.logCat, Logger.logCat);
	}

	/**
	 * 打印info日志
	 * 
	 * @param msg
	 *            具体的内容
	 * @param logcat
	 *            true：在logcat中也输出，false，在logcat中不输出
	 * @param fileLog
	 *            是否在文件中输出
	 */
	public synchronized void i(String msg, boolean logcat, boolean fileLog) {
		if (writer == null) {
			return;
		}
		if (msg != null && Logger.level <= Logger.INFO) {
			StringBuilder builder = new StringBuilder();
			if (timeSwitch)
				builder.append(dateFormat.format(new Date()) + " ");
			builder.append("[I]:");
			builder.append(msg);
			if (logcat)
				Log.i(TAG, builder.toString());
			if (fileLog)
				write(builder);
		}
	}

	public static void w(String msg) {
		if (logger != null)
			logger.w(msg, Logger.logCat, Logger.logCat);
	}

	/**
	 * 打印warn日志
	 * 
	 * @param msg
	 *            具体的内容
	 * @param logcat
	 *            true：在logcat中也输出，false，在logcat中不输出
	 * @param fileLog
	 *            是否在文件中输出
	 */
	public synchronized void w(String msg, boolean logcat, boolean fileLog) {
		if (writer == null) {
			return;
		}
		if (msg != null && Logger.level <= Logger.WARN) {
			StringBuilder builder = new StringBuilder();
			if (timeSwitch)
				builder.append(dateFormat.format(new Date()) + " ");
			builder.append("[W]:");
			builder.append(msg);
			if (logcat)
				Log.w(TAG, builder.toString());
			if (fileLog)
				write(builder);
		}
	}

	public static void e(String msg) {
		if (logger != null)
			logger.e(msg, Logger.logCat, Logger.logCat);
	}

	/**
	 * 打印error日志
	 * 
	 * @param msg
	 *            具体的内容
	 * @param logcat
	 *            true：在logcat中也输出，false，在logcat中不输出
	 * @param fileLog
	 *            是否在文件中输出
	 */
	public synchronized void e(String msg, boolean logcat, boolean fileLog) {
		if (writer == null) {
			return;
		}
		if (msg != null && Logger.level <= Logger.ERROR) {
			StringBuilder builder = new StringBuilder();
			if (timeSwitch)
				builder.append(dateFormat.format(new Date()) + " ");
			builder.append("[E]:");
			builder.append(msg);
			if (logcat)
				Log.e(TAG, builder.toString());
			if (fileLog)
				write(builder);
		}
	}

	/**
	 * 写日志到文件中
	 * 
	 * @param builder
	 *            日志内容
	 */
	private synchronized void write(StringBuilder builder) {
		if (writer != null) {
			try {
				judgeSize();
			} catch (IOException e) {
				Log.e(TAG,
						"judge file size error, errmessage is "
								+ e.getMessage());
			}
			try {
				writer.write(builder.toString() + "\n");
				writer.flush();
			} catch (IOException e) {
				Log.e(TAG, "Log msg error,errmessage is " + e.getMessage());
			}
		}
	}

	/**
	 * 关闭日志文件
	 */
	public void close() {
		try {
			if (writer != null)
				writer.close();
			if (fos != null)
				fos.close();
		} catch (IOException e) {
			Log.e(TAG, "close logger error, errmessage is " + e.getMessage());
		}
	}

	/**
	 * Setter of level
	 * 
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		Logger.level = level;
	}
}
