package com.example.dump.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.print.PrinterGraphics;
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

    IPage<DumpDataOfCar> dumpDataOfCar(Page<DumpDataOfCar> page, @Param("start") String start, @Param("end") String end, @Param("site_name") String site_name);

    Integer pastDumpAmountOfCar(@Param("site_name") String site_name, @Param("car_number") String car_number, @Param("start") String start, @Param("end") String end);
    Integer todayDumpAmountOfCar(@Param("site_name") String site_name, @Param("car_number") String car_number, @Param("start") String start, @Param("end") String end);

    IPage<CarData> carDumpAmountOfAllSite(Page<DumpDataOfCar> page, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd, @Param("dateTimeStart") String dateTimeStart, @Param("dateTimeEnd") String dateTimeEnd);

    List<CarData> carDumpAmount(@Param("car_number") String car_number, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd, @Param("dateTimeStart") String dateTimeStart, @Param("dateTimeEnd") String dateTimeEnd);

    List<DumpDataOfSite> dumpDataOfSite(@Param("start") String start, @Param("end") String end, @Param("site_name") String site_name);

    List<DBStatus> checkStatus();
}
