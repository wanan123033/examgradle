package com.gwm.mvvm;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 采用观察者设计模式访问数据 并监听Activity,Fragment等的生命周期
 */
public class BaseViewModel<T> extends ViewModel<T> implements LifecycleObserver,LifecycleOwner {
    private MutableLiveData<T> liveData;

    public BaseViewModel(){
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<T> getLiveData() {
        return liveData;
    }

    /**
     * 发送一个LiveData到自己ViewModel上
     * @param liveData  liveData数据
     */
    public void sendLiveData(LiveData<T> liveData) {
        postValue(liveData);
    }
    /**
     * 发送一个LiveData到自己ViewModel上
     * @param liveData  liveData数据
     */
    public void sendLiveData(T liveData) {
        postValue(liveData);
    }

    protected void setValue(T data){
        liveData.setValue(data);
    }

    protected void postValue(T data){
        liveData.postValue(data);
    }

    protected void postValue(LiveData<T> data){
        data.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.postValue(t);
            }
        });
    }
    protected void setValue(LiveData<T> data){
        data.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.setValue(t);
            }
        });
    }
}
