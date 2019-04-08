package com.ebupt.portal.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 获取占位符之间的字符串
     *
     * @param str
     *                  源字符串
     * @param start
     *                  占位符开始标识
     * @param end
     *                  占位符结束标识
     * @return
     *                  占位符
     */
    public static List<String> getPlaceholder(String str, String start, String end) {
        String regex =  start + "(.*?)" + end;
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find())
            list.add(matcher.group(1));

        return list;
    }

    /**
     * 判断字符串是否为空。
     *
     * @param str
     *              源字符串
     * @return
     *              true or false
     */
    public static boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     *              源字符串
     * @return
     *              true or false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
