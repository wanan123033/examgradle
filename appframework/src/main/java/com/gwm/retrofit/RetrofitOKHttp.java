package com.gwm.retrofit;

import com.gwm.http.BaseOkHttp;
import com.gwm.http.HttpParams;
import com.gwm.http.NetWorkParams;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 * 模仿Retrofit框架的工具类
 */

public class RetrofitOKHttp<T> extends BaseOkHttp{
    private T instance;
    private HttpHandler handler;

    private static RetrofitOKHttp http;
    private String baseurl;

    private RetrofitOKHttp(Class<T> clazz){
        super();
        //使用动态代理生成部分代码
        handler = new HttpHandler();
        instance = (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
    }

    public T getHttpInstance(Class<T> clazz){
        if (handler == null)
            handler = new HttpHandler();
        if (instance == null)
            instance = (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
        return instance;
    }
    /**
     * 执行Http请求(动态代理模式,已测试)
     * @param observable http请求的参数名称  模仿RxJava跟Retrofit的写法
     */

    public synchronized <E> void runObservable(Observable<E> observable){
        List<String> methods = handler.methods();
        for(String method : methods){
            NetWorkParams<E> httpParams = handler.getHttpParams(method);
            if(httpParams instanceof HttpParams) {
                HttpParams<E> params = (HttpParams<E>) httpParams;
                if(!params.url.startsWith("http") && !params.url.startsWith("ws:")) {
                    params.url = baseurl + params.url;
                }
                ((HttpParams<E>) httpParams).secretKey = observable.secretKey();
                params.result = observable.getClazz();
                Observable observable1 = handler.getObservable(method);
                params.observer = observable1.getObserver();
                params.websocketListener = observable1.getListener();
                params.netListener = observable1.getNetListener();
                if (observable1 == observable) {    //判断执行的请求是不是参数对应的请求
                    sendHttp(params);
                    break;
                }
            }
        }
        handler.methods().clear();
    }

    public static synchronized <S> RetrofitOKHttp<S> getInstance(Class<S> clazz){
        if(http == null){
            http = new RetrofitOKHttp<S>(clazz);
        }
        return http;
    }
    public void setBaseUrl(String url){
        this.baseurl = url;
    }
}