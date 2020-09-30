package com.gwm.mvvm;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gwm.base.BaseDialog;
import com.gwm.inter.IViewBind;

public abstract class BaseMvvmDialog<M,V extends IViewBind,VM extends ViewModel<M>> extends BaseDialog<V> implements Observer<M>,IViewModelProvider {
    protected VM viewModel;
    private ViewModelProvider viewModelProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(getViewModelClass());
        viewModel.getLiveData().observe(this,this);
        getLifecycle().addObserver(viewModel);
    }

    @Override
    public ViewModelProvider getViewModelProvider() {
        return viewModelProvider;
    }

    protected abstract Class<VM> getViewModelClass();

    @Override
    public void onChanged(M m) {

    }
}
