package com.gwm.http;

import com.gwm.annotation.http.HTTP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.WebSocketListener;

/**
 * Created by John on 2016/4/14.
 */
public class HttpParams<Result> extends NetWorkParams<Result>{
    public static final int WEB_SOCKET = 5;
    public WebSocketListener websocketListener;

    public Map<String,File> files;  //HTTP 文件上传字段


    public HTTP.WAY way;  //请求方式

    public HttpNetListener netListener;  //网络请求对话框
    public RequestBody body;
    public boolean isRequestBody;
    public String json;
    public boolean isJson;
    public HashMap<String,String> headers;

    public interface HttpNetListener{
        /**
         * UI Thread
         *
         * @param request
         */
        void onBefore(Request request, int id);

        /**
         * UI Thread
         *
         * @param
         */
        void onAfter(int id);

        /**
         * UI Thread
         *
         * @param progress
         */
        void inProgress(float progress, long total, int id);
    }

    @Override
    public String toString() {
        return "HttpParams{" +
                "url='" + url + '\'' +
                ", params=" + params +
                '}';
    }
}
