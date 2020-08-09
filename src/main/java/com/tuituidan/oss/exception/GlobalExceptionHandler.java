package com.tuituidan.oss.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/9
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 统一处理 ImageHostException 的方法.
     *
     * @param e 异常实例
     * @return 异常提示字符串
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ImageHostException.class)
    public String handleIbedException(ImageHostException e) {
        log.error("运行发生了异常，请检查!", e);
        return e.getMessage();
    }
}
