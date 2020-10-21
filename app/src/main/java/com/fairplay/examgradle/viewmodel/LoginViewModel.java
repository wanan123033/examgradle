package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.httppresenter.LoginDataPresenter;
import com.gwm.mvvm.BaseViewModel;

public class LoginViewModel extends BaseViewModel<EnvInfoBean> {
    public void login(String username,String password){
        LoginDataPresenter dataPresenter = new LoginDataPresenter();
        dataPresenter.setViewModel(this);
        dataPresenter.login(username,password);
    }
}
