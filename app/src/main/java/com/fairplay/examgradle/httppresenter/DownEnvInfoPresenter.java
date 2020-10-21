package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.EnvInfoBean;
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
        String jsonString = genJsonString(100020110, null);
        Observable<EnvInfoBean> downEnvInfo = getHttpPresenter().downEnvInfo(getToken(), jsonString);
        addHttpSubscriber(downEnvInfo,EnvInfoBean.class);
    }

    @Override
    protected void onNextResult(EnvInfoBean response, int id) {
        if (getViewModel() instanceof BaseViewModel){
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    public interface DownEnvInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
