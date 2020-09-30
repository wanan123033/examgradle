package com.gwm.base;

import com.blankj.utilcode.util.LogUtils;
import com.gwm.http.HttpObserver;
import com.gwm.retrofit.Observable;
import com.gwm.retrofit.RetrofitOKHttp;

/**
 * Created by Administrator on 2018/1/25.
 *
 * Http 请求基类(动态代理模式)
 */
public abstract class BaseDataPresenter<S,D> implements HttpObserver<D>{
    private RetrofitOKHttp<S> retrofitOKHttp;
    private Class<S> clazz;
    public BaseDataPresenter(Class<S> clazz){
        this.clazz = clazz;
        retrofitOKHttp = RetrofitOKHttp.getInstance(clazz);
    }
    protected void addHttpSubscriber(Observable<D> observable,Class<D> clazz){
        observable = observable.subscriber(this).subscriber(clazz);
        retrofitOKHttp.runObservable(observable);
    }

    @Override
    public void onError(Exception e, int id) {
        onErrorResult(e,id);
        LogUtils.e(e);
    }

    @Override
    public void onNext(D response, int id) {
        onNextResult(response,id);
    }
    protected void setBaseUrl(String baseUrl) {
        retrofitOKHttp.setBaseUrl(baseUrl);
    }
    public S getHttpPresenter(){
        return retrofitOKHttp.getHttpInstance(clazz);
    }
    protected abstract  void onNextResult(D response, int id);

    protected abstract void onErrorResult(Exception e, int id);
}
