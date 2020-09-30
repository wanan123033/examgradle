package com.gwm.retrofit2;

import com.blankj.utilcode.util.LogUtils;
import com.gwm.http.HttpObserver;
import com.gwm.retrofit.Observable;

public abstract class BaseDataPresenter<S> {
    private RetrofitOKHttp<S> retrofitOKHttp;
    private Class<S> clazz;
    public BaseDataPresenter(){
        this.clazz = clazz;
        retrofitOKHttp = RetrofitOKHttp.getInstance();
    }
    protected <T> void addHttpSubscriber(Observable<T> observable, Class<T> clazz){
        HttpObserver<T> httpObserver = new HttpObserver<T>() {
            @Override
            public void onError(Exception e, int id) {
                onErrorResult(e,id);
                LogUtils.e(e);
            }

            @Override
            public void onNext(T response, int id) {
                onNextResult(response,id);

            }
        };
        observable = observable.subscriber(httpObserver).subscriber(clazz);
        retrofitOKHttp.runObservable(observable);
    }
    protected void setBaseUrl(String baseUrl) {
        retrofitOKHttp.setBaseurl(baseUrl);
    }
    public S getHttpPresenter(){
        return retrofitOKHttp.getHttpInstance();
    }

    protected abstract  void onNextResult(Object response, int id);

    protected abstract void onErrorResult(Exception e, int id);
}
