package com.example.dump.service.impl;

import com.example.dump.entity.OfflineRecord;
import com.example.dump.mapper.OfflineRecordMapper;
import com.example.dump.service.IOfflineRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定期查询dump_record入库数据时间，与历史入库时间比对，以此判断数据读取程序是否掉线，存在误差 服务实现类
 * </p>
 *
 * @author luo
 * @since 2023-03-16
 */
@Service
public class OfflineRecordServiceImpl extends ServiceImpl<OfflineRecordMapper, OfflineRecord> implements IOfflineRecordService {

}
