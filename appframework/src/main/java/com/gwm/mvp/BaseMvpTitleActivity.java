package com.gwm.mvp;

import android.os.Bundle;

import com.gwm.base.BasePresenter;
import com.gwm.base.BaseTitleActivity;
import com.gwm.inter.IViewBind;

public abstract class BaseMvpTitleActivity<V extends IViewBind,P extends BasePresenter> extends BaseTitleActivity<V> implements IBaseView {
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
