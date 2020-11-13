package com.fairplay.examgradle.httppresenter;

import android.content.Intent;

import com.fairplay.examgradle.base.BaseDataPresenter;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

public class LoginDataPresenter extends BaseDataPresenter<LoginBean> {

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username,String password){
        String token = "Basic dGVybWluYWw6dGVybWluYWxfc2VjcmV0";
        Observable<LoginBean> loginhttp = getHttpPresenter().login(token,username,password);
        addHttpSubscriber(loginhttp,LoginBean.class);
    }
    @Override
    protected void onNextResult(LoginBean response, int id) {
        System.out.println(response+"----25");
        if (response.code == 0) {
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.TOKEN, response.data.token);
            DownEnvInfoPresenter envInfoPresenter = new DownEnvInfoPresenter();
            envInfoPresenter.setViewModel(getViewModel());
            envInfoPresenter.downEnv();
        }else {
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }
}
