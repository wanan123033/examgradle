package com.gwm.mvvm;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gwm.base.BaseTitleActivity;
import com.gwm.inter.IViewBind;

public abstract class BaseMvvmTitleActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseTitleActivity<V> implements Observer<M> ,IViewModelProvider{
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
    public void onChanged(M o) {
        try {
            int i = Integer.parseInt(o.toString());
            if (i == DIMMSION_PROGREESS){
                dismissDialog();
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
}
