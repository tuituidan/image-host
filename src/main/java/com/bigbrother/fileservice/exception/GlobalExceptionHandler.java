package com.bigbrother.fileservice.exception;

import com.bigbrother.fileservice.consts.SysConst;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Administrator
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数验证失败返回异常
     *
     * @param ex MethodArgumentNotValidException
     * @return String
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(SysConst.SEP_COMMA));
    }
}
