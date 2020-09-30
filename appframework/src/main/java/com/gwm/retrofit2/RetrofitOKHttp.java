package com.gwm.retrofit2;

import com.gwm.http.BaseOkHttp;
import com.gwm.http.HttpParams;
import com.gwm.http.NetWorkParams;
import com.gwm.retrofit.Observable;

public class RetrofitOKHttp<S> extends BaseOkHttp {
    private S instance;
    private String baseurl;
    private static RetrofitOKHttp retrofitOKHttp;
    private RetrofitOKHttp() {
        super();
        try {
            instance = (S) Class.forName("com.app.http.HttpModel_Impl").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized <S> RetrofitOKHttp<S> getInstance(){
        if (retrofitOKHttp == null){
            retrofitOKHttp = new RetrofitOKHttp<S>();
        }
        return retrofitOKHttp;
    }
    public synchronized <E> void runObservable(Observable<E> observable){
        NetWorkParams<E> httpParams = observable.getParams();
        if(httpParams instanceof HttpParams) {
            HttpParams<E> params = (HttpParams<E>) httpParams;
            if(!params.url.startsWith("http") && !params.url.startsWith("ws:")) {
                params.url = baseurl + params.url;
            }
            params.result = observable.getClazz();
            params.netListener = observable.getNetListener();
            params.observer = observable.getObserver();
            params.websocketListener = observable.getListener();
            sendHttp(params);
        }
    }

    public S getHttpInstance() {
        return instance;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
