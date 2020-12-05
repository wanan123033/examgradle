package com.fairplay.examgradle.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return getIMEIDeviceId(context);
        }
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
    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
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
