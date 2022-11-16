package com.example.dump.controller;


import com.example.dump.entity.DumpRecord;
import com.example.dump.entity.GpsRecord;
import com.example.dump.service.IGpsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xavi
 * @since 2022-11-16
 */
@Api(tags = "车辆GPS查询")
@RestController
@RequestMapping("//gps-record")
public class GpsRecordController {

    @Resource
    private IGpsRecordService gpsRecordService;

    @ApiOperation(value = "查询指定指定车辆在指定时间段的gps记录")
    @GetMapping("/car_period/{car_number}/{start}/{end}")
    public List<GpsRecord> selectByCarAndPeriod(@PathVariable String car_number, @PathVariable String start, @PathVariable String end) {
        return gpsRecordService.selectByCarAndPeriod(car_number, start, end);
    }
}

