package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.BaseDataPresenter;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.contract.MMKVContract;
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
        Observable<LoginBean> loginhttp = getHttpPresenter().login(username,password);
        addHttpSubscriber(loginhttp,LoginBean.class);
    }
    @Override
    protected void onNextResult(LoginBean response, int id) {
        MMKV.defaultMMKV().putString(MMKVContract.TOKEN,response.data.token);
        if (getViewModel() instanceof BaseViewModel){
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }
}
