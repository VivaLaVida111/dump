package com.example.dump.mapper;

import com.example.dump.entity.CarBySite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点） Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2023-02-25
 */
@Mapper
public interface CarBySiteMapper extends BaseMapper<CarBySite> {

}
