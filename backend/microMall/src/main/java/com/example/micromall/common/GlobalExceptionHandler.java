package com.example.micromall.common;

import com.example.micromall.common.api.ApiException;
import com.example.micromall.common.api.CommonResult;
import com.example.micromall.common.api.ResultCode;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public CommonResult<Void> handleApiException(ApiException e) {
        if (e.getResultCode() != null) {
            return CommonResult.failed(e.getResultCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public CommonResult<Void> handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e) {
        return CommonResult.failed(e.getMessage());
    }
} 