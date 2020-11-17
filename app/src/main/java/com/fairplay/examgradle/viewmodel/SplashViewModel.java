package com.fairplay.examgradle.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.fairplay.examgradle.BuildConfig;
import com.fairplay.examgradle.httppresenter.LoginDataPresenter;
import com.gwm.mvvm.BaseViewModel;
import com.orhanobut.logger.utils.LogUtils;


public class SplashViewModel extends BaseViewModel<Object> {

    @Override
    protected void onResume(LifecycleOwner owner) {
        super.onResume(owner);
        init();
    }
    private void init(){

    }

    public void login(String username, String passsword) {
        LoginDataPresenter dataPresenter = new LoginDataPresenter();
        dataPresenter.setViewModel(this);
        dataPresenter.login(username,passsword);
    }
}
