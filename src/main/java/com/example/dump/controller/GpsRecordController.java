package com.example.dump.controller;


import com.example.dump.common.ResponseData;
import com.example.dump.common.ResultEnum;
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
    @GetMapping("/track/{car_number}/{start}/{end}")
    public ResponseData<Object> selectByCarAndPeriod(@PathVariable String car_number, @PathVariable String start, @PathVariable String end) {
        List<GpsRecord> res = gpsRecordService.selectByCarAndPeriod(car_number, start, end);
        ResponseData<Object> responseData = null;
        if (res == null || res.size() == 0) {
            responseData = new ResponseData<>(ResultEnum.FAILED);
            responseData.setData("error: 车牌号不存在或查询操作太频繁");
            return  responseData;
        }
        responseData = new ResponseData<>(ResultEnum.SUCCESS);
        responseData.setData(res);
        return responseData;
    }
}

