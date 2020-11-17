package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.httppresenter.DownItemInfoPresenter;
import com.fairplay.examgradle.httppresenter.LoginDataPresenter;
import com.gwm.mvvm.BaseViewModel;

public class LoginViewModel extends BaseViewModel<Object> {
    public void login(String username,String password){
        //登录用户
        LoginDataPresenter dataPresenter = new LoginDataPresenter();
        dataPresenter.setViewModel(this);
        dataPresenter.login(username,password);

        //下载项目信息
        DownItemInfoPresenter presenter = new DownItemInfoPresenter();
        presenter.setViewModel(this);
        presenter.downItem();
    }
}
