package com.tuituidan.oss.exception.base;

import com.tuituidan.oss.kit.StringKit;

import ch.qos.logback.classic.spi.EventArgUtil;

/**
 * AbstractException.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = -3449536764341400765L;

    /**
     * 非public只被ExceptionBuilder使用.
     *
     * @param message String
     * @param cause   Throwable
     */
    AbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 用于自定义异常.
     *
     * @param message String
     * @param args    Object...
     */
    public AbstractException(String message, Object... args) {
        super(StringKit.format(message, args), EventArgUtil.extractThrowable(args));
    }
}
