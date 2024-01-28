package com.example.dump.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.CarAll;
import com.example.dump.service.ICarAllService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 记录所有垃圾归集系统的相关车辆，包括仁和以及天府环境的车辆 前端控制器
 * </p>
 *
 * @author xavi
 * @since 2022-11-21
 */
@Api(tags = "car-all表的增删改查与分页查询")
@RestController
@RequestMapping("//car")
public class CarAllController {

    @Resource
    private ICarAllService carAllService;

    /**
     * 返回天府环境和仁和星牛的所有车辆数据
     * @return
     */
    @ApiOperation(value = "查询全部车辆")
    @GetMapping()
    public List<CarAll> getAll() {
        return carAllService.list();
    }

    /**
     * 分页查询返回天府环境和仁和星牛的车辆数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "要查询第几页", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页有几条记录", dataType = "int", required = true),
    })
    @GetMapping("/page")
    public Page<CarAll> findPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize) {
        return carAllService.page(new Page<>(pageNum, pageSize));
    }
}

