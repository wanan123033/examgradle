package com.fairplay.examgradle.base;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.contract.IHttp;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.gwm.http.HttpParams;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.mvvm.ViewModel;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

import okhttp3.Request;

public abstract class BaseDataPresenter<D> extends com.gwm.base.BaseDataPresenter<IHttp,D> {
    private ViewModel viewModel;
    public BaseDataPresenter() {
        super(IHttp.class);
        String baseUrl = BaseApplication.getInstance().getMmkv().getString(MMKVContract.BASE_URL,"");
        setBaseUrl(baseUrl);
    }
    public void addHttpSubscriber(Observable<D> observable,Class<D> clazz){
        observable.subscriber(new HttpParams.HttpNetListener() {
            @Override
            public void onBefore(Request request, int id) {

            }

            @Override
            public void onAfter(int id) {

            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }
        });
        super.addHttpSubscriber(observable,clazz);
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void onErrorResult(Exception e, int id) {
        ToastUtils.showShort(e.getMessage());
        e.printStackTrace();
        if(getViewModel() != null);
            ((BaseViewModel)getViewModel()).sendLiveData(BaseActivity.DIMMSION_PROGREESS);
    }
}
