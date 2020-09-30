package com.gwm.mvvm;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gwm.base.BaseActivity;
import com.gwm.inter.IViewBind;
import com.gwm.util.DataBindUtil;

public abstract class BaseMvvmActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseActivity<V> implements Observer<M>,IViewModelProvider {
    protected VM viewModel;
    private ViewModelProvider viewModelProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(getViewModelClass());
        viewModel.getLiveData().observe(this,this);
        getLifecycle().addObserver(viewModel);
        DataBindUtil.cache(this);
    }

    public ViewModelProvider getViewModelProvider(){
        return viewModelProvider;
    }

    protected abstract Class<VM> getViewModelClass();

    @Override
    public void onChanged(M o) {
        if (o != null)
            DataBindUtil.bind(getView(),o);
    }
}
