package com.lancoo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

/**
 *TODO 时间工具类
 *--------------------
 * 2017年8月25日下午5:00:49
 */
public class TimeUtil {
	public static final String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

	/**
	 * @return 返回当前年
	 */
	public static int CurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @return 返回当前月
	 */
	public static int CurrentMontn() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * @return 返回当前几号
	 */
	public static int CurrentDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return 返回这个月的最小那天是几号
	 */
	public static int CurrentMonthMinDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}


	/**
	 * @return 返回这个月的最大那天是几号
	 */
	public static int CurrentMonthMaxDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return 返回当前月份的所有天号数
	 */
	public static List<Integer> CurrentMonthDays() {
		int min = CurrentMonthMinDay();
		int max = CurrentMonthMaxDay();

		List<Integer> days = new ArrayList<>();

		for (int i = min; i <= max; i++) {
			days.add(i);
		}
		return days;
	}


	/**
	 * @return 返回这个月剩余天数，可能返回0
	 */
	public static int getThisMonthLeftDays() {
		int currentdayinmonth = CurrentDay();
		int maxday = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		int left = maxday - currentdayinmonth;
		if (left <= 0) {
			left = 0;
		}
		return left;
	}

	/**
	 * @return 获取今天星期几
	 */
	public static String getDayInweek() {
		Calendar calendar = Calendar.getInstance();
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}


	/**
	 * 获取当前系统时间，格式为yyyyMMdd HH:mm:ss
	 */
	public static String getCurrentDateString() {
		return getCurrentDateString("yyyyMMdd HH:mm:ss");
	}

	/**
	 * @param form 指定时间格式
	 * @return
	 */
	public static String getCurrentDateString(String form) {
		return getTimebyDate(getCurtentDate(), form);
	}

	/**
	 * @param date
	 * @param form
	 * @return 指定date与时间格式获取时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimebyDate(Date date, String form) {
		SimpleDateFormat simpleFormatter = new SimpleDateFormat(form);
		return simpleFormatter.format(date);
	}

	/**
	 * @param Time 当前格式的时间字符串
	 * @param form 转为为该格式
	 * @return 将某个格式时间转化为另外格式的时间
	 */
	public static String getTimeByString(String Time, String form) {

		SimpleDateFormat sdf = new SimpleDateFormat(form);

		try {
			return getTimebyDate(sdf.parse(Time), form);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return Time;
	}

	/**
	 * @param Time
	 * @param fromform 当前时间格式
	 * @param toform 要转为的时间格式
	 * @return 将某个格式时间转化为另外格式的时间
	 */
	public static String getTimeByString(String Time, String fromform, String toform) {
		SimpleDateFormat sdf = new SimpleDateFormat(fromform);

		try {
			return getTimebyDate(sdf.parse(Time), toform);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return Time;
	}

	public static String getTimeFormTimeMillis(long TimeMillis, String form) {
		return new SimpleDateFormat(form).format(new Date(TimeMillis));
	}

	/**
	 * @return 获取当前时间date
	 */
	public static Date getCurtentDate() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取距离当前时间多少天的日期
	 * @param add 距离的时间天数,正数就增加，负数就减少
	 * @return
	 */
	public static String getTimefarFromCurrentdateByDay(int add) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, add);
		return getTimebyDate(calendar.getTime(), "yyyyMMdd");

	}

	/**
	 * 获取距离当前时间多少月的日期
	 * @param add 距离的时间月数,正数就增加，负数就减少
	 * @return
	 */
	public static String getTimefarFromCurrentdateByMonth(int add) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, add);
		return getTimebyDate(calendar.getTime(), "yyyyMMdd");
	}

	/**
	 *@param Duration
	 *@return
	 *--------------------
	 *TODO 秒时间转为有单位时间，单位是中文单位
	 *--------------------
	 * author: huangwei
	 */
	public static String GetTimeFromSecondWithChinese(int Duration) {
		if (Duration < 0) {
			return "0秒";
		}

		if (Duration < 60) {
			return Duration + "秒";
		}

		if (Duration < 3600) {
			return Duration / 60 + "分钟";
		}

		return FormatUtil.getFloat_KeepOneDecimalplaces(((float) Duration) / 3600) + "小时";
	}

	/**
	 *@param Duration
	 *@return
	 *--------------------
	 *TODO 秒时间转为有单位时间，单位是英文单位
	 *--------------------
	 * author: huangwei
	 * 2017年2月16日上午10:39:22
	 */
	public static String GetTimeFromSecondWithEnglish(int Duration) {
		if (Duration < 0) {
			return "0s";
		}
		if (Duration < 60) {
			return Duration + "s";
		}

		if (Duration < 3600) {
			return Duration / 60 + "min";
		}
		return FormatUtil.getFloat_KeepOneDecimalplaces(((float) Duration) / 3600) + "h";
	}

}
