package com.example.micromall.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录返回结果
 */
@Data
public class LoginVO {
    private Long id;
    private String username;
    private String token;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private Integer gender;
    private LocalDateTime birthday;
    private Integer role;
    private String roles;
} 