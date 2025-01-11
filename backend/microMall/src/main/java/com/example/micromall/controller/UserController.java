package com.example.micromall.controller;

import com.example.micromall.common.api.CommonResult;
import com.example.micromall.dto.UserUpdateDTO;
import com.example.micromall.service.UserService;
import com.example.micromall.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public CommonResult<Void> updateUserInfo(@Validated @RequestBody UserUpdateDTO updateDTO) {
        userService.updateUserInfo(updateDTO);
        return CommonResult.success(null);
    }

    @PostMapping("/avatar")
    public CommonResult<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        return CommonResult.success(avatarUrl);
    }

    @PutMapping("/password")
    public CommonResult<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return CommonResult.success(null);
    }
} 