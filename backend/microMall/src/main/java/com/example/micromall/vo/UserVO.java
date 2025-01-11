package com.example.micromall.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户VO
 */
@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer gender;
    private LocalDateTime birthday;
    private Integer status;
    private String roles;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 