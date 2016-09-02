/**
 *  ClassName: DateAndTimeHepler.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.shuaijiguang.android_tiku_demo.R;


/**
 * 日期和时间操作工具类
 * 
 * @author qjyong
 */
@SuppressLint("SimpleDateFormat")
public class DateAndTimeHepler {
	private DateAndTimeHepler() {
	}

	public static final String TIME_PATTERN = "HH:mm";
	public static final String DATE_TIME_FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FULL_PATTERN);

		return df.format(date);
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param ctx
	 * @param times
	 *            时间毫秒值
	 * @return
	 */
	public static String friendly_time(Context ctx, long times) {
		int ct = (int) ((System.currentTimeMillis() - times) / 1000);
		if (ct < 3600) {
			return ctx.getString(R.string.minutes_before,
					Integer.valueOf(Math.max(ct / 60, 1)));
		}
		if (ct >= 3600 && ct < 86400) {
			return ctx.getString(R.string.hours_before,
					Integer.valueOf(ct / 3600));
		}
		if (ct >= 86400 && ct < 172800) { // 86400
			String t = new SimpleDateFormat(TIME_PATTERN)
					.format(new Date(times));
			return ctx.getString(R.string.yesterday, t);
		}
		if (ct >= 172800 && ct < 259200) { // 86400 * 2
			String t = new SimpleDateFormat(TIME_PATTERN)
					.format(new Date(times));
			return ctx.getString(R.string.day_before_yesterday, t);
		}
		if (ct >= 259200 && ct < 2592000) { // 86400 * 30
			return ctx.getString(R.string.days_before,
					Integer.valueOf(ct / 86400));
		}
		if (ct >= 2592000 && ct < 31104000) { // 86400 * 30以上
			return ctx.getString(R.string.months_before,
					Integer.valueOf(ct / 2592000));
		}
		return ctx.getString(R.string.years_before,
				Integer.valueOf(ct / 31104000));
	}
}
