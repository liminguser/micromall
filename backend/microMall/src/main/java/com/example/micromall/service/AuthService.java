package com.example.micromall.service;

import com.example.micromall.dto.UserLoginDTO;
import com.example.micromall.dto.UserRegisterDTO;
import com.example.micromall.vo.LoginVO;

/**
 * 认证服务
 */
public interface AuthService {
    /**
     * 登录
     */
    LoginVO login(UserLoginDTO loginDTO);

    /**
     * 注册
     */
    void register(UserRegisterDTO registerDTO);

    /**
     * 登出
     */
    void logout();
} 