package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dump.entity.*;
import com.example.dump.mapper.AlarmRecordMapper;
import com.example.dump.mapper.DumpRecordMapper;
import com.example.dump.service.IAlarmRecordService;
import com.example.dump.service.IDumpRecordService;
import com.example.dump.service.IGpsRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    IAlarmRecordService alarmRecordService;

    @Resource
    IGpsRecordService gpsRecordService;

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

    @Override
    public List<DumpDataOfSite> dumpDataOfSite(String start, String end, String site_name) {
        return dumpRecordMapper.dumpDataOfSite(start, end, site_name);
    }

    @Override
    public void checkDB(Map<String, String> res) {
        List<DBStatus> list = dumpRecordMapper.checkStatus();
        for (DBStatus item : list) {
            if ((item.getPredict() != null && item.getPredict() > 0) && (item.getActual() == null || item.getActual() < item.getPredict() / 10)) {
                LocalDateTime now = LocalDateTime.now();
                String info = now + " 进站数据小于预测值的10%";
                res.put(item.getSiteName(), info);
                AlarmRecord record = new AlarmRecord();
                record.setExactDate(now);
                record.setTimeInterval("00:00 to now");
                record.setName(item.getSiteName());
                record.setCategory("DB_status");
                alarmRecordService.insertByDeDuplication(record);
            }
        }
    }

}
