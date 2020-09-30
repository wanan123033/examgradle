package com.gwm.http;

import android.content.Context;

import okhttp3.OkHttpClient;

public interface HttpClients {

    OkHttpClient getOkHttpClient(Context context);
}
