package com.gwm.http;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.gwm.util.EncryptUtil;
import com.orhanobut.logger.Logger;

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

    /**
     * {"code":0,"msg":null,"data":"","sign":"1da81191bbvlifkp5u889e829263b677c18b940a71c44e9bc9","encrypt":1,"responseTime":"1603765801926"}
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try{
            String string = response.body().string();
            HttpResult<Object> httpResult = GsonUtils.fromJson(string, HttpResult.class);
            if (httpResult.encrypt == HttpResult.ENCRYPT_TRUE && !TextUtils.isEmpty(httpResult.data.toString())){
                String decodeBody = EncryptUtil.getInstance().decodeHttpData(httpResult.data.toString(),httpResult.sign);
                JsonParser jsonParser = new JsonParser();
                httpResult.data = jsonParser.parse(decodeBody);
                string = GsonUtils.toJson(httpResult);
            }
            Log.e("TAG===>","解密后:"+string);
            T t = GsonUtils.fromJson(string,result);
            onResponseNext(call,t);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public abstract void onResponseNext(Call call,T t);
}
