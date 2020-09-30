package com.gwm.haware.bluetooth;

import android.app.Application;

import com.gwm.base.BaseApplication;
import com.tencent.mmkv.MMKV;


public final class BlueToothHelper {

    private BlueBindBean blueBindBean;

    private Application application;

    private static BlueToothHelper instances;

    private BlueToothHelper(BaseApplication application) {
        this.application = application;
    }

    public static void init(BaseApplication application) {
        if (instances == null) {
            instances = new BlueToothHelper(application);
        } else {
            instances.blueBindBean = MMKV.defaultMMKV().decodeParcelable("BlueBindBean",BlueBindBean.class);
        }
    }


    public synchronized static BlueBindBean getBlueBind() {
        if (instances == null) {
            return new BlueBindBean();
        }
        if (instances.blueBindBean == null)
            instances.blueBindBean = MMKV.defaultMMKV().decodeParcelable("BlueBindBean",BlueBindBean.class);
        if (instances.blueBindBean == null)
            instances.blueBindBean = new BlueBindBean();
        return instances.blueBindBean;
    }

    /**
     * 修改保存信息
     */
    public static boolean updateBlueBindCache(BlueBindBean blueBindBean) {
        if (blueBindBean == null)
            return false;
        instances.blueBindBean = blueBindBean;
        return MMKV.defaultMMKV().encode("BlueBindBean",blueBindBean);
    }

    /**
     * 清理信息
     */
    public static void clearBlueBindCache() {
        instances.blueBindBean = null;
        MMKV.defaultMMKV().remove("BlueBindBean");
    }
}

