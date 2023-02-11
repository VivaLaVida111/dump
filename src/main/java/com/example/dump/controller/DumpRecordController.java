package com.example.dump.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.DumpDataOfCar;
import com.example.dump.entity.DumpRecord;
import com.example.dump.service.IDumpRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
@Api(tags = "dump-record表的增删改查与分页查询")
@RestController
@RequestMapping("//dump-record")
public class DumpRecordController {

    @Resource
    private IDumpRecordService dumpRecordService;

    @ApiOperation(value = "查询指定站点名的记录")
    @GetMapping("/test/{site_name}")
    public List<DumpRecord> test(@PathVariable String site_name) {
        return dumpRecordService.selectBySiteName(site_name);
    }

    @ApiOperation(value = "查询指定日期区间的记录")
    @GetMapping("/period/{start}/{end}")
    public List<DumpRecord> selectByPeriod(@PathVariable String start, @PathVariable String end) {
        return dumpRecordService.selectByPeriod(start, end);
    }

    @ApiOperation(value = "查询指定站点名的记录")
    @GetMapping("/site_name/{site_name}")
    public List<DumpRecord> selectBySiteName(@PathVariable String site_name) {
        return dumpRecordService.selectBySiteName(site_name);
    }

    @ApiOperation(value = "分页条件查询：siteName, transporter, start, end, pageNum, pageSize")
    @GetMapping("/page/{site_name}/{transporter}/{start}/{end}/{pageNum}/{pageSize}")
    public List<DumpRecord> conditionSelectAndPage(@PathVariable String site_name, @RequestParam(required = false) String transporter,
                                                   @PathVariable String start, @PathVariable String end,
                                                   @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return dumpRecordService.conditionSelectAndPage(site_name, transporter, start, end, pageNum, pageSize);
    }

    @ApiOperation(value = "分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "要查询第几页", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页有几条记录", dataType = "int", required = true),
    })
    @GetMapping("/page")
    public Page<DumpRecord> findPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize) {
        QueryWrapper<DumpRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return dumpRecordService.page(new Page<>(pageNum, pageSize));
    }

    @ApiOperation(value = "车辆倾倒数据查询;若需全部站点数据,则site_name填all")
    @GetMapping("/dump_car/{start}/{end}/{site_name}/{pageNum}/{pageSize}")
    public IPage<DumpDataOfCar> dumpDataOfCar(@PathVariable String start, @PathVariable String end,
                                              @PathVariable String site_name,
                                              @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        IPage<DumpDataOfCar> res = dumpRecordService.dumpDataOfCar(new Page<>(pageNum, pageSize), start, end, site_name);
        return res;
    }
}

