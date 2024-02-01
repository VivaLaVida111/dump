package com.example.dump.mapper;

import com.example.dump.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2024-01-29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
