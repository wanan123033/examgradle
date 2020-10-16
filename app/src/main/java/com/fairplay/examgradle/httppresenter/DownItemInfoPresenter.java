package com.fairplay.examgradle.httppresenter;


import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

public class DownItemInfoPresenter extends JsonDataPresenter<DownItemInfoPresenter.DownItemInfo,ItemInfoBean> {
    public DownItemInfoPresenter() {
        super(DownItemInfo.class);
    }

    @Override
    protected void onNextResult(ItemInfoBean response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    /**
     * 下载考点项目
     */
    public void downItem() {
        String json = genJsonString(100020111,null);
        String token =  "Bearer "+MMKV.defaultMMKV().getString(MMKVContract.TOKEN,"");
        Observable<ItemInfoBean> itemInfoBeanObservable = getHttpPresenter().downItemInfo(token, json);
        addHttpSubscriber(itemInfoBeanObservable,ItemInfoBean.class);
    }

    public interface DownItemInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
