package com.example.dump.service;

import com.example.dump.entity.GpsRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xavi
 * @since 2022-11-16
 */
public interface IGpsRecordService extends IService<GpsRecord> {
    List<GpsRecord> selectByCarAndPeriod(String carNumber, String start, String end);
}
