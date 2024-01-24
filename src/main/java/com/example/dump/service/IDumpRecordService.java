package com.example.dump.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
public interface IDumpRecordService extends IService<DumpRecord> {
    List<DumpRecord> selectByPeriod(String start, String end);

    List<DumpRecord> selectBySiteName(String site_name);

    List<DumpRecord> conditionSelectAndPage(String site_name, String transporter, String start, String end, Integer pageNum, Integer pageSize);

    JSONObject getSumByDuration(String site_name, String start, String end,Integer pageNum,Integer pageSize);


    JSONObject getPredictByStation(String site_name);

    JSONObject getPredict(String site_name);

    void getWeightWarning(Map<String,String> res);

    List<DumpRecord> test(String site_name);

    IPage<DumpDataOfCar> dumpDataOfCar(Page<DumpDataOfCar> page, String start, String end, String site_name);

    Integer pastDumpAmountOfCar(String site_name, String car_number, String start, String end);

    Integer todayDumpAmountOfCar(String site_name, String car_number, String start, String end);

    IPage<CarData> carDumpAmountOfAllSite(Page<DumpDataOfCar> page, String timeStart, String timeEnd);

    List<CarData> carDumpAmount(String car_number, String timeStart, String timeEnd);

    List<DumpDataOfSite> dumpDataOfSite(String start, String end, String site_name);

    List<DumpRecordOfSiteByTrans> dumpRecordOfSiteByTrans(String start, String end, String site_name);

    void checkDB(Map<String, String> res);

}
