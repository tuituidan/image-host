package com.tuituidan.oss.exception;

import com.tuituidan.oss.exception.base.AbstractException;

/**
 * MinioException.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/11
 */
public class MinioRuntimeException extends AbstractException {
    private static final long serialVersionUID = -6271373259444097860L;

    /**
     * 用于自定义异常.
     *
     * @param message String
     * @param args    Object...
     */
    public MinioRuntimeException(String message, Object... args) {
        super(message, args);
    }
}
