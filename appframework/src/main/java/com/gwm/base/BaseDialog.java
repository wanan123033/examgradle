package com.gwm.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.messagesendreceive.MessageBus;

import java.lang.ref.SoftReference;


public abstract class BaseDialog<V extends IViewBind> extends DialogFragment implements ViewClick {
    protected V mBinding;
    protected View view;
    protected Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new SoftReference<>(new MyHandler(this)).get();
        MessageBus.getBus().register(this);


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Layout layout = getClass().getAnnotation(Layout.class);
        view = inflate(getContext(),layout.value());
        mBinding = getViewBind();
        mBinding.bindView(view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutEventUtil.getInstance().bind(this,getView());
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }


    public V getViewBind(){
        return BaseApplication.getInstance().getLayoutUtil().getViewBind(getClass().getAnnotation(Layout.class).value());
    }
    private static View inflate(Context context, int resId) {
        View view = LayoutInflater.from(context).inflate(resId, null,false);
        return view;
    }

    public void handlerMessage(Message message){

    }
    private static final class MyHandler extends Handler {
        private SoftReference<? extends BaseDialog> dialog;
        public MyHandler(BaseDialog dialog){
            super(Looper.getMainLooper());
            this.dialog = new SoftReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            dialog.get().handlerMessage(msg);
        }
    }
}
