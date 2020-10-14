package com.tuituidan.oss.exception.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
     * 统一处理 ExceptionBuilder 的方法.
     *
     * @param ex 异常实例
     * @return 异常提示字符串
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ExceptionBuilder.class)
    public String handleIbedException(ExceptionBuilder ex) {
        // 只有tip的直接返回给前端，不记录日志
        if (StringUtils.isNotBlank(ex.getMessage())) {
            // ex.getCause()：本来就包装了一层的，所以用ex.getCause()拿到原始的记录就行
            log.error(ex.getMessage(), ex.getCause());
        }
        return StringUtils.isBlank(ex.getTip()) ? ex.getMessage() : ex.getTip();
    }
}
