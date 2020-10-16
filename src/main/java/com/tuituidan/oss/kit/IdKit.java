package com.tuituidan.oss.kit;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.experimental.UtilityClass;

/**
 * IdKit.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/15
 */
@UtilityClass
public class IdKit {

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
}
