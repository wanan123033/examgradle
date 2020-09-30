package com.gwm.http;

import android.content.Context;
import android.text.TextUtils;

import com.gwm.annotation.http.HTTP;
import com.gwm.base.BaseApplication;
import com.gwm.util.ContextUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HasParamsable;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BaseOkHttp {

    public BaseOkHttp() {
        OkHttpClient okHttpClient = getOkHttpClient(ContextUtil.get());
        OkHttpUtils.initClient(okHttpClient);
    }

    private OkHttpClient getOkHttpClient(Context context) {
        return BaseApplication.getInstance().getHttpClients().getOkHttpClient(context);
    }

    protected <T> void sendHttp(final HttpParams<T> httpParams) {
        if (!httpParams.isRequestBody){
            if (TextUtils.isEmpty(httpParams.json) && httpParams.isJson) {
                Map<String,? extends Object> bundle = httpParams.params;
                Set<String> strings = bundle.keySet();
                JSONObject json = new JSONObject();
                for (String string : strings) {
                    Object value = bundle.get(string);
                    try {
                        json.put(string, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                httpParams.body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json.toString());
            }else if (httpParams.isJson && !TextUtils.isEmpty(httpParams.json)){
                httpParams.body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),httpParams.json);
            }else {
                //通过Query做普通的数据请求
                sendQueryReq(httpParams);
                return;
            }
        }
        Request request = getRequest(httpParams);
        execute(request,httpParams);
    }

    private <T> void sendQueryReq(final HttpParams<T> httpParams) {
        if (httpParams.way == HTTP.WAY.GET) {
            GetBuilder getBuilder = OkHttpUtils.get().url(httpParams.url);
            addParams(getBuilder, httpParams.params);
            if (httpParams.headers != null){
                addHeader(getBuilder,httpParams.headers);
            }
            execute(httpParams, getBuilder);
        } else if (httpParams.way == HTTP.WAY.POST) {
            PostFormBuilder builder = OkHttpUtils.post().url(httpParams.url);
            addParams(builder, httpParams.params);
            if (httpParams.headers != null){
                addHeader(builder,httpParams.headers);
            }
            if (httpParams.files != null){
                addFile(builder,httpParams.files);
            }
            execute(httpParams, builder);
        }else if (httpParams.way == HTTP.WAY.PUT){
            OtherRequestBuilder requestBuilder = OkHttpUtils.put().url(httpParams.url);
            if (httpParams.headers != null){
                addHeader(requestBuilder,httpParams.headers);
            }
            requestBuilder.requestBody(httpParams.body);
            execute(httpParams, requestBuilder);
        }else if (httpParams.way == HTTP.WAY.DELETE){
            OtherRequestBuilder requestBuilder = OkHttpUtils.delete().url(httpParams.url);
            if (httpParams.headers != null){
                addHeader(requestBuilder,httpParams.headers);
            }
            requestBuilder.requestBody(httpParams.body);
            execute(httpParams, requestBuilder);
        }
    }

    private void addHeader(OkHttpRequestBuilder builder, HashMap<String, String> headers) {
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
             builder.addHeader(key,headers.get(key));
        }

    }

    private <T> void execute(Request request, final HttpParams<T> httpParams) {
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new JsonCallBack<T>(httpParams.result) {
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                if (httpParams.observer != null){
                    httpParams.observer.onError(e,0);
                }
            }

            @Override
            public void onResponseNext(Call call, T t) {
                if (httpParams.observer != null){
                    httpParams.observer.onNext(t,0);
                }
            }
        });
    }

    private Request getRequest(HttpParams params) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(params.url);
        if (params.way == HTTP.WAY.DELETE){
            builder = builder.delete(params.body);
        }else if (params.way == HTTP.WAY.POST){
            builder = builder.post(params.body);
        }else if (params.way == HTTP.WAY.PUT){
            builder = builder.put(params.body);
        }
        if (params.headers != null){
            Set<String> keySet = params.headers.keySet();
            for (String key : keySet){
                builder.addHeader(key, (String) params.headers.get(key));
            }
        }
        return builder.build();
    }

    private void addFile(PostFormBuilder builder, Map<String, File> files) {
        Set<String> keySet = files.keySet();
        for (String key : keySet){
            File file = files.get(key);
            builder.addFile(key,file.getName(),file);
        }
    }
    private void addParams(HasParamsable builder, Map<String,? extends Object> params) {
        Set<String> keySet = params.keySet();
        for (String key : keySet){
            builder.addParams(key,params.get(key).toString());
        }
    }

    private <T> void execute(final HttpParams<T> httpParams, OkHttpRequestBuilder build) {
        build.build().execute(new JsonStringCallBack<T>(httpParams.result) {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                if (httpParams.netListener != null) {
                    httpParams.netListener.onAfter(id);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                if (httpParams.netListener != null) {
                    httpParams.netListener.onBefore(request,id);
                }
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                if (httpParams.netListener != null) {
                    httpParams.netListener.inProgress(progress,total,id);
                }
            }

            @Override
            public void onResponseNext(T t) {
                if (httpParams.observer != null) {
                    httpParams.observer.onNext(t, 0);
                }
            }
        });
    }
}
