package com.gwm.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gwm.base.BaseDialog;
import com.gwm.base.BasePresenter;
import com.gwm.inter.IViewBind;

public abstract class BaseMvpDialog<V extends IViewBind,P extends BasePresenter> extends BaseDialog<V> implements IBaseView {
    protected P presenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        if (presenter != null) {
            presenter.onMvpAttachView(this);
            getLifecycle().addObserver(presenter);
        }
    }
    protected abstract P getPresenter();
}
