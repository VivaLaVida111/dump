package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dump.entity.CarBySite;
import com.example.dump.mapper.CarBySiteMapper;
import com.example.dump.service.ICarBySiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点） 服务实现类
 * </p>
 *
 * @author luo
 * @since 2023-02-25
 */
@Service
public class CarBySiteServiceImpl extends ServiceImpl<CarBySiteMapper, CarBySite> implements ICarBySiteService {
    @Resource
    CarBySiteMapper mapper;

    @Override
    public List<CarBySite> selectBySite(String siteName) {
        QueryWrapper<CarBySite> wrapper = new QueryWrapper<>();
        wrapper.eq("site_name", siteName);
        return mapper.selectList(wrapper);
    }
}
