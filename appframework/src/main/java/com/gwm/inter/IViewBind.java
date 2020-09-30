package com.gwm.inter;

import android.view.View;

public interface IViewBind<T> {
    public void bindView(View view);
    public void bindData(T obj);
}
