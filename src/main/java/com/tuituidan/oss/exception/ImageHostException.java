package com.tuituidan.oss.exception;

/**
 * ImageHostException.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/9
 */
public class ImageHostException extends RuntimeException {
    private static final long serialVersionUID = 8758096634440775116L;

    /**
     * 根据 Message 来构造异常实例.
     *
     * @param message 消息
     */
    public ImageHostException(String message) {
        super(message);
    }
}
