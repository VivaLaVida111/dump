package com.example.dump.mapper;

import com.example.dump.entity.DumpRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
@Mapper
public interface DumpRecordMapper extends BaseMapper<DumpRecord> {
    List<DumpRecord> selectByPeriod(@Param("start") String start, @Param("end") String end);

    List<DumpRecord> selectBySiteName(@Param("site_name") String site_name);
}
