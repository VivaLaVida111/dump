package com.example.dump.schedule;

import com.example.dump.service.IDumpRecordService;
import com.example.dump.service.IEventQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class stationWarningSchedule {
    @Autowired
    IEventQueryService iEventQueryService;

    @Autowired
    IDumpRecordService iDumpRecordService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //每隔五分钟轮询一次
    @Scheduled(cron = "0 0/5 10-22 * * ? ")
//    @Scheduled(cron = "0/1 * * * * ? ")

    public void persistenceGarbageEvent(){
        logger.info("开始执行定时任务");
        //早上10点到晚上22点，每隔5分钟检测是否有新事件
        iEventQueryService.getGarbageEventPeriod();

//        iEventQueryService.getNeedHandleEvent();

        logger.info("轮询事件");
    }
}
