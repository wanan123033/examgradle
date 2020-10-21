package com.fairplay.examgradle.bean;

public class EnvInfoBean extends BaseBean<EnvInfoBean.EnvInfo>{
    public static class EnvInfo{
        public MqttEnv mq;
    }
    public static class MqttEnv{
        public String ip;                //IP
        public String port;              //port
        public String username;          //MQTT用户名
        public String password;          //MQTT密码
        public String recvTopic;         //接收频道(topic)
        public String sendTopic;         //发送频道(topic)
    }
}
