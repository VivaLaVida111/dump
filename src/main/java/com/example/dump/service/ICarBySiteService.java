package com.example.dump.service;

import com.example.dump.entity.CarBySite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点） 服务类
 * </p>
 *
 * @author luo
 * @since 2023-02-25
 */
public interface ICarBySiteService extends IService<CarBySite> {
    List<CarBySite> selectBySite(String siteName);
}
