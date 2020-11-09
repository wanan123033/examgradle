package com.fairplay.examgradle.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_select;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.StudentAdapter;
import com.fairplay.examgradle.bean.DataRetrieveBean;
import com.fairplay.examgradle.viewmodel.DataCatModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_data_select)
public class DataCatActivity extends BaseMvvmTitleActivity<Object, DataCatModel, activity_data_select> {
    private MMKV mmkv;
    private List<DataRtiveBean> mList;
    private StudentAdapter adapter;

    @Override
    protected Class<DataCatModel> getViewModelClass() {
        return DataCatModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        mList = new ArrayList<>();
        adapter = new StudentAdapter(this,mList);
        mBinding.rv_results.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.rv_results.setAdapter(adapter);
        selectAll();
    }

    private void selectAll() {
        List<DataRtiveBean> data = DBManager.getInstance().getAllData(100, 0);
        mList.addAll(data);
        adapter.notifyDataSetChanged();

    }
}
