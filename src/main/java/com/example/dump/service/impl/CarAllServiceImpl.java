package com.example.dump.service.impl;

import com.example.dump.entity.CarAll;
import com.example.dump.mapper.CarAllMapper;
import com.example.dump.service.ICarAllService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录所有垃圾归集系统的相关车辆，包括仁和以及天府环境的车辆 服务实现类
 * </p>
 *
 * @author xavi
 * @since 2022-11-21
 */
@Service
public class CarAllServiceImpl extends ServiceImpl<CarAllMapper, CarAll> implements ICarAllService {

}
