package com.gwm.base;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gwm.http.HttpObserver;
import com.gwm.http.HttpParams;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.retrofit.Observable;
import com.gwm.retrofit.RetrofitOKHttp;

import okhttp3.Request;

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
    protected void addHttpSubscriber(String message,final Observable<D> observable,Class<D> clazz){
        observable.setMessage(message);
        observable.subscriber(new HttpParams.HttpNetListener() {
            @Override
            public void onBefore(Request request, int id) {
                if (!TextUtils.isEmpty(observable.getMessage().toString())) {
                    MessageBus.getBus().post(new MessageBusMessage(observable.getMessage(), "SHOW_PROGRESS"));
                }
            }

            @Override
            public void onAfter(int id) {
                if (!TextUtils.isEmpty(observable.getMessage().toString())) {
                    MessageBus.getBus().post(new MessageBusMessage("","DIMMSION_PROGREESS"));
                }
            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }
        });
        observable.subscriber(this).subscriber(clazz);
        retrofitOKHttp.runObservable(observable);
    }

    @Override
    public void onError(Exception e, int id) {
        onErrorResult(e,id);

//        LogUtils.e(e);
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
