package com.gwm.http;

import com.blankj.utilcode.util.GsonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class JsonCallBack<T> implements Callback {
    private Class<T> result;

    public JsonCallBack(Class<T> result) {
        this.result = result;
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        T t = GsonUtils.fromJson(response.body().string(), result);//转成对象
        onResponseNext(call,t);

    }
    public abstract void onResponseNext(Call call,T t);
}
