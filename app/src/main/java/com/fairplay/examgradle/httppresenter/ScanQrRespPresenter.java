package com.fairplay.examgradle.httppresenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.activity.MainActivity;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.bean.TopicBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.contract.OnScanListener;
import com.fairplay.examgradle.mq.MqttManager;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

public class ScanQrRespPresenter extends JsonDataPresenter<ScanQrRespPresenter.ScanQrRespInfo, TopicBean> {
    private String channelCode;

    public ScanQrRespPresenter() {
        super(ScanQrRespInfo.class);
    }

    public void addTopic(String channelCode){
        this.channelCode = channelCode;
        String getJson = genJsonString(100020180,getJsonCreator().channelCode(channelCode));
        String token = getToken();
        Observable<TopicBean> addTopic = getHttpPresenter().scanQrResp(token,getJson);
        addHttpSubscriber("加入裁判组...",addTopic, TopicBean.class);
    }

    @Override
    protected void onNextResult(TopicBean response, int id) {
        try {
            if (response.data == 0){
                ToastUtils.showShort("通道组加入成功");
                BaseApplication.getInstance().getMmkv().putString(MMKVContract.CHANNEL_CODE,channelCode);
                MqttManager.getInstance().subscribe(channelCode);
                DownItemInfoPresenter itemInfoPresenter = new DownItemInfoPresenter();
                itemInfoPresenter.setViewModel(getViewModel());
                itemInfoPresenter.downItem();
            }else {
                ToastUtils.showShort(response.msg);
            }
        }catch (Exception e){
            ToastUtils.showShort("通道组加入失败");
        }

    }

    public interface ScanQrRespInfo extends JsonDataPresenter.HttpBaseBean{
        @JSON
        String channelCode(@Param("channelCode")String channelCode);
    }
}
