package com.fairplay.examgradle.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.gwm.base.BaseApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by pengjf on 2018/10/16.
 * 深圳市菲普莱体育发展有限公司   秘密级别:绝密
 */

public class CommonUtils {

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context context) {

        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    @SuppressLint({"WifiManagerLeak", "MissingPermission"})
    public static String getDeviceInfo() {
        TelephonyManager phone = (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);

        return wifi.getConnectionInfo().getMacAddress() + "," + phone.getDeviceId() + "," + getCpuName() + "," + phone.getNetworkOperator();
    }

    /**
     * 获取CPU型号
     *
     * @return
     */
    private static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }


}
