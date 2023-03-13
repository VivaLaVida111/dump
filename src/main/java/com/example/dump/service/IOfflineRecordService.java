package com.example.dump.service;

import com.example.dump.entity.OfflineRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 定期查询dump_record入库数据时间，与历史入库时间比对，以此判断数据读取程序是否掉线，存在误差 服务类
 * </p>
 *
 * @author luo
 * @since 2023-03-16
 */
public interface IOfflineRecordService extends IService<OfflineRecord> {

}
