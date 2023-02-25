package com.example.dump.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.dump.entity.GpsRecord;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class QueryTianFu {
    private static String token = "";
    private static final String secret = "606e87ed-6d0e-4a79-bd61-d710a07de0e0";
    private static final String key = "40b66dec-a664-40ef-bc01-b13ad7094568";
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTianFu.class);

    public static String getToken() {
        if (!isValidToken()) {
            refreshToken();
        }

        return token;
    }

    public static List<GpsRecord> getLatestGPS(List<String> carNumbers) {
        List<GpsRecord> res = new ArrayList<>();
        if (carNumbers != null) {
            String vehicle = "";
            for (int i = 0; i < carNumbers.size(); i++) {
                vehicle += String.format("\"%s\"", carNumbers.get(i));
                if (i < carNumbers.size() - 1) {
                    vehicle += ",";
                }
            }

            try {
                String content = String.format("{\n\"Temp_Token\":\"%s\",\n\"Vehicle\":[%s]\n}\n", getToken(), vehicle);
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(content, mediaType);
                Request request = new Request.Builder()
                        .url("https://chelian.gpskk.com/Data/GZIP/GetLastGPS")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                JSONObject jo = JSON.parseObject(response.body().string());
                JSONArray array = jo.getJSONArray("GpsPack");
                if (array == null) {
                    LOGGER.error("content of http GetLastGPS response: {}", jo);
                } else {
                    for (int i = 0; i < array.size(); i++) {
                        Object item = array.get(i);
                        if (item instanceof JSONObject) {
                            JSONObject gpsPack = (JSONObject) item;
                            JSONObject record = gpsPack.getJSONObject("PACK");
                            GpsRecord gpsRecord = parseRecord(record);
                            String carNumber = (String) gpsPack.get("Vehicle");
                            gpsRecord.setCarNumber(carNumber);
                            res.add(gpsRecord);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("error when query the last GPS: ", e);
            }
        }
        return res;
    }

    public static List<GpsRecord> getGPSTrack(String carNumber, String start, String end) {
        try {
            String content = String.format("{\n\"Temp_Token\":\"%s\",\n\"Vehicle\":\"%s\",\n\"From\":\"%s\",\n\"To\":\"%s\"\n}\n", getToken(), carNumber, start, end);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url("https://chelian.gpskk.com/Data/GZIP/getHistoryData")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jBody = JSON.parseObject(response.body().string());
            Headers headers = response.headers();
            String cookie = "";
            for (int i = 0; i < headers.size(); i++) {
                if ("set-cookie".equals(headers.name(i))) {
                    //System.out.println(headers.name(i));
                    String[] cookies = headers.get(headers.name(i)).split(";");
                    cookie = cookies[0];
                    //System.out.println(cookie);
                }
            }
            return recData(carNumber, (String) jBody.get("Key"), start, end, cookie);
        } catch (Exception e) {
            LOGGER.error("error: carNumber: {}, start: {}, end: {}", carNumber, start, end, e);
            return null;
        }
    }

    private static void refreshToken() {
        try {
            String content = String.format("{\n\"Secret\": \"%s\",\n\"Key\": \"%s\"\n}", secret, key);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url("https://chelian.gpskk.com/Data/GZIP/GetToken")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Cookie", "SERVERID=1ff4d12439fff09fdb55ea4313e5adf5|1668586346|1668586346")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jo = JSON.parseObject(response.body().string());
            JSONObject jsonToken = jo.getJSONObject("Token");
            token = (String) jsonToken.get("Temp_Token");
        } catch (Exception e) {
            LOGGER.error("error: ", e);
        }
    }

    private static Boolean isValidToken() {
        try {
            String content = String.format("{\"Temp_Token\":\"%s\"}", token);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url("https://chelian.gpskk.com/Data/GZIP/IsValidToken")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Cookie", "SERVERID=9b5738b78213ba4752874f30bc478cb6|1668653366|1668653366")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jo = JSON.parseObject(response.body().string());
            String code = (String) jo.get("Code");
            return code.equals("0");
        } catch (Exception e) {
            LOGGER.error("error: ", e);
            return false;
        }
    }

    private static List<GpsRecord> recData(String carNumber, String token, String start, String end, String cookie) {
        try {
            List<GpsRecord> res = new ArrayList<>();

            String content = String.format("\"%s\"", token);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url("https://chelian.gpskk.com/Data/GZIP/RecData")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", cookie)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jo = JSON.parseObject(response.body().string());
            JSONArray array = jo.getJSONArray("PACK");
            if (array == null) {
                LOGGER.warn("content of http recData request: {}", jo);
            }
            for (int i = 0; i < array.size(); i++) {
                Object item = array.get(i);
                if (item instanceof JSONObject) {
                    JSONObject record = (JSONObject) item;
                    GpsRecord gpsRecord = parseRecord(record);

                    gpsRecord.setCarNumber(carNumber);
                    if (i == 0 || locChanged(res.get(res.size() - 1), gpsRecord)) {
                        res.add(gpsRecord);
                    }
                }
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("error: carNumber: {}, start: {}, end: {}, token: {}, cookie: {}", carNumber, start, end, token, cookie, e);
            return null;
        }
    }

    private static GpsRecord parseRecord(JSONObject record) {
        GpsRecord gpsRecord = new GpsRecord();
        Integer loclat = (Integer) record.get("loclatitude");
        BigDecimal lat = BigDecimal.valueOf(loclat).divide(BigDecimal.valueOf(3600000), 6, RoundingMode.HALF_UP);
        Integer locLng = (Integer) record.get("loclongitude");
        BigDecimal lng = BigDecimal.valueOf(locLng).divide(BigDecimal.valueOf(3600000), 6, RoundingMode.HALF_UP);
        gpsRecord.setLatitude(lat.toString());
        gpsRecord.setLongitude(lng.toString());

        // 处理时间戳，返回的时间戳太阴间！
        JSONArray timestamp = record.getJSONArray("gpstm");
        String year = String.format("20%s", timestamp.get(0));
        String month = timestamp.get(1).toString();
        month = month.length() < 2? "0" + month : month;
        String day = timestamp.get(2).toString();
        day = day.length() < 2? "0" + day : day;
        String hour = timestamp.get(3).toString();
        hour = hour.length() < 2? "0" + hour : hour;
        String min = timestamp.get(4).toString();
        min = min.length() < 2? "0" + min : min;
        String sec = timestamp.get(5).toString();
        sec = sec.length() < 2? "0" + sec : sec;
        String date = String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, min, sec);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime exactDate = LocalDateTime.parse(date, df);
        LocalDateTime days = LocalDateTime.parse(date.substring(0, 10) + " 00:00:00", df);
        gpsRecord.setDay(days);
        gpsRecord.setExactDate(exactDate);
        return gpsRecord;
    }

    private static Boolean locChanged(GpsRecord lastRecord, GpsRecord newRecord) {
        BigDecimal newLat = new BigDecimal(newRecord.getLatitude());
        BigDecimal newLng = new BigDecimal(newRecord.getLongitude());
        BigDecimal lastLat = new BigDecimal(lastRecord.getLatitude());
        BigDecimal lastLng = new BigDecimal(lastRecord.getLongitude());
        BigDecimal tolerance = new BigDecimal("0.0001");
        BigDecimal dLat = newLat.subtract(lastLat).abs();
        BigDecimal dLng = newLng.subtract(lastLng).abs();

        try {
            return dLat.compareTo(tolerance) >= 0 || dLng.compareTo(tolerance) >= 0;
        } catch (Exception e) {
            LOGGER.error("error when compare lat and lng: ", e);
            return false;
        }
    }

    public static void main(String[] args) {
        /*
        List<String> cars = new ArrayList<>();
        cars.add("川ADK8628");
        cars.add("川AEK569");
        List<GpsRecord> res = getLatestGPS(cars);
        for (GpsRecord record : res) {
            System.out.println(record.getCarNumber());
        }
         */
        getGPSTrack("川AX1791", "2023-02-08 12:00:00", "2023-02-13 14:00:00");
    }

}
