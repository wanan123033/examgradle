package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.httppresenter.ScanQrRespPresenter;
import com.gwm.mvvm.BaseViewModel;

public class MainViewModel extends BaseViewModel<Object> {
    public void scanQr(String channelCode) {
        ScanQrRespPresenter presenter = new ScanQrRespPresenter();
        presenter.setViewModel(this);
        presenter.addTopic(channelCode);
    }
}
