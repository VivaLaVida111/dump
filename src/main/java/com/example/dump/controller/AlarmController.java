package com.example.dump.controller;

import com.example.dump.service.IDumpRecordService;
import com.example.dump.service.IGpsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "警报接口")
@RestController
@RequestMapping("//alarm")
public class AlarmController {
    @Resource
    IGpsRecordService gpsRecordService;

    @Resource
    IDumpRecordService dumpRecordService;

    @ApiOperation(value = "警报处理")
    @GetMapping("/check_status")
    public Map<String, String> checkStatus() {
       Map<String, String> res = new HashMap<>();
       gpsRecordService.checkCarUsage(res);
       dumpRecordService.checkDB(res);
       return res;
    }
}
