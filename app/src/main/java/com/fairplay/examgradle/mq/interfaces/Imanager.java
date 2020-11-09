package com.fairplay.examgradle.mq.interfaces;

import android.content.Context;

/**
 *
 * @Desc 网络管理的基础结构
 */

public interface Imanager {
    void connect(Context context);

    void disConnect();

    void regeisterServerMsg(OnMqttAndroidConnectListener listener);

    void unRegeisterServerMsg(OnMqttAndroidConnectListener listener);

    void sendMsg(String topic, String message);

    boolean isConnected();
}
