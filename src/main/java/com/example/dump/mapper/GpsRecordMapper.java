package com.example.dump.mapper;

import com.example.dump.entity.GpsRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xavi
 * @since 2022-11-16
 */
@Mapper
public interface GpsRecordMapper extends BaseMapper<GpsRecord> {
    List<GpsRecord> selectByCarAndPeriod(@Param("carNumber") String carNumber, @Param("start") String start, @Param("end") String end);
}
