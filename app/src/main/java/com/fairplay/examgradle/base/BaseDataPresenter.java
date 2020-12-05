package com.fairplay.examgradle.base;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.contract.IHttp;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.base.BaseApplication;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.mvvm.ViewModel;

public abstract class BaseDataPresenter<D> extends com.gwm.base.BaseDataPresenter<IHttp,D> {
    private ViewModel viewModel;
    public BaseDataPresenter() {
        super(IHttp.class);
        String baseUrl = BaseApplication.getInstance().getMmkv().getString(MMKVContract.BASE_URL,MMKVContract.BASE_URL_NOMAL);
        setBaseUrl(baseUrl);
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected synchronized void onErrorResult(Exception e, int id) {
        ToastUtils.showShort(e.getMessage());
        MessageBus.getBus().post(new MessageBusMessage("","DIMMSION_PROGREESS"));
        e.printStackTrace();
//        if(getViewModel() != null);
//            ((BaseViewModel)getViewModel()).sendLiveData(BaseActivity.DIMMSION_PROGREESS);
    }
}
