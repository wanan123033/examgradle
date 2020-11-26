package com.fairplay.examgradle.contract;

public interface MMKVContract {
    String BASE_URL = "BASE_URL";                 //url地址
    String BASE_URL_NOMAL = "https://api.ncee.fplcloud.com";                 //url地址
    String USERNAME = "USERNAME";                 //用户名
    String PASSWORD = "PASSWORD";                 //密码
    String TOKEN = "TOKEN";
    String LASTUPDATETIME = "LASTUPDATETIME";      //更新时间
    String CURRENT_ITEM = "CURRENT_ITEM";          //当前素质项目
    String CURRENT_SUB_ITEM = "CURRENT_SUB_ITEM";  //当前专项项目
    String EXAMTYPE = "EXAM_TYPE";                 //当前考试类型
    String EXAMPLACENAME = "EXAMPLACENAME";  //场地信息
    String GROUPNO = "GROUPNO";
    String GROUPTYPE = "GROUPTYPE";
    String SORTNAME = "SORTNAME";
    String TRACKNO = "TRACKNO";
    String MQTT_ID = "MQTT_ID";   //学生MQID
    String CHANNEL_CODE = "CHANNEL_CODE";  //通道号
    String MQIP = "MQIP";  //MQ IP
    String MQPORT = "MQPORT";
    String MQUSER = "MQUSER";
    String MQPASS = "MQPASS";
    String PERMISSION = "PERMISSION";
}
