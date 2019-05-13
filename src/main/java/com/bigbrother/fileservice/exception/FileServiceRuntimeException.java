package com.bigbrother.fileservice.exception;

/**
 * @author Administrator
 */
public class FileServiceRuntimeException extends RuntimeException {

    /**
     * FileServiceRuntimeException serialVersionUID.
     */
    private static final long serialVersionUID = 9182442917283846022L;

    /**
     * 根据 Message 来构造异常实例.
     *
     * @param message 消息
     */
    public FileServiceRuntimeException(String message) {
        super(message);
    }

    /**
     * 根据 Message 来构造异常实例.
     *
     * @param message 消息
     * @param e Throwable实例
     */
    public FileServiceRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
