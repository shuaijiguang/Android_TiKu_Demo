package tools;

import android.util.Log;

import com.example.shuaijiguang.android_tiku_demo.BuildConfig;


/**
 * 日志工具类
 */
public class LogHelper {

	public static void d(String tag, String msg) {

		if (BuildConfig.DEBUG) {

			Log.d(tag, msg);

		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg) {

		Log.e(tag, msg);

	}

	public static void e(String tag, String msg, Throwable tr) {

		Log.e(tag, msg, tr);

	}
}
