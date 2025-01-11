package com.example.micromall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.micromall.dto.UserUpdateDTO;
import com.example.micromall.entity.User;
import com.example.micromall.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {
    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser();

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserUpdateDTO updateDTO);

    /**
     * 修改密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 根据用户名获取用户信息
     */
    UserVO getUserByUsername(String username);

    /**
     * 上传头像
     * @param file 头像文件
     * @return 头像URL
     */
    String uploadAvatar(MultipartFile file);
} 