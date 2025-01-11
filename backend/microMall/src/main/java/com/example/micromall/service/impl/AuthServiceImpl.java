package com.example.micromall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.micromall.common.api.ApiException;
import com.example.micromall.common.api.ResultCode;
import com.example.micromall.dto.UserLoginDTO;
import com.example.micromall.dto.UserRegisterDTO;
import com.example.micromall.entity.User;
import com.example.micromall.mapper.UserMapper;
import com.example.micromall.security.UserDetailsImpl;
import com.example.micromall.service.AuthService;
import com.example.micromall.util.JwtTokenUtil;
import com.example.micromall.vo.LoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
                
        if (user == null) {
            throw new ApiException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new ApiException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 创建UserDetails对象
        UserDetails userDetails = new UserDetailsImpl(user);
        
        // 生成token
        String token = jwtTokenUtil.generateToken(userDetails);

        // 转换为VO
        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);

        return loginVO;
    }

    @Override
    public void register(UserRegisterDTO registerDTO) {
        // 校验用户是否同意协议
        if (!"true".equals(registerDTO.getAgreement())) {
            throw new ApiException("请阅读并同意用户协议和隐私政策");
        }

        // 校验两次密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new ApiException("两次密码不一致");
        }

        // 校验用户名是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername())) > 0) {
            throw new ApiException(ResultCode.DUPLICATE_USERNAME);
        }

        // 校验手机号是否已存在
        if (registerDTO.getPhone() != null && userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, registerDTO.getPhone())) > 0) {
            throw new ApiException(ResultCode.PHONE_NUMBER_EXIST);
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        // 如果昵称为空，使用用户名作为默认昵称
        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setStatus(1); // 1: 正常状态
        user.setRoles("ROLE_USER"); // 设置默认角色
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public void logout() {
        // TODO: 实现token失效
    }
} 