package com.tuituidan.oss.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Consts.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Consts {
    /**
     * 使用'/'来做分隔符，有些时候不用`File.separator`, 因为windows中用'\'时，在 Minio 中作为对象名会报错.
     */
    public static final String SEP = "/";

    /**
     * 英文点号.
     */
    public static final String DOT = ".";

    /**
     * 下划线.
     */
    public static final String UNDERLINE = "_";

    /**
     * 中文逗号.
     */
    public static final String COMMA_CN = "，";
}
