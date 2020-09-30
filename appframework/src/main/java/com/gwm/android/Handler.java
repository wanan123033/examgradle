package com.gwm.android;

import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 *
 */
public class Handler extends android.os.Handler {
    private static Handler handler;
    private Handler(Looper mainLooper) {
        super(mainLooper);
    }

    public static synchronized Handler getHandler(){
        if (handler == null){
            handler = new WeakReference<>(new Handler(Looper.getMainLooper())).get();
        }
        return handler;
    }

}
