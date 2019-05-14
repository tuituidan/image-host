package com.bigbrother.fileservice.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SysConst {
    /**
     * 分隔符 - 斜杠
     */
    public static final String SEP_SLASH = "/";

    /**
     * 分隔符 - 点
     */
    public static final String SEP_DOT = ".";

    /**
     * 分隔符 - 逗号
     */
    public static final String SEP_COMMA = ",";

    /**
     * key - id
     */
    public static final String KEY_ID = "id";

    /**
     * key - filename
     */
    public static final String KEY_FILENAME = "filename";

    /**
     * key - tags
     */
    public static final String KEY_TAGS = "tags";
}
