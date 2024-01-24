package com.example.dump.controller;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.*;
import com.example.dump.service.IDumpRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询某个垃圾站一段时间之间的垃圾记录
     * @param site_name 站点名
     * @param transporter
     * @param start 起始时间
     * @param end 终止时间 格式：YYYY-MM-DDTHH:mm:ss
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页条件查询：siteName, transporter, start, end, pageNum, pageSize")
    @GetMapping("/page/{site_name}/{transporter}/{start}/{end}/{pageNum}/{pageSize}")
    public List<DumpRecord> conditionSelectAndPage(@PathVariable String site_name, @RequestParam(required = false) String transporter,
                                                   @PathVariable String start, @PathVariable String end,
                                                   @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return dumpRecordService.conditionSelectAndPage(site_name, transporter, start, end, pageNum, pageSize);
    }

    /**
     * 查询某个垃圾站一段时间之间的垃圾总净重量，精确到某个具体的时间范围
     * @param site_name 站点名
     * @param start 起始时间
     * @param end 终止时间 格式：YYYY-MM-DDTHH:mm:ss
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "一段时间内的垃圾总量：siteName, start, end, pageNum, pageSize")
    @GetMapping("/getSum/{site_name}/{start}/{end}/{pageNum}/{pageSize}")
    public JSONObject getSumByDuration(@PathVariable String site_name,
                                                   @PathVariable String start, @PathVariable String end,
                                                   @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return dumpRecordService.getSumByDuration(site_name,  start, end, pageNum, pageSize);
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

    /*
    @ApiOperation(value = "查询指定车辆当日起止时间垃圾总量以及预测值，start以及end只需时间不要日期")
    @GetMapping("/car_data/{site_name}/{car_number}/{start}/{end}")
    public CarData carData(@PathVariable String site_name, @PathVariable String car_number, @PathVariable String start, @PathVariable String end) {
        CarData carData = new CarData();
        carData.setCarNumber(car_number);
        carData.setSiteName(site_name);
        Integer pastAmount = dumpRecordService.pastDumpAmountOfCar(site_name, car_number, start, end);
        carData.setPredictAmount(pastAmount == null? null : pastAmount / 7);
        carData.setTodayAmount(dumpRecordService.todayDumpAmountOfCar(site_name, car_number, start, end));
        return carData;
    }
     */
    @ApiOperation(value = "查询指定车辆当日起止时间垃圾总量以及预测值，start以及end只需时间不要日期")
    @GetMapping("/car_data/{car_number}/{start}/{end}")
    public List<CarData> carDumpAmount(@PathVariable String car_number, @PathVariable String start, @PathVariable String end) {
        return dumpRecordService.carDumpAmount(car_number, start, end);
    }

    @ApiOperation(value = "查询全站当日起止时间垃圾总量以及预测值，start以及end只需时间不要日期")
    @GetMapping("/car_data/all_site/{start}/{end}/{pageNum}/{pageSize}")
    public IPage<CarData> carDumpAmountOfAllSite(@PathVariable String start, @PathVariable String end, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        IPage<CarData> res = dumpRecordService.carDumpAmountOfAllSite(new Page<>(pageNum, pageSize), start, end);
        return res;
    }

    @ApiOperation(value = "站点每日垃圾量查询;若需全部站点数据,则site_name填all")
    @GetMapping("/site_data_day/{start}/{end}/{site_name}")
    public void dumpDataOfSite(HttpServletResponse response, @PathVariable String start, @PathVariable String end,
                               @PathVariable String site_name) throws IOException {
        List<DumpDataOfSite> data = dumpRecordService.dumpDataOfSite(start, end, site_name);
        // 设置文本内省
        response.setContentType("application/vnd.ms-excel");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        // 设置响应头
        if ("big_stations".equals(site_name)) {
            site_name = "大站";
        } else if ("small_stations".equals(site_name)) {
            site_name = "小站";
        }
        String name = site_name + start + "至" + end + "垃圾总量统计" + ".xlsx";
        String encodedFileName = URLUtil.encode(name, CharsetUtil.CHARSET_UTF_8);
        response.setHeader("Content-disposition",  "attachment;filename="+encodedFileName);
        //System.out.println(encodedFileName);
        //System.out.println(URLUtil.decode(encodedFileName, CharsetUtil.CHARSET_UTF_8));

        EasyExcel.write(response.getOutputStream(), DumpDataOfSite.class).sheet(name).doWrite(data);
    }

    @ApiOperation(value = "查询站点垃圾记录并按承运单位排序")
    @GetMapping("/record_site_trans/{start}/{end}/{site_name}")
    public void dumpRecordOfSiteByTrans(HttpServletResponse response, @PathVariable String start, @PathVariable String end,
                               @PathVariable String site_name) throws IOException {
        List<DumpRecordOfSiteByTrans> data = dumpRecordService.dumpRecordOfSiteByTrans(start, end, site_name);
        // 设置文本内省
        response.setContentType("application/vnd.ms-excel");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        String name = site_name + start + "至" + end + "垃圾记录统计" + ".xlsx";
        String encodedFileName = URLUtil.encode(name, CharsetUtil.CHARSET_UTF_8);
        response.setHeader("Content-disposition",  "attachment;filename="+encodedFileName);
        EasyExcel.write(response.getOutputStream(), DumpRecordOfSiteByTrans.class).sheet(name).doWrite(data);
    }

    /**
     * 返回站点每日垃圾量以及一段日期内的垃圾净重总和，没精确到具体的时分秒，以天为单位
     * @param start
     * @param end
     * @param site_name
     * @return
     */
    @ApiOperation(value = "站点每日垃圾总量查询;只返回数组，不返回Excel")
    @GetMapping("/site_data_day_array/{start}/{end}/{site_name}")
    public List<DumpDataOfSite> dumpDataOfSiteArray(@PathVariable String start, @PathVariable String end,
                                               @PathVariable String site_name) {
        return dumpRecordService.dumpDataOfSite(start, end, site_name);
    }


    @ApiOperation(value = "站点今日预测值，根据过去一周从零点到当前时分秒的垃圾净重总量求平均值。并且判断是否超过阈值，生成告警事件")
    @GetMapping("/getPredictByStation/{site_name}")
    public JSONObject getPredictByStation(@PathVariable String site_name) {
        return dumpRecordService.getPredictByStation(site_name);
    }
}

