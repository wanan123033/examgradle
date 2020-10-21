package com.fairplay.examgradle;

import com.app.layout.LayoutInflaterUtils;
import com.gwm.annotation.layout.Application;
import com.gwm.base.BaseApplication;
import com.gwm.http.HttpClients;
import com.gwm.util.LayoutInflaterUtil;

@Application("app")
public class MyApplication extends BaseApplication {

    @Override
    protected synchronized LayoutInflaterUtil getLayoutUtil() {
        return LayoutInflaterUtils.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
