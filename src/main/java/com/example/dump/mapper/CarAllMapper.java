package com.example.dump.mapper;

import com.example.dump.entity.CarAll;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 记录所有垃圾归集系统的相关车辆，包括仁和以及天府环境的车辆 Mapper 接口
 * </p>
 *
 * @author xavi
 * @since 2022-11-21
 */
@Mapper
public interface CarAllMapper extends BaseMapper<CarAll> {

}
