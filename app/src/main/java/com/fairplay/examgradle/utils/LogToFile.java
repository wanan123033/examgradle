package com.fairplay.examgradle.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.fairplay.examgradle.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogToFile extends HandlerThread {
    private static Handler mHandler;
    private static LogToFile mLogToFile;
    private static final String LOG_THREAD = "LOG_THREAD";
    private static final String TAG = "LogToFile";
 
    private LogToFile(String name) {
        super(name);
    }
    //在application初始化 LogToFile.init();
    public static void init() {
        if (mLogToFile == null) {
            mLogToFile = new LogToFile(LOG_THREAD);
            mLogToFile.start();
            mHandler = new Handler(mLogToFile.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg != null) {//子线程执行保存文件操作
                        Bundle data = msg.getData();
                        String tag = data.getString("TAG", "tag is null");
                        String message = data.getString("MESSAGE", "message is null");
                        saveLogFile(tag, message);
                    }
                }
            };
        }
    }
    //在程序退出时调用
    public static void deInit() {
        if (mLogToFile != null) {
            mHandler.removeCallbacksAndMessages(null);
            mLogToFile.quit();
            mLogToFile = null;
            mHandler=null;
        }
    }
 
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
        toChildThread(tag, msg);
    }
 
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
        toChildThread(tag, msg);
    }
 
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
        toChildThread(TAG, msg);
    }
 
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
        toChildThread(tag, msg);
    }
 
    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
        toChildThread(TAG, msg);
    }
 
 
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
        toChildThread(tag, msg);
    }
 
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
        toChildThread(tag, msg);
    }
 
    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
        toChildThread(TAG, msg);
    }
    
    private static void toChildThread(String tag, String message) {
 
        if (!BuildConfig.DEBUG) {//release版本不保存
            return;
        }
 
        if (mHandler != null && !TextUtils.isEmpty(message)) {
            Message obtain = Message.obtain();
            Bundle bundle = new Bundle();
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            bundle.putString("TAG", tag);
            bundle.putString("MESSAGE", message);
            obtain.setData(bundle);
            mHandler.sendMessage(obtain);
        }
    }
 
    private static final int LOG_FILE_SIZE_MAX = 20 * 1024 * 1024;
    private static final String PATH = "/sdcard/";
    private static final String NAME = "MyLog.txt";
    private static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
 
    private static void saveLogFile(String tag, String message) {//
 
        if (TextUtils.isEmpty(tag)) {
            tag = "--Tag null--";
        }
 
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(PATH, NAME);
            if (file == null || !file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file, true);
 
            long size = fileOutputStream.getChannel().size();
//            if (size > LOG_FILE_SIZE_MAX) {//超过20m，删除
//                fileOutputStream.close();
//                file.delete();
//                return;
//            }
            String result = DateFormat.format(new Date()) + "/" + tag + "=====>>[" + message + "]" + "\n";
            fileOutputStream.write(result.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}