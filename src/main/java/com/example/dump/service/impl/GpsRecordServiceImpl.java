package com.example.dump.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dump.entity.AlarmRecord;
import com.example.dump.entity.CarAll;
import com.example.dump.entity.CarRenhe;
import com.example.dump.entity.GpsRecord;
import com.example.dump.mapper.CarAllMapper;
import com.example.dump.mapper.CarRenheMapper;
import com.example.dump.mapper.GpsRecordMapper;
import com.example.dump.service.IAlarmRecordService;
import com.example.dump.service.IGpsRecordService;
import com.example.dump.utils.QueryTianFu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xavi
 * @since 2022-11-16
 */
@Service
public class GpsRecordServiceImpl extends ServiceImpl<GpsRecordMapper, GpsRecord> implements IGpsRecordService {

    @Resource
    GpsRecordMapper gpsRecordMapper;
    @Resource
    CarRenheMapper carRenheMapper;
    @Resource
    CarAllMapper carAllMapper;
    @Resource
    IAlarmRecordService alarmRecordService;

    @Override
    public List<GpsRecord> selectByCarAndPeriod(String carNumber, String start, String end){
        QueryWrapper<CarRenhe> wrapper = new QueryWrapper<CarRenhe>();
        wrapper.eq("name", carNumber);
        CarRenhe car = carRenheMapper.selectOne(wrapper);
        if (car != null) {
            return gpsRecordMapper.selectByCarAndPeriod(carNumber, start, end);
        } else {
            return  QueryTianFu.getGPSTrack(carNumber, start, end);
        }
    }

    @Override
    public List<GpsRecord> selectLatest() {
        QueryWrapper<CarAll> wrapper = new QueryWrapper<>();
        wrapper.eq("company", "天府环境");
        List<CarAll> carList = carAllMapper.selectList(wrapper);
        List<String> carNumbers = new ArrayList<>();
        if (carList != null) {
            for (CarAll car : carList) {
                carNumbers.add(car.getCarNumber());
            }
        }
        List<GpsRecord> resTianFu = QueryTianFu.getLatestGPS(carNumbers);
        List<GpsRecord> resRenHe = gpsRecordMapper.selectLatest();
        List<GpsRecord> res = new ArrayList<>();
        res.addAll(resRenHe);
        res.addAll(resTianFu);

        /* 确认是否遗漏
        List<String> allNumbers = new ArrayList<>();
        for (GpsRecord record : res) {
            allNumbers.add(record.getCarNumber());
        }
        QueryWrapper<CarAll> wrapper1 = new QueryWrapper<>();
        wrapper1.isNotNull("car_number");
        List<CarAll> carAlls = carAllMapper.selectList(wrapper1);
        Map<String, String> map = new HashMap<>();
        for (CarAll car : carAlls) {
            map.put(car.getCarNumber(), "1");
        }
        for (String car : allNumbers) {
            map.remove(car);
        }
        for (String carNumber : map.keySet()) {
            System.out.println(carNumber);
        }
         */
        return res;
    }

    @Override
    public void checkCarUsage(Map<String, String> res) {
        List<GpsRecord> records = selectLatest();
        QueryWrapper<CarAll> wrapper = new QueryWrapper<>();
        List<CarAll> carList = carAllMapper.selectList(wrapper);
        int tianfu = 0;
        int renhe = 0;
        for (CarAll car : carList) {
            if ("天府环境".equals(car.getCompany())) {
                tianfu++;
            } else {
                renhe++;
            }
        }
        int tianfuUse = 0;
        int renheUse = 0;
        for (GpsRecord record : records) {
            if (record.getId() != null) {
                renheUse++;
            } else {
                tianfuUse++;
            }
        }

        double f1 = new BigDecimal((float)tianfuUse/tianfu).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (f1 < 0.7) {
            LocalDateTime now = LocalDateTime.now();
            String info = now + " 垃圾转运车辆严重偏少";
            res.put("天府环境", info);
            AlarmRecord record = new AlarmRecord();
            record.setExactDate(now);
            record.setTimeInterval("1 hour");
            record.setName("天府环境");
            record.setCategory("car_usage");
            alarmRecordService.insertByDeDuplication(record);
        }

        double f2 = new BigDecimal((float)renheUse/renhe).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (f2 < 0.7) {
            LocalDateTime now = LocalDateTime.now();
            String info = now + " 垃圾转运车辆严重偏少";
            res.put("仁和星牛", info);
            AlarmRecord record = new AlarmRecord();
            record.setExactDate(now);
            record.setTimeInterval("1 hour");
            record.setName("仁和星牛");
            record.setCategory("car_usage");
            alarmRecordService.insertByDeDuplication(record);
        }
    }
}
