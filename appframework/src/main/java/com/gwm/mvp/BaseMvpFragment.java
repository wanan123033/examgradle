package com.gwm.mvp;

import android.os.Bundle;

import com.gwm.base.BaseFragment;
import com.gwm.base.BasePresenter;
import com.gwm.inter.IViewBind;

public abstract class BaseMvpFragment<V extends IViewBind,P extends BasePresenter> extends BaseFragment<V> implements IBaseView{
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

    protected abstract P getPresenter();
}
