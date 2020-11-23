package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.httppresenter.LoginDataPresenter;
import com.gwm.mvvm.BaseViewModel;


public class SplashViewModel extends BaseViewModel<Object> {

    public void login(String username, String passsword) {
        LoginDataPresenter dataPresenter = new LoginDataPresenter();
        dataPresenter.setViewModel(this);
        dataPresenter.login(username,passsword);
    }
}
