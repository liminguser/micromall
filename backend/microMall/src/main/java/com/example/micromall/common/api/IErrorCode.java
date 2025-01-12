package com.example.micromall.common.api;

/**
 * 封装API的错误码
 */
public interface IErrorCode {
    /**
     * 获取错误码
     */
    long getCode();

    /**
     * 获取错误信息
     */
    String getMessage();
} 