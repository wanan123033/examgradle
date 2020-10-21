package com.fairplay.examgradle.viewmodel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.fairplay.examgradle.BuildConfig;
import com.fairplay.examgradle.utils.ToastUtils;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.util.ContextUtil;
import com.orhanobut.logger.utils.LogUtils;


public class SplashViewModel extends BaseViewModel<Object> {
    public static final String LOG_PATH_NAME = "examgradle";
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    @Override
    protected void onResume(LifecycleOwner owner) {
        super.onResume(owner);
        init();
        Log.e("TAG","SplashViewModel onResume");
    }
    private void init(){
        LogUtils.initLogger(BuildConfig.DEBUG, BuildConfig.DEBUG, LOG_PATH_NAME);
        ToastUtils.init(ContextUtil.get());
    }
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(ContextUtil.get(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }
}
