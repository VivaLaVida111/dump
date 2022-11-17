package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dump.entity.CarRenhe;
import com.example.dump.entity.GpsRecord;
import com.example.dump.mapper.CarRenheMapper;
import com.example.dump.mapper.GpsRecordMapper;
import com.example.dump.service.IGpsRecordService;
import com.example.dump.utils.QueryTianFu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xavi
 * @since 2022-11-16
 */
@Service
public class GpsRecordServiceImpl extends ServiceImpl<GpsRecordMapper, GpsRecord> implements IGpsRecordService {

    @Resource
    GpsRecordMapper gpsRecordMapper;
    @Resource
    CarRenheMapper carRenheMapper;

    @Override
    public List<GpsRecord> selectByCarAndPeriod(String carNumber, String start, String end){
        QueryWrapper<CarRenhe> wrapper = new QueryWrapper<CarRenhe>();
        wrapper.eq("name", carNumber);
        CarRenhe car = carRenheMapper.selectOne(wrapper);
        if (car != null) {
            return gpsRecordMapper.selectByCarAndPeriod(carNumber, start, end);
        } else {
            return  QueryTianFu.getGPSLocus(carNumber, start, end);
        }
    }
}
