package com.fairplay.examgradle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class EnvInfoBean extends BaseBean<EnvInfoBean.EnvInfo>{

    public static class EnvInfo{
        public MqttEnv mq;

        @Override
        public String toString() {
            return "EnvInfo{" +
                    "mq=" + mq +
                    '}';
        }
    }
    public static class MqttEnv{
        public String ip;                //IP
        public String port;              //port
        public String username;          //MQTT用户名
        public String password;          //MQTT密码
        public String recvTopic;         //接收频道(topic)
        public String sendTopic;         //发送频道(topic)

        @Override
        public String toString() {
            return "MqttEnv{" +
                    "ip='" + ip + '\'' +
                    ", port='" + port + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", recvTopic='" + recvTopic + '\'' +
                    ", sendTopic='" + sendTopic + '\'' +
                    '}';
        }
    }
}
