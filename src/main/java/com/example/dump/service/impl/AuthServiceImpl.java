package com.example.dump.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dump.component.JwtTokenUtil;
import com.example.dump.entity.*;
import com.example.dump.mapper.UserMapper;
import com.example.dump.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@DS("login")
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", username);
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User register(User userParam) {
        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        user.setCreateTime(LocalDateTime.now());
        //查询是否有相同用户名的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", user.getName());
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userMapper.insert(user);
        return user;
    }

    @Override
    public Boolean isValidPassword(String password) {
        // 检查密码长度是否至少为6位
        if (password.length() < 6) {
            return false;
        }

        // 使用正则表达式检查密码是否包含至少一个字母、一个数字和一个符号
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }

    @Override
    public LoginResult login(String username, String password) {
        LoginResult res = new LoginResult();
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("用户信息："+userDetails);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(userDetails);
            res.setToken(token);
            res.setIsValidPassword(isValidPassword(password));
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public LoginResult pswFreeLogin(String username){
        LoginResult res = new LoginResult();
        try{
            System.out.println("开始查信息");
            //检查数据库中是否存在该用户
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("用户信息："+userDetails);
            if(userDetails != null){
                //存在该用户则创建认证令牌并自动登录用户
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtTokenUtil.generateToken(userDetails);
                res.setToken(token);
                res.setIsValidPassword(true);
                System.out.println("是否验证："+res.getIsValidPassword());
            }else{
                //用户不在数据库中
                throw new BadCredentialsException("您无权访问该应用！");
            }
        }catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }

        return res;
    }

    @Override
    public Boolean changePassword(String name, String password) {
        User user = getUserByName(name);
        if (isValidPassword(password)) {
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
            userMapper.updateById(user);
            return true;
        }
        return false;
    }



}