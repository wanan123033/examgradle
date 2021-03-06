package com.gwm.http;

/**
 * MVC模式中Service跟V(视图层)通讯的回调接口
 * @author gwm
 */
public interface HttpObserver<T> {

    void onError(Exception e, int id);

    void onNext(T response, int id);
}