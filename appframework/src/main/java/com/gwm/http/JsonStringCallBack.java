package com.gwm.http;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/5/23 0023.
 */

public abstract class JsonStringCallBack<T> extends StringCallback {
    private Class<T> clazz;

    public JsonStringCallBack(Class<T> clazz){
        this.clazz = clazz;
    }
    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {
        try {
            T t = GsonUtils.fromJson(response, clazz);//转成对象
            onResponseNext(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public abstract void onResponseNext(T t);
}
