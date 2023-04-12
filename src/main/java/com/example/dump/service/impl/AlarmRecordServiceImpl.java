package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dump.entity.AlarmRecord;
import com.example.dump.mapper.AlarmRecordMapper;
import com.example.dump.service.IAlarmRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 报警记录 服务实现类
 * </p>
 *
 * @author luo
 * @since 2023-03-31
 */
@Service
public class AlarmRecordServiceImpl extends ServiceImpl<AlarmRecordMapper, AlarmRecord> implements IAlarmRecordService {
    @Resource
    AlarmRecordMapper alarmRecordMapper;

    public void insertByDeDuplication(AlarmRecord record) {
        QueryWrapper<AlarmRecord> wrapper = new QueryWrapper<AlarmRecord>();
        wrapper.eq("name", record.getName());
        wrapper.eq("category", record.getCategory());
        wrapper.orderByDesc("exact_date");
        wrapper.last("limit 1");
        List<AlarmRecord> records = alarmRecordMapper.selectList(wrapper);
        if (records != null ) {
            for (AlarmRecord latest : records) {
                Duration duration = Duration.between(latest.getExactDate(), record.getExactDate());
                long hours = duration.toHours();

                if (hours < 1) {
                    return;
                }
            }
        }
        alarmRecordMapper.insert(record);
    }
}
