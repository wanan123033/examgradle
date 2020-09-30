package com.fairplay.examgradle.httppresenter;

import androidx.lifecycle.MutableLiveData;

import com.fairplay.examgradle.base.BaseDataPresenter;
import com.fairplay.examgradle.bean.LoginBean;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

public class LoginDataPresenter extends BaseDataPresenter<LoginBean> {

    public void login(String username,String password){
        Observable<LoginBean> loginhttp = getHttpPresenter().login(username,password);
        addHttpSubscriber(loginhttp,LoginBean.class);
    }
    @Override
    protected void onNextResult(LoginBean response, int id) {
        if (getViewModel() instanceof BaseViewModel){
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }
}
