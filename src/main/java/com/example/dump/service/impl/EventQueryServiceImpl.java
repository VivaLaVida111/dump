package com.example.dump.service.impl;


import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.dump.entity.DumpDataOfSite;
import com.example.dump.entity.EventQuery;
import com.example.dump.entity.WarningOfSite;
import com.example.dump.mapper.DumpRecordMapper;
import com.example.dump.mapper.EventQueryMapper;
import com.example.dump.service.IDumpRecordService;
import com.example.dump.service.IEventQueryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dump.service.IGpsRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.formula.functions.Even;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wlp
 * @since 2023-12-22
 */
@Service
public class EventQueryServiceImpl extends ServiceImpl<EventQueryMapper, EventQuery> implements IEventQueryService {
    @Autowired
    IEventQueryService iEventQueryService;

    @Resource
    EventQueryMapper eventQueryMapper;

    @Resource
    IGpsRecordService gpsRecordService;

    @Resource
    IDumpRecordService dumpRecordService;
    @Autowired
    IDumpRecordService iDumpRecordService;
    /**
     * @Date:2023/06/05
     * @author:Sunny
     * @description: 访问垃圾系统告警事件接口，读取后通过判断是否为新事件，若是则存入数据库
     */
    @Override
    public boolean getGarbageEventPeriod(){

        //读取城市管家实时传送过来的渗滤液告警事件，因为两个系统都部署在政务云，所以访问的ip是内网+端口号： http://172.30.19.10:8084/
        String queryUrl = "http://172.30.19.10:8084/shenlvye/getShenlvyeWarning";
        HttpResponse queRes = HttpRequest.get(queryUrl)
                .execute();
        JSONObject dataArray =  JSONObject.parseObject(queRes.body()).getJSONObject("data");
        //渗滤液告警事件
        Map<String, String> res = (Map) dataArray;
//        Map<String, String> res = new HashMap<>();
        //车辆gps告警事件
        gpsRecordService.checkCarUsage(res);
        //垃圾站采集告警事件
        dumpRecordService.checkDB(res);
        //垃圾量实际值和预测值的差值超过阈值
        iDumpRecordService.getWeightWarning(res);
        //是否为新事件
        boolean isNewEvent = false;

        //目前有告警事件
        if(!res.isEmpty()){
            for (Object key : res.keySet()) {
                String data = (String)res.get(key);
                String date = data.substring(0,data.indexOf("."));
                String event = data.substring(data.indexOf(" "),data.length());
                date = date.substring(0,date.indexOf("T"))+" "+date.substring(date.indexOf("T")+1,date.length());
                LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                LambdaQueryWrapper<EventQuery> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                //根据事件来源和原因筛查数据库中上一次存入的事件里是否已存在相同事件。
                lambdaQueryWrapper.eq(EventQuery::getEventSource,(String) key)
                        .eq(EventQuery::getEventCause,event)
                        .eq(EventQuery::getEventSign,"上次");
                List<EventQuery> list= iEventQueryService.list(lambdaQueryWrapper);
                //数据库中没有该事件
                if(list.isEmpty()){
                    //将上一次存入的设置标志为“以前”
                    LambdaUpdateWrapper<EventQuery> oldWrapper = new LambdaUpdateWrapper<>();
                    oldWrapper.eq(EventQuery::getEventSign,"上次");
                    oldWrapper.set(EventQuery::getEventSign, "以前");

                    //新事件设置标志为“上次”
                    isNewEvent = true;
                    EventQuery eventQuery = new EventQuery();
                    eventQuery.setEventSource((String) key);
                    eventQuery.setEventTime(dateTime);
                    eventQuery.setEventCause(event);
                    eventQuery.setEventSign("上次");
                    iEventQueryService.save(eventQuery);
                }else{
                    LocalDateTime eventTime = list.get(0).getEventTime();
                    System.out.println("事件发生时间："+eventTime);
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("当前时间："+now);
                    Duration duration = Duration.between(eventTime, now);

                    // 计算时间差
                    long days = duration.toDays();
                    long hours = duration.toHours() % 24;
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.toMillis() % 60;

                    String durations = days + " 天 "+ hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒";

                    System.out.println(durations);

                    //将目前该事件的持续时长更新
                    LambdaUpdateWrapper<EventQuery> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    System.out.println("事件来源："+key+"事件："+event);
                    lambdaUpdateWrapper.eq(EventQuery::getEventSource,(String) key)
                            .eq(EventQuery::getEventCause,event)
                            .eq(EventQuery::getEventSign,"上次");
                    lambdaUpdateWrapper.set(EventQuery::getIssueDuration,durations);
                    boolean affectedRows = iEventQueryService.update(lambdaUpdateWrapper);
                    System.out.println("更新数据库是否成功："+affectedRows);
                }
                System.out.println("是否是新事件："+isNewEvent);
            }
        }

        return isNewEvent;

    }

    /**
     * @description:查询数据库中的历史事件
     * @return
     */
    @Override
    public List<EventQuery> getAllEvent(@RequestBody String dateAndSite) {
        JSONObject jsonObject =JSONObject.parseObject(dateAndSite);
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String site_name = jsonObject.getString("site_name");


        System.out.println("startTime:"+startTime);
        LambdaQueryWrapper<EventQuery> lambdaQueryWrapper = new LambdaQueryWrapper<EventQuery>();
        if(endTime ==null || endTime.isEmpty() ){
            // 获取当前时间
            Date currentDate = new Date();

            // 设置日期时间格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 格式化当前时间
            endTime = dateFormat.format(currentDate);
        }
        System.out.println("endTime:"+endTime);
        System.out.println("startTime:"+startTime);
        //如果起始时间为空
        if(startTime == null|| startTime.isEmpty()  ) {
            lambdaQueryWrapper.le(EventQuery::getEventTime, endTime);
        }else{
            // 查询起始时间与终止时间之间的所有告警事件
            lambdaQueryWrapper.between(EventQuery::getEventTime, startTime, endTime);
        }
        //查询某个站点
        if(site_name !=null){
            if(site_name.equals("所有站点")){
                List<String> siteNames = Arrays.asList("西华", "红星","五里墩", "五块石","红花堰");
                lambdaQueryWrapper.in(EventQuery::getEventSource, siteNames);
            }else if(site_name.equals("大站")) {
                List<String> siteNames = Arrays.asList("西华", "红星");
                lambdaQueryWrapper.in(EventQuery::getEventSource, siteNames);
            }else if(site_name.equals("小站")){
                List<String> siteNames = Arrays.asList("五里墩", "五块石","红花堰");
                lambdaQueryWrapper.in(EventQuery::getEventSource, siteNames);
            }else{
                lambdaQueryWrapper.eq(EventQuery::getEventSource,site_name);
            }
        }

        //按照时间倒序返回告警事件
        List<EventQuery> list =iEventQueryService.list(lambdaQueryWrapper.orderByDesc(EventQuery::getEventTime));

        return list;
    }


    @Override
    public List<WarningOfSite> waringOfSite(@RequestBody String dateAndSite){
        JSONObject jsonObject =JSONObject.parseObject(dateAndSite);
        String start = jsonObject.getString("startTime");
        String end = jsonObject.getString("endTime");
        String event_source = jsonObject.getString("site_name");
        List<WarningOfSite> res = eventQueryMapper.warningOfSite(start, end, event_source);

        return res;
    }
}
