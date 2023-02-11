package com.example.dump.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.DumpDataOfCar;
import com.example.dump.entity.DumpRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
public interface IDumpRecordService extends IService<DumpRecord> {
    List<DumpRecord> selectByPeriod(String start, String end);

    List<DumpRecord> selectBySiteName(String site_name);

    List<DumpRecord> conditionSelectAndPage(String site_name, String transporter, String start, String end, Integer pageNum, Integer pageSize);

    List<DumpRecord> test(String site_name);

    IPage<DumpDataOfCar> dumpDataOfCar(Page<DumpDataOfCar> page, String start, String end, String site_name);
}
