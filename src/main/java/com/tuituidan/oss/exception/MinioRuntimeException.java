package com.tuituidan.oss.exception;

/**
 * MinioException.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/11
 */
public class MinioRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6271373259444097860L;

    /**
     * 根据 Message 来构造异常实例.
     *
     * @param message 消息
     * @param e       Throwable实例
     */
    public MinioRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
