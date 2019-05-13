package com.bigbrother.fileservice.utils;

import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * @author Administrator
 */
@UtilityClass
public class SysUtil {

    /**
     * CHINESE_PATTERN
     */
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断字符串中是否包含中文，不能校验是否为中文标点符号
     *
     * @param str 待校验字符串
     * @return 是否为中文
     */
    public static boolean containChinese(String str) {
        return CHINESE_PATTERN.matcher(str).find();
    }
}
