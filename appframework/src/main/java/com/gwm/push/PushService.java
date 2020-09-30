package com.gwm.push;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Request;
public abstract class PushService extends IntentService{
    public static final String PUSH_URL = "push_url";

    public static Context mContext;

    public PushService() {
        super("PushService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra(PUSH_URL);
        WebSocketListener socketListener = getWebSocketListener();
        new WebSocket(url,socketListener).run();
    }

    public abstract WebSocketListener getWebSocketListener();

    static class WebSocket{
        private WebSocketListener webSocketListener;
        private String url;

        public WebSocket(String url, WebSocketListener webSocketListener){
            this.url = url;
            this.webSocketListener = webSocketListener;
        }
        public void run() {
            Request request = new Request.Builder().url(url).build();
            OkHttpUtils.getInstance().getOkHttpClient().newWebSocket(request, webSocketListener);
        }
    }
}
