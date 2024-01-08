package com.example.dump.controller;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.example.dump.entity.CarAll;
import com.example.dump.entity.DumpDataOfSite;
import com.example.dump.entity.EventQuery;
import com.example.dump.entity.WarningOfSite;
import com.example.dump.service.ICarAllService;
import com.example.dump.service.IEventQueryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wlp
 * @since 2023-12-22
 */
@RestController
@RequestMapping("//event-query")
public class EventQueryController {
    @Resource
    private IEventQueryService iEventQueryService;

    /**
     * 查询startTime和endTime之间的所有历史告警事件
     * 如果startTime和endTime为null，则默认查询当前时间以前的所有历史告警事件
     * startTime 格式：YYYY-MM-DD HH:mm:ss
     * @return
     */
    @ApiOperation(value = "查询历史告警事件")
    @PostMapping("/getAllWarning")
    public List<EventQuery> getAllWarning(@RequestBody String dateAndSite) {
        return iEventQueryService.getAllEvent(dateAndSite);
    }

    @ApiOperation(value = "报表：查询所有历史告警事件,如果是所有站，则site_name填all")
    @PostMapping("/warning")
    public void getWarningList(HttpServletResponse response,@RequestBody String dateAndSite) throws IOException {
        List<WarningOfSite> data = iEventQueryService.waringOfSite(dateAndSite);
        // 设置文本内省
        response.setContentType("application/vnd.ms-excel");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");

        JSONObject jsonObject =JSONObject.parseObject(dateAndSite);
        String start = jsonObject.getString("startTime");
        String end = jsonObject.getString("endTime");
        String site_name = jsonObject.getString("site_name");

        String name = site_name + start+ "至" +end+"历史告警事件汇总" + ".xlsx";
        String encodedFileName = URLUtil.encode(name, CharsetUtil.CHARSET_UTF_8);
        response.setHeader("Content-disposition",  "attachment;filename="+encodedFileName);


        EasyExcel.write(response.getOutputStream(), WarningOfSite.class).sheet(name).doWrite(data);
    }
}

