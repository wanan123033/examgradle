package com.fairplay.examgradle.mqtt;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.fairplay.examgradle.activity.ExamActivity;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.mqtt.interfaces.OnMqttAndroidConnectListener;

public class MqttService {
    private Application application;
    private static MqttService mqttService;
    private OnMqttAndroidConnectListener listener = new OnMqttAndroidConnectListener() {
        @Override
        public void onDataReceive(String message) {
            Log.e("TAG=====>",message);
            Intent intent = new Intent(ExamActivity.ACTION);
            intent.putExtra("message",message);
            application.sendBroadcast(intent);
        }
    };

    private MqttService(Application application){
        this.application = application;
    }

    public static synchronized MqttService getInstance(Application application){
        if (mqttService == null){
            mqttService = new MqttService(application);
        }
        mqttService.application = application;
        return mqttService;
    }
    public void start(EnvInfoBean o){
        if(!MqttManager.getInstance().isConnected()){
            MqttManager.getInstance().init(application)
                    .setServerIp(o.data.mq.ip)
                    .setServerPort(Integer.parseInt(o.data.mq.port))
                    .connect();
            MqttManager.getInstance().regeisterServerMsg(listener);
        }
    }
}
