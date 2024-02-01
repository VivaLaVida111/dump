package com.example.dump.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.example.dump.common.ResponseData;
import com.example.dump.common.ResponseDataUtil;
import com.example.dump.common.ResultEnum;
import com.example.dump.dto.*;
import com.example.dump.entity.LoginResult;
import com.example.dump.entity.User;
import com.example.dump.service.AuthService;
//import com.example.dump.service.InfoService;
import com.example.dump.service.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("//auth")
public class AuthController {
    @Resource
    private AuthService authService;
    @Resource
    private InfoService infoService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/register")
    public Boolean register(@RequestBody User userParam) {
        String username = userParam.getName();
        String password = userParam.getPassword();
        String realName = userParam.getRealName();
        if (username == null || password == null || realName == null) {
            return false;
        }
        User user = authService.register(userParam);
        if (user == null) {
            return false;
        }
        return true;
    }

    @PostMapping("/login")
    public ResponseData<Map<String, String>> login(@RequestBody UserLoginParam userLoginParam) {
        SimpleDateFormat format_time = new SimpleDateFormat();
        logger.warn("AuthController requestTime:" + format_time.format(new Date(System.currentTimeMillis())));
        String username = userLoginParam.getName();
        System.out.println("登陆人员账号："+username);
        String password = userLoginParam.getPassword();
        Map<String, String> tokenMap = new HashMap<>();
        if (username == null || password == null) {
            tokenMap.put("error_message", "username or password is null");
            return ResponseDataUtil.Failed(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg() , tokenMap);
        }
        LoginResult res = authService.login(username, password);
        String token = res.getToken();
        Boolean isValidPassword = res.getIsValidPassword();
        if (token == null || isValidPassword == null) {
            tokenMap.put("error_message", "username or password is not correct");
            return ResponseDataUtil.Failed(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg() , tokenMap);
        }
        tokenMap = infoService.getInfo();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("isValidPassword", isValidPassword.toString());
        return ResponseDataUtil.Success(tokenMap);
    }

    @PostMapping("/change_password")
    public Boolean changePassword(@RequestBody String password) {
        if (password == null) {
            return false;
        }
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.changePassword(name, password);
    }

    /**
     * 免密登录 返回token
     * @param code oauth2.0认证重定向到城市管家并返回一个授权码code，用授权码code+令牌access_token获取第三方登录人员的账号userId，即手机号，判断如果城市管家人员里存在该账号则返回token并免密登录系统。
     * @return
     */
    @GetMapping("/getAccessToken")
    public ResponseData<Map<String, String>> getAccessToken(String code) {
        //获取令牌access_token 互联网地址
        //正式环境
        String loginUrl = "https://www.rztcd.com:30443/cgi-bin/gettoken?corpid=wwa1a67ed0008261d9&corpsecret=FAeRegq1Pa2qShhICdDEmi7KsJbD0KD-CalSE17z4qg";
        //测试环境
//        String loginUrl = "https://rztcs.zttr.net:89/cgi-bin/gettoken?corpid=ww9904fd98c1b0df9e&corpsecret=kbwcaURcHk8kIHRP2QPzi7UTN8JayTnBxHdFNOf3uQQ";

        HttpResponse loginRes = HttpRequest.get(loginUrl)
                .execute();
        JSONObject json = new JSONObject();

        String accessToken = new String();
        accessToken = JSONObject.parseObject(loginRes.body()).getString("access_token");


        //令牌和授权码获取第三方登录人员的信息即手机号
        String quesUrl = "https://www.rztcd.com:30443/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
//        String quesUrl = "https://rztcs.zttr.net:89/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
        HttpResponse quesRes = HttpRequest.get(quesUrl)
                .execute();

        String userId = "";
        String errorcode = "";
        String errmsg = "";
        if (JSONObject.parseObject(quesRes.body()).getString("errmsg").equals("ok")) {

            userId = JSONObject.parseObject(quesRes.body()).getString("UserId");
            System.out.println("互联网获取到用户："+userId);
        }else{
            errorcode=JSONObject.parseObject(quesRes.body()).getString("errcode");
            errmsg=JSONObject.parseObject(quesRes.body()).getString("errmsg");
            System.out.println("互联网获取用户信息时报错errcode:"+errorcode);
            System.out.println("互联网获取用户信息时报错errmsg:"+errmsg);

            //电子政务外网

            //获取令牌access_token 互联网地址
            loginUrl = "https://10.1.214.27:30443/cgi-bin/gettoken?corpid=wwa1a67ed0008261d9&corpsecret=FAeRegq1Pa2qShhICdDEmi7KsJbD0KD-CalSE17z4qg";
//            loginUrl = "https://10.1.214.27:89/cgi-bin/gettoken?corpid=ww9904fd98c1b0df9e&corpsecret=kbwcaURcHk8kIHRP2QPzi7UTN8JayTnBxHdFNOf3uQQ";

            loginRes = HttpRequest.get(loginUrl)
                    .execute();

            accessToken = JSONObject.parseObject(loginRes.body()).getString("access_token");


            //令牌和授权码获取第三方登录人员的信息即手机号
            quesUrl = "http://10.1.214.27:30443/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
//            quesUrl = "https://10.1.214.27:89/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
            quesRes = HttpRequest.get(quesUrl)
                    .execute();

            if (JSONObject.parseObject(quesRes.body()).getString("errmsg").equals("ok")) {

                userId = JSONObject.parseObject(quesRes.body()).getString("UserId");
                System.out.println("电子政务获取到用户："+userId);
            }else{
                errorcode=JSONObject.parseObject(quesRes.body()).getString("errcode");
                errmsg=JSONObject.parseObject(quesRes.body()).getString("errmsg");
                System.out.println("电子政务获取用户信息时报错errcode:"+errorcode);
                System.out.println("电子政务获取用户信息时报错errmsg:"+errmsg);


            }
        }

        json.put("userId",userId);
        json.put("access_token",accessToken);
        json.put("code",code);


        //判断该人员是否已存在城市管家的数据库里，如果是则返回token并免密登录城市管家
        LoginResult res = authService.pswFreeLogin(userId);
        String token = res.getToken();
        System.out.println("获取到的token:"+token);
//        Boolean isValidPassword = res.getIsValidPassword();
        Map<String, String> tokenMap = new HashMap<>();
        if (token == null) {
            tokenMap.put("error_message", errmsg);
            return ResponseDataUtil.Failed(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg() , tokenMap);
        }
        System.out.println("登陆人员的账号："+userId);
        System.out.println("登陆人员的access_token："+accessToken);
        System.out.println("登陆人员的code："+code);
        tokenMap = infoService.getInfo();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
//        tokenMap.put("isValidPassword", isValidPassword.toString());
        return ResponseDataUtil.Success(tokenMap);
    }
}
