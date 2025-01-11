package com.example.micromall.vo;

import lombok.Data;

/**
 * 登录返回结果
 */
@Data
public class LoginVO {
    private Long id;
    private String username;
    private String token;
    private String avatar;
    private String phone;
    private Integer role;
} 