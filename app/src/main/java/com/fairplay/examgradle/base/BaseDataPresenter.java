package com.fairplay.examgradle.base;

import com.fairplay.examgradle.contract.IHttp;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.http.HttpParams;
import com.gwm.mvvm.ViewModel;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

import okhttp3.Request;

public abstract class BaseDataPresenter<D> extends com.gwm.base.BaseDataPresenter<IHttp,D> {
    private ViewModel viewModel;
    public BaseDataPresenter() {
        super(IHttp.class);
        String baseUrl = MMKV.defaultMMKV().getString(MMKVContract.BASE_URL,"");
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
}
