package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.layout.activity_data_mamager;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.OperationAdapter;
import com.fairplay.examgradle.bean.OperationBean;
import com.fairplay.examgradle.viewmodel.DataManagerViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.rv_operation.setLayoutManager(new StaggeredGridLayoutManager(StaggeredGridLayoutManager.VERTICAL,3));
        List<OperationBean> operationBeans = new ArrayList<>();
        String[] typeName = getResources().getStringArray(R.array.data_admin);
        int[] resIds = getResources().getIntArray(R.array.data_icon);
        for (int i = 0 ; i < typeName.length  ; i++){
            OperationBean bean = new OperationBean();
            bean.name = typeName[i];
            bean.res = resIds[i];
            operationBeans.add(bean);
        }
        OperationAdapter adapter = new OperationAdapter(this,operationBeans);
        mBinding.rv_operation.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:      //名单下载
                        rosterDownload();
                        break;
                    case 1:    //数据查看

                        break;
                    case 2:    //数据备份
                        break;
                    case 3:   //数据还原
                        break;
                    case 4:   //数据清空
                        break;
                    case 5:   //成绩上传
                        break;
                }
            }
        });
    }

    private void rosterDownload() {
        viewModel.rosterDownload();
    }
}
