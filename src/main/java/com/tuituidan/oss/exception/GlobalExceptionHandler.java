package com.tuituidan.oss.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param ex 异常实例
     * @return 异常提示字符串
     */
    @ExceptionHandler(ImageHostException.class)
    public ResponseEntity<String> handleImageHostException(ImageHostException ex) {
        // 只有tip的直接返回给前端，不记录日志
        if (StringUtils.isBlank(ex.getMessage())) {
            // 这类错误都是用户操作不规范问题，属于badRequest
            return ResponseEntity.badRequest().body(ex.getTip());
        }
        // ex.getCause()：本来就包装了一层的，所以用ex.getCause()拿到原始的异常就行
        log.error(ex.getMessage(), ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StringUtils.isBlank(ex.getTip()) ? ex.getMessage() : ex.getTip());
    }

    /**
     * 捕获其他未知异常，避免报到前端.
     *
     * @param throwable throwable
     * @return String
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return "服务器内部出现问题！";
    }
}
