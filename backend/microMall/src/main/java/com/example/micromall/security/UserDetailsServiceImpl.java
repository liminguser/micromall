package com.example.micromall.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.micromall.entity.User;
import com.example.micromall.security.UserDetailsImpl;
import com.example.micromall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
                
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        return new UserDetailsImpl(user);
    }
} 