package com.feipulai.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class SystemBrightUtils {

    // 判断是否开启了自动亮度调节
    public static boolean isAutoBrightness(Activity activity) {
        boolean autoBrightness = false;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            autoBrightness = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return autoBrightness;
    }

    // 获取当前系统亮度值
    public static int getBrightness(Activity activity) {
        int brightValue = 0;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightValue;
    }

    /**
     * 改变屏幕亮度
     *
     * @param brightValue -1-255,如果<0，则表示自动设置的亮度
     */
    public static void setBrightness(Activity activity, int brightValue) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = (brightValue < 0 ? -1.0f : brightValue / 255f);
        activity.getWindow().setAttributes(lp);
    }

    public static void setSystemBrightness(Activity activity, int value) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, value);
    }

    // 开启亮度自动亮度模式
    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Uri uri = Settings.System.getUriFor("screen_brightness");
        activity.getContentResolver().notifyChange(uri, null);
    }

    // 停止自动亮度模式
    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Uri uri = Settings.System.getUriFor("screen_brightness");
        activity.getContentResolver().notifyChange(uri, null);
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static void setBrightnessMode(Activity activity, int brightMode) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightMode);
    }

    /**
     * 获取当前版本号
     *
     * @param context 上下文对象
     */
    public static String getCurrentVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String version = null;
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
