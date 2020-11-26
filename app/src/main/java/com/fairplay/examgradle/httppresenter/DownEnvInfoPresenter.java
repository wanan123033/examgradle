package com.fairplay.examgradle.httppresenter;

import android.util.Log;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

public class DownEnvInfoPresenter extends JsonDataPresenter<DownEnvInfoPresenter.DownEnvInfo, EnvInfoBean> {
    public DownEnvInfoPresenter() {
        super(DownEnvInfo.class);
    }

    /**
     * 加载MQTT配置信息
     */
    public void downEnv(){
        String jsonString = genJsonString(100020110, "");
        Observable<EnvInfoBean> downEnvInfo = getHttpPresenter().downEnvInfo(getToken(), jsonString);
        addHttpSubscriber("加载配置信息...",downEnvInfo,EnvInfoBean.class);
    }

    @Override
    protected void onNextResult(EnvInfoBean response, int id) {
        if (response.code >= 0){
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
        Log.e("TAG=====",response.toString());
        if (getViewModel() instanceof BaseViewModel){
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.MQIP,response.data.mq.ip);
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.MQPORT,response.data.mq.port);
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.MQUSER,response.data.mq.username);
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.MQPASS,response.data.mq.password);
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }
    public interface DownEnvInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
