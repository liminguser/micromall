package com.example.micromall.common.api;

/**
 * 常用API操作码
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 用户相关错误码
    USER_NOT_EXIST(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    DUPLICATE_USERNAME(1003, "用户名已存在"),
    PHONE_NUMBER_EXIST(1004, "手机号已存在"),
    OLD_PASSWORD_ERROR(1005, "原密码错误"),
    USER_DISABLED(1006, "用户已被禁用");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 