package com.fairplay.examgradle.activity;

import com.app.layout.activity_data_mamager;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.DataManagerViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

@Layout(R.layout.activity_data_mamager)
public class DataManagerActivity extends BaseMvvmTitleActivity<Object, DataManagerViewModel, activity_data_mamager> {
    @Override
    protected Class<DataManagerViewModel> getViewModelClass() {
        return DataManagerViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return builder.setTitle("数据管理");
    }
}
