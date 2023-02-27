package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dump.entity.CarData;
import com.example.dump.entity.DumpDataOfCar;
import com.example.dump.entity.DumpRecord;
import com.example.dump.mapper.DumpRecordMapper;
import com.example.dump.service.IDumpRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
@Service
public class DumpRecordServiceImpl extends ServiceImpl<DumpRecordMapper, DumpRecord> implements IDumpRecordService {

    @Resource
    DumpRecordMapper dumpRecordMapper;



    @Override
    public List<DumpRecord> selectByPeriod(String start, String end) {
        return dumpRecordMapper.selectByPeriod(start, end);
    }

    @Override
    public List<DumpRecord> selectBySiteName(String site_name) {
        return dumpRecordMapper.selectBySiteName(site_name);
    }

    @Override
    public List<DumpRecord> conditionSelectAndPage(String site_name, String transporter, String start, String end, Integer pageNum, Integer pageSize) {
        Page<DumpRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DumpRecord> wrapper = new QueryWrapper<>();
        if(site_name != null)
            wrapper.eq("site_name", site_name);
        if(transporter != null)
            wrapper.eq("transporter", transporter);
        if(start != null && end != null)
            wrapper.between("exact_date", start, end);
        return dumpRecordMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public List<DumpRecord> test(String site_name) {
        QueryWrapper<DumpRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("site_name", site_name);
        return dumpRecordMapper.selectList(wrapper);
    }

    @Override
    public IPage<DumpDataOfCar> dumpDataOfCar(Page<DumpDataOfCar> page, String start, String end, String site_name) {
        return dumpRecordMapper.dumpDataOfCar(page, start, end, site_name);
    }

    @Override
    public Integer pastDumpAmountOfCar(String site_name, String car_number, String start, String end) {
        return dumpRecordMapper.pastDumpAmountOfCar(site_name, car_number, start, end);
    }

    @Override
    public Integer todayDumpAmountOfCar(String site_name, String car_number, String start, String end) {
        LocalDate date = LocalDate.now();
        String now = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        start = now + " " + start;
        end = now + " " + end;
        return dumpRecordMapper.todayDumpAmountOfCar(site_name, car_number, start, end);
    }

    @Override
    public IPage<CarData> carDumpAmountOfAllSite(Page<DumpDataOfCar> page, String timeStart, String timeEnd) {
        LocalDate date = LocalDate.now();
        String now = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateTimeStart = now + " " + timeStart;
        String dateTimeEnd = now + " " + timeEnd;
        return dumpRecordMapper.carDumpAmountOfAllSite(page, timeStart, timeEnd, dateTimeStart, dateTimeEnd);
    }

    @Override
    public List<CarData> carDumpAmount(String car_number, String timeStart, String timeEnd) {
        LocalDate date = LocalDate.now();
        String now = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateTimeStart = now + " " + timeStart;
        String dateTimeEnd = now + " " + timeEnd;
        return dumpRecordMapper.carDumpAmount(car_number, timeStart, timeEnd, dateTimeStart, dateTimeEnd);
    }
}
