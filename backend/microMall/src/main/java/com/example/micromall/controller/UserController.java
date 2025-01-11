package com.example.micromall.controller;

import com.example.micromall.common.api.CommonResult;
import com.example.micromall.dto.UserRegisterDTO;
import com.example.micromall.service.UserService;
import com.example.micromall.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public CommonResult<UserVO> getUserInfo() {
        UserVO userVO = userService.getCurrentUser();
        return CommonResult.success(userVO);
    }

    @PutMapping("/info")
    public CommonResult<Void> updateUserInfo(@Validated @RequestBody UserRegisterDTO registerDTO) {
        userService.updateUserInfo(registerDTO);
        return CommonResult.success(null);
    }

    @PutMapping("/password")
    public CommonResult<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return CommonResult.success(null);
    }
} 