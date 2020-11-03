package com.fairplay.examgradle.mq.bean;


import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;

/**
 *
 * @Desc 每个注册事件的回调
 */

public class MqttListenerData {
    private String event;
    private OnMqttAndroidConnectListener listener;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public OnMqttAndroidConnectListener getListener() {
        return listener;
    }

    public void setListener(OnMqttAndroidConnectListener listener) {
        this.listener = listener;
    }
}
