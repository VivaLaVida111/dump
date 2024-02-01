package com.example.dump.service.impl;

import com.example.dump.entity.User;
import com.example.dump.mapper.UserMapper;
import com.example.dump.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-01-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
