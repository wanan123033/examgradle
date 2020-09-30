package com.gwm.base;

import androidx.lifecycle.LifecycleOwner;

import com.gwm.android.Handler;
import com.gwm.mvp.IBasePresenter;
import com.gwm.mvp.IBaseView;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends IBaseView> implements IBasePresenter {
    WeakReference<V> viewRef;
    protected static Handler handler;
    public void onMvpAttachView(V view){

        viewRef = new WeakReference<V>(view);
        if (handler == null)
            handler = Handler.getHandler();
    }

    public V getView(){
        return viewRef.get();
    }

    public void runOnUIThread(Runnable runnable){
        handler.post(runnable);
    }

    @Override
    public void onAny(LifecycleOwner owner) {

    }

    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onDestory(LifecycleOwner owner) {
        viewRef.clear();
        viewRef = null;
    }
}
