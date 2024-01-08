package com.example.dump.service;

import com.example.dump.entity.DumpDataOfSite;
import com.example.dump.entity.EventQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dump.entity.WarningOfSite;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlp
 * @since 2023-12-22
 */
public interface IEventQueryService extends IService<EventQuery> {
    boolean getGarbageEventPeriod();
    public List<EventQuery> getAllEvent(@RequestBody String dateAndSite);

//    List<EventQuery> warningOfSite(@RequestBody String dateAndSite);

    List<WarningOfSite> waringOfSite(@RequestBody String dateAndSite);
}
