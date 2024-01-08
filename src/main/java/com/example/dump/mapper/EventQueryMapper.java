package com.example.dump.mapper;

import com.example.dump.entity.DumpDataOfSite;
import com.example.dump.entity.EventQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dump.entity.WarningOfSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wlp
 * @since 2023-12-22
 */
@Mapper
public interface EventQueryMapper extends BaseMapper<EventQuery> {

    List<WarningOfSite> warningOfSite(@Param("start") String start, @Param("end") String end, @Param("event_source") String event_source);

}
