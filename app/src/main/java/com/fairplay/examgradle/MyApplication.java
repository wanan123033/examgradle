package com.fairplay.examgradle;

import com.app.layout.LayoutInflaterUtils;
import com.fairplay.examgradle.utils.LogToFile;
import com.fairplay.examgradle.viewmodel.ExamResultModel;
import com.feipulai.common.CrashHandler;
import com.gwm.annotation.layout.Application;
import com.gwm.base.BaseApplication;
import com.gwm.http.HttpClients;
import com.gwm.util.LayoutInflaterUtil;
import com.orhanobut.logger.utils.DecryptLogUtils;
import com.orhanobut.logger.utils.LogUtils;

import java.text.SimpleDateFormat;

@Application("app")
public class MyApplication extends BaseApplication {
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static final SimpleDateFormat scoreDateFormat = new SimpleDateFormat();
    public static final String LOG_PATH_NAME = "examgradle";

    @Override
    protected synchronized LayoutInflaterUtil getLayoutUtil() {
        return LayoutInflaterUtils.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        LogUtils.initLogger(BuildConfig.DEBUG, BuildConfig.DEBUG, LOG_PATH_NAME);
    }

}
