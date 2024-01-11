package com.example.dump.controller;


import com.example.dump.entity.CarBySite;
import com.example.dump.service.ICarBySiteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点） 前端控制器
 * </p>
 *
 * @author luo
 * @since 2023-02-25
 */
@RestController
@RequestMapping("//car-by-site")
public class CarBySiteController {
    @Resource
    private ICarBySiteService service;

    @ApiOperation(value = "查询站点对应车辆")
    @GetMapping("/{site_name}")
    public List<CarBySite> selectBySite(@PathVariable String site_name) {
        return service.selectBySite(site_name);
    }

    @ApiOperation(value = "查询全部站点车辆")
    @GetMapping("/all")
    public List<CarBySite> selectAll() {
        return service.list();
    }
}
