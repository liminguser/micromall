package com.example.micromall.controller;

import com.example.micromall.common.api.CommonResult;
import com.example.micromall.dto.UserLoginDTO;
import com.example.micromall.dto.UserRegisterDTO;
import com.example.micromall.service.AuthService;
import com.example.micromall.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public CommonResult<LoginVO> login(@Validated @RequestBody UserLoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return CommonResult.success(loginVO);
    }

    @PostMapping("/register")
    public CommonResult<Void> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        authService.register(registerDTO);
        return CommonResult.success(null);
    }

    @PostMapping("/logout")
    public CommonResult<Void> logout() {
        authService.logout();
        return CommonResult.success(null);
    }
} 