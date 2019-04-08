package com.ebupt.portal.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TimeUtil {

    private static final DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取当前时间
     *
     * @return
     *              14位字符串
     */
    public static String getCurrentTime14() {
        return format1.format(new Date());
    }

    public static long getTimeDiff14(String startTime, String endTime) {
        long timeDiff;
        try {
            Date start = format1.parse(startTime);
            Date end = format1.parse(endTime);
            timeDiff = end.getTime() - start.getTime();
        } catch (ParseException e) {
            timeDiff = 0;
            log.warn("计算两个日期差值失败,按默认值0进行处理:{}", e.getMessage());
        }
        return timeDiff;
    }

}
