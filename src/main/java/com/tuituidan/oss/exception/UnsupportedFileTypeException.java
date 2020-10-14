package com.tuituidan.oss.exception;

import com.tuituidan.oss.exception.base.AbstractException;

/**
 * UnsupportedFileTypeException.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
public class UnsupportedFileTypeException extends AbstractException {
    private static final long serialVersionUID = 5997766988967009018L;

    /**
     * 用于自定义异常.
     *
     * @param message String
     * @param args    Object...
     */
    public UnsupportedFileTypeException(String message, Object... args) {
        super(message, args);
    }
}
