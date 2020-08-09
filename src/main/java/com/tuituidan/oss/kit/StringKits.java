package com.tuituidan.oss.kit;

import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

/**
 * StringKits.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/9
 */
@UtilityClass
public class StringKits {

    /**
     * 使用 Slf4j 中的字符串格式化方式来格式化字符串.
     *
     * @param pattern 待格式化的字符串
     * @param args    参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... args) {
        return pattern == null ? "" : MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

}
