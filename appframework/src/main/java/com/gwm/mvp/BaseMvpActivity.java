package com.gwm.mvp;

import android.os.Bundle;

import com.gwm.base.BaseActivity;
import com.gwm.base.BasePresenter;
import com.gwm.inter.IViewBind;

public abstract class BaseMvpActivity<V extends IViewBind,P extends BasePresenter> extends BaseActivity<V> implements IBaseView {
    protected P mPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null){
            mPresenter.onMvpAttachView(this);
            getLifecycle().addObserver(mPresenter);
        }
    }

    public abstract P getPresenter();
}
