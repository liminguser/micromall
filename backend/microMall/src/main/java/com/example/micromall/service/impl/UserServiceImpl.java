package com.example.micromall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.micromall.common.api.ApiException;
import com.example.micromall.common.api.ResultCode;
import com.example.micromall.dto.UserUpdateDTO;
import com.example.micromall.entity.User;
import com.example.micromall.mapper.UserMapper;
import com.example.micromall.security.UserDetailsImpl;
import com.example.micromall.service.UserService;
import com.example.micromall.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserVO getCurrentUser() {
        // 1. 获取当前用户ID
        Long userId = getCurrentUserId();

        // 2. 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new ApiException(ResultCode.USER_NOT_EXIST);
        }

        // 3. 转换为VO对象
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserUpdateDTO updateDTO) {
        // 1. 获取当前用户ID
        Long userId = getCurrentUserId();

        // 2. 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new ApiException(ResultCode.USER_NOT_EXIST);
        }

        // 3. 检查手机号是否被占用
        if (updateDTO.getPhone() != null && !updateDTO.getPhone().equals(user.getPhone())) {
            if (count(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, updateDTO.getPhone())) > 0) {
                throw new ApiException(ResultCode.PHONE_NUMBER_EXIST);
            }
        }

        // 4. 更新用户信息
        BeanUtils.copyProperties(updateDTO, user);
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String oldPassword, String newPassword) {
        // 1. 获取当前用户ID
        Long userId = getCurrentUserId();

        // 2. 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new ApiException(ResultCode.USER_NOT_EXIST);
        }

        // 3. 校验旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ApiException(ResultCode.OLD_PASSWORD_ERROR);
        }

        // 4. 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        // 1. 获取用户信息
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new ApiException(ResultCode.USER_NOT_EXIST);
        }

        // 2. 转换为VO对象
        return convertToVO(user);
    }

    /**
     * 转换为VO对象
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(ResultCode.UNAUTHORIZED);
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getUser().getId();
        }
        
        throw new ApiException(ResultCode.UNAUTHORIZED);
    }
} 