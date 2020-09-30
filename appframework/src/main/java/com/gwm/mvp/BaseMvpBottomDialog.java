package com.gwm.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gwm.base.BaseBottomDialog;
import com.gwm.base.BasePresenter;
import com.gwm.inter.IViewBind;

public abstract class BaseMvpBottomDialog<V extends IViewBind,P extends BasePresenter> extends BaseBottomDialog<V> implements IBaseView {
    protected P presenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        if (presenter != null){
            presenter.onMvpAttachView(this);
            getLifecycle().addObserver(presenter);
        }
    }

    protected abstract P getPresenter();
}
