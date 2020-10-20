package com.tuituidan.oss.util;

import com.tuituidan.oss.consts.Separator;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.helpers.MessageFormatter;

/**
 * 字符串工具类.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/9
 */
@UtilityClass
public class StringExtUtils {

    /**
     * 从base64字符串中获取文件扩展名.
     */
    private static final Pattern PATTERN = Pattern.compile("data:image/(.*?);base64");

    private static final int SEQ_MIN = 1000;
    private static final int SEQ_MAX = 9998;

    private static final AtomicInteger ID_SEQ = new AtomicInteger(SEQ_MIN);

    /**
     * 获取短ID，要求1毫秒内的并发不超过9000.
     *
     * @return String
     */
    public static String getId() {
        if (ID_SEQ.intValue() > SEQ_MAX) {
            ID_SEQ.set(SEQ_MIN);
        }
        return Long.toString(Long.parseLong(System.currentTimeMillis()
                + "" + ID_SEQ.getAndIncrement()), Character.MAX_RADIX);
    }
    
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

    /**
     * 获取base64数据.
     *
     * @param source String
     * @return Pair
     */
    public static Pair<String, String> getBase64Info(String source) {
        String[] datas = StringUtils.split(source, Separator.COMMA);
        Matcher matcher = PATTERN.matcher(datas[0]);
        if (matcher.find()) {
            return Pair.of(matcher.group(1), datas[1]);
        }
        return null;
    }

    /**
     * getObjectName.
     *
     * @param id  id
     * @param ext ext
     * @return String
     */
    public static String getObjectName(String id, String ext) {
        LocalDate now = LocalDate.now();
        return StringUtils.join(now.getYear(), Separator.SLASH, now.getMonthValue(),
                Separator.SLASH, now.getDayOfMonth(), Separator.SLASH, id,
                Separator.DOT, ext);
    }
}
