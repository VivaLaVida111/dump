package com.example.dump.service.impl;

import com.example.dump.entity.AlarmRecord;
import com.example.dump.mapper.AlarmRecordMapper;
import com.example.dump.service.IAlarmRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
