package com.ebupt.portal.canyon.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author chy
 * @date 2019-03-07 21:13
 */
@Slf4j
public class TimeUtil {

	private static ThreadLocal<DateFormat> dateFormat14
			= ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
	private static ThreadLocal<DateFormat> dateFormat8
			= ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

	/**
	 * 获取14位长度当前时间（线程安全）
	 *
	 * @return
	 *          当前时间
	 */
	public static String getCurrentTime14() {
		return dateFormat14.get().format(new Date());
	}

	/**
	 * 计算两个日期相差天数
	 *
	 * @param nowTime
	 *                  14位当前时间字符串
	 * @param prevTime
	 *                  14位之前时间字符串
	 * @return
	 *                  相差天数
	 */
	public static long getTimeDiff(String nowTime, String prevTime) {
		long diffDays = 0L;
		try {
			DateFormat format = dateFormat14.get();
			Date now = format.parse(nowTime);
			Date prev = format.parse(prevTime);
			diffDays = (now.getTime() - prev.getTime()) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			log.error("日期格式化失败: {}", e.toString());
		}
		return diffDays;
	}

	/**
	 * 获取指定时间之前的日期
	 *
	 * @param daysAgo
	 *                 时长
	 * @return
	 *                 指定时间之前的日期
	 */
	public static String getBeforeDays(int daysAgo) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 0 - daysAgo);
		Date time = calendar.getTime();
		return dateFormat8.get().format(time);
	}
}
