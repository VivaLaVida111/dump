package com.example.dump.service;
import com.example.dump.dto.UserDetailsImpl;
import com.example.dump.entity.LoginResult;
import com.example.dump.entity.User;

import java.util.List;

public interface AuthService {
    /**
     * 根据用户名获取用户信息
     */
    User getUserByName(String name);

    /**
     * 注册
     */
    User register(User userParam);

    /**
     * 登录
     * @return 生成的JWT的token
     */
    LoginResult login(String username, String password);

    /**
     * 免密登录
     * @author sunny
     * @return 生成的JWT的token
     * @date 2023.10.20
     */
    LoginResult pswFreeLogin(String username);
    /**
     * 检查密码是否符合规范
     */
    Boolean isValidPassword(String password);

    /**
     * 修改密码
     */
    Boolean changePassword(String name, String password);
}

