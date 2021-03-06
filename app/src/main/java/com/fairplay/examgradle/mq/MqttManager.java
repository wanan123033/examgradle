package com.fairplay.examgradle.mq;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.fairplay.examgradle.mq.interfaces.Imanager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.mq.io.MqttAndroidConnect;
import com.orhanobut.logger.Logger;


/**
 * Created by xiaomo
 * Date on  2019/4/14 16:25.
 *
 * @Desc mqttdemo 长连接接收推送 模块 的启动,功能操作类
 */

public class MqttManager implements Imanager {
    private static final String TAG = "MqttManager";
    public static String APP_NAME = "MyMqttDemo";
    public static String ip;
    public static int port;
    public static Handler mHandler;
    public static Application mApp; //当前应用的Application

    private MqttAndroidConnect mMqttAndroidConnect;
    private static MqttManager mInstance;

    public static MqttManager getInstance() {
        synchronized (MqttManager.class) {
            if (mInstance == null) {
                mInstance = new MqttManager();
            }
        }
        return mInstance;
    }

    private MqttManager() {
        mMqttAndroidConnect = new MqttAndroidConnect();
    }

    /**
     * @param application 当前Application
     */
    public MqttManager init(Application application) {
        mApp = application;
        mHandler = new Handler(application.getMainLooper());
        return mInstance;
    }

    /**
     * @param serverIp 服务端的ip
     * @return
     */
    public MqttManager setServerIp(String serverIp) {
        ip = serverIp;
        return mInstance;
    }

    /**
     * @param serverPort 服务端的port
     * @return
     */
    public MqttManager setServerPort(int serverPort) {
        port = serverPort;
        return mInstance;
    }

    @Override
    public void connect(final Context context) {
        if (mMqttAndroidConnect != null) {
            if (mMqttAndroidConnect.isAlive()) {
                Logger.e("MqttManager connect thread has alive");
                return;
            }
            if (mMqttAndroidConnect.isConnected()) {
                Logger.e("MqttManager has connected");
                return;
            }
        }
        mMqttAndroidConnect = new MqttAndroidConnect();
        mMqttAndroidConnect.start();
    }

    @Override
    public void disConnect() {
        if (mMqttAndroidConnect == null) {
            Logger.e("Wisepush should connect first");
            return;
        }
        mMqttAndroidConnect.disConnect();
        mMqttAndroidConnect = null;
    }

    public void subscribe(String topic){
        if (mMqttAndroidConnect != null)
            mMqttAndroidConnect.subscribe(topic);
    }

    /**
     * 需要订阅的模块的String
     */
    @Override
    public void regeisterServerMsg(OnMqttAndroidConnectListener listener) {
        if (mMqttAndroidConnect != null)
            mMqttAndroidConnect.regeisterServerMsg(listener);
    }

    @Override
    public void unRegeisterServerMsg(OnMqttAndroidConnectListener listener) {
        mMqttAndroidConnect.unRegeisterServerMsg(listener);
    }

    /**
     * @param topic 需要发送的模块的toppic
     */
    @Override
    public void sendMsg(String topic, String message) {
        if (!isConnected()) {
            Toast.makeText(MqttManager.mApp.getApplicationContext(), "还未建立连接", Toast.LENGTH_SHORT).show();
            return;
        }
        mMqttAndroidConnect.sendMsg(topic, message);
    }

    /**
     * 判断是否正在连接
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (mMqttAndroidConnect == null) {
            return false;
        }
        if (mMqttAndroidConnect.isConnected()) {
            return true;
        }
        return false;
    }

}
