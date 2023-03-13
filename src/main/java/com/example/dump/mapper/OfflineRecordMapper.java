package com.example.dump.mapper;

import com.example.dump.entity.OfflineRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 定期查询dump_record入库数据时间，与历史入库时间比对，以此判断数据读取程序是否掉线，存在误差 Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2023-03-16
 */
@Mapper
public interface OfflineRecordMapper extends BaseMapper<OfflineRecord> {

}
