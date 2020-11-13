package com.fairplay.examgradle.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.fairplay.examgradle.BuildConfig;
import com.fairplay.examgradle.httppresenter.LoginDataPresenter;
import com.gwm.mvvm.BaseViewModel;
import com.orhanobut.logger.utils.LogUtils;


public class SplashViewModel extends BaseViewModel<Object> {
    public static final String LOG_PATH_NAME = "examgradle";
    @Override
    protected void onResume(LifecycleOwner owner) {
        super.onResume(owner);
        init();
        Log.e("TAG","SplashViewModel onResume");
    }
    private void init(){
        LogUtils.initLogger(BuildConfig.DEBUG, BuildConfig.DEBUG, LOG_PATH_NAME);
    }

    public void login(String username, String passsword) {
        LoginDataPresenter dataPresenter = new LoginDataPresenter();
        dataPresenter.setViewModel(this);
        dataPresenter.login(username,passsword);
    }
}
