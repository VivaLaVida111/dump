package com.example.dump.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dump.entity.*;
import com.example.dump.mapper.DumpRecordMapper;
import com.example.dump.service.IAlarmRecordService;
import com.example.dump.service.IDumpRecordService;
import com.example.dump.service.IGpsRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public JSONObject getSumByDuration(String site_name,String start, String end,Integer pageNum,Integer pageSize){
        JSONObject res= new JSONObject();
        QueryWrapper<DumpRecord> wrapper = new QueryWrapper<>();
        if(site_name != null)
            wrapper.eq("site_name", site_name);
        if(start != null && end != null)
            wrapper.between("exact_date", start, end);

        wrapper.select("SUM(net_weight) AS net_weight");

        //垃圾站出现异常时设置垃圾量的默认值为0
        BigDecimal sumResult = BigDecimal.valueOf(0.0);

        // 执行查询
        List<Object> resultList = dumpRecordMapper.selectObjs(wrapper);

        //当垃圾站有数据时
        if(resultList.get(0) != null){
            // 获取求和结果
            sumResult = resultList.isEmpty() ? BigDecimal.ZERO : (BigDecimal) resultList.get(0);
        }

        res.put("site_name",site_name);
        res.put("startTime",start);
        res.put("endTime",end);
        res.put("net_weight",sumResult);
        return res;
    }



    @Override
    public JSONObject getPredictByStation(String site_name) {
        JSONObject res = new JSONObject();
        Double todayWeight=0.0;
        Double predictWeight =0.0;
        if(site_name.equals("大站")){
            todayWeight =Double.parseDouble(String.valueOf(getPredict("西华").get("actualWeight")))  + Double.parseDouble(String.valueOf(getPredict("红星").get("actualWeight")));
            predictWeight =Double.parseDouble(String.valueOf(getPredict("西华").get("predictWeight")))  + Double.parseDouble(String.valueOf(getPredict("红星").get("predictWeight")));

        }else if(site_name.equals("小站")){
            todayWeight =Double.parseDouble(String.valueOf(getPredict("五里墩").get("actualWeight")))  + Double.parseDouble(String.valueOf(getPredict("红花堰").get("actualWeight"))) + Double.parseDouble(String.valueOf(getPredict("五块石").get("actualWeight")))+ Double.parseDouble(String.valueOf(getPredict("泉水").get("actualWeight")))+ Double.parseDouble(String.valueOf(getPredict("营门口").get("actualWeight")))+ Double.parseDouble(String.valueOf(getPredict("金泉").get("actualWeight")))+ Double.parseDouble(String.valueOf(getPredict("西北桥").get("actualWeight")))+ Double.parseDouble(String.valueOf(getPredict("黄忠").get("actualWeight")));
            predictWeight =Double.parseDouble(String.valueOf(getPredict("五里墩").get("predictWeight")))  + Double.parseDouble(String.valueOf(getPredict("红花堰").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("五块石").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("泉水").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("营门口").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("金泉").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("西北桥").get("predictWeight")))+ Double.parseDouble(String.valueOf(getPredict("黄忠").get("predictWeight")));
        }else{
            todayWeight =Double.parseDouble(String.valueOf(getPredict(site_name).get("actualWeight")));
            predictWeight =Double.parseDouble(String.valueOf(getPredict(site_name).get("predictWeight")));
        }

        String predict = String.format("%.2f", predictWeight);
        String actual = String.format("%.2f", todayWeight);
        res.put("site_name",site_name);
        res.put("predictWeight",predict);
        res.put("actualWeight",actual);

        //判断预测值与实际值的差值是否超过阈值20%
        Double discrepancy = Math.abs(todayWeight - predictWeight);

        if( discrepancy<= predictWeight * 0.2){
            res.put("status","正常");
        }else{

            if(todayWeight - predictWeight < 0 )
                res.put("status","今日垃圾量低于预测值的20%");
            else
                res.put("status","今日垃圾量高于预测值的20%");

        }
        return res;
    }

    public void getWeightWarning(Map<String,String> res){
        String xihua = String.valueOf(getPredictByStation("西华").get("status"));
        String hongxing =  String.valueOf(getPredictByStation("红星").get("status"));
        String honghuayan =  String.valueOf(getPredictByStation("红花堰").get("status"));
        String wulidun =  String.valueOf(getPredictByStation("五里墩").get("status"));
        String wukuaishi =  String.valueOf(getPredictByStation("五块石").get("status"));
        String quanshui =  String.valueOf(getPredictByStation("泉水").get("status"));
        String yingmenkou =  String.valueOf(getPredictByStation("营门口").get("status"));
        String jinquan =  String.valueOf(getPredictByStation("金泉").get("status"));
        String xibeiqiao =  String.valueOf(getPredictByStation("西北桥").get("status"));
        String huangzhong =  String.valueOf(getPredictByStation("黄忠").get("status"));
        LocalDateTime now = LocalDateTime.now();
        if(!xihua.equals("正常")){

            res.put("西华",now+" "+xihua);
        }
        if(!hongxing.equals("正常")){
            res.put("红星",now+" "+hongxing);
        }
        if(!honghuayan.equals("正常")){
            res.put("红花堰",now+" "+honghuayan);
        }
        if(!wulidun.equals("正常")){
            res.put("五里墩",now+" "+wulidun);
        }
        if(!wukuaishi.equals("正常")){
            res.put("五块石",now+" "+wukuaishi);
        }
        if(!wukuaishi.equals("正常")){
            res.put("泉水",now+" "+quanshui);
        }
        if(!wukuaishi.equals("正常")){
            res.put("营门口",now+" "+yingmenkou);
        }
        if(!wukuaishi.equals("正常")){
            res.put("金泉",now+" "+jinquan);
        }
        if(!wukuaishi.equals("正常")){
            res.put("西北桥",now+" "+xibeiqiao);
        }
        if(!wukuaishi.equals("正常")){
            res.put("黄忠",now+" "+huangzhong);
        }
    }


    @Override
    public JSONObject getPredict(String site_name){
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间："+now);
        Double total = 0.0;
        Double todayWeight = 0.0;
        for(int i = 0; i < 7 ; i++){
            //计算过去一周内某一天的零点以及当前时间
            LocalDateTime passedDayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime passedDayEnd = now.minusDays(i);

            // 格式化为字符串，不包含小数点及其后的部分
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String start= passedDayStart.format(formatter);
            String end= passedDayEnd.format(formatter);


            //计算某一天零点到当前时间的垃圾净重量
            Double sumResult =  Double.parseDouble(String.valueOf(getSumByDuration(site_name, start, end,1,10000).get("net_weight")));


            //记录今天的实际垃圾量
            if(i == 0){
                todayWeight = Double.parseDouble(String.valueOf(sumResult)) / 1000;
            }

            total = total + Double.parseDouble(String.valueOf(sumResult));

        }
        //过去一周从零点到当前时间的垃圾净重量平均值
        Double predictWeight = total / 7 / 1000;

        System.out.println(predictWeight);
        System.out.println(todayWeight);
        // 使用 String.format 格式化保留两位小数
        String predict = String.format("%.2f", predictWeight);
        String actual = String.format("%.2f", todayWeight);
        JSONObject res= new JSONObject();
        res.put("site_name",site_name);
        res.put("predictWeight",predict);
        res.put("actualWeight",actual);

        return res;
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
        List<DumpDataOfSite> res =  dumpRecordMapper.dumpDataOfSite(start, end, site_name);
        DumpDataOfSite total = new DumpDataOfSite();
        total.setSiteName("总计");
        int count = 0;
        for (DumpDataOfSite record : res) {
            count += record.getWeight();
        }
        total.setWeight(count);
        res.add(total);
        return res;
    }

    @Override
    public List<DumpRecordOfSiteByTrans> dumpRecordOfSiteByTrans(String start, String end, String site_name) {
        List<DumpRecordOfSiteByTrans> res =  dumpRecordMapper.dumpRecordOfSiteByTrans(start, end, site_name);
        return res;
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
