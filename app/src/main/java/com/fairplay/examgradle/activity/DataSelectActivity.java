package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_select;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.Item;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.StudentAdapter;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.viewmodel.DataSelectViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.layout.OnItemSelected;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_data_select)
public class DataSelectActivity extends BaseMvvmTitleActivity<Object, DataSelectViewModel, activity_data_select> {
    private MMKV mmkv;
    private StudentAdapter adapter;
    private int mPageNum;
    private List<DataRtiveBean> mList;
    private ArrayAdapter itemAdapter;
    private String itemCode;
    private String subItemCode;
    private List<Item> itemList;
    @Override
    protected Class<DataSelectViewModel> getViewModelClass() {
        return DataSelectViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        mList = new ArrayList<>();
        adapter = new StudentAdapter(this,mList);
        mBinding.rv_results.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.rv_results.setAdapter(adapter);
        itemCode = mmkv.getString(MMKVContract.CURRENT_ITEM,"");
        subItemCode = mmkv.getString(MMKVContract.CURRENT_SUB_ITEM,"");
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DataDisplayActivity.class);
                String studentCode = adapter.getData().get(position).studentCode;
                intent.putExtra(DataDisplayActivity.StudentCode,studentCode);
                intent.putExtra(DataDisplayActivity.ItemCode,itemCode);
                intent.putExtra(DataDisplayActivity.SubItemCode,subItemCode);
                startActivity(intent);
            }
        });

        itemList = DBManager.getInstance().getItemList();
        List<String> itemNames = new ArrayList<>();
        for (Item item : itemList){
            itemNames.add(item.getItemName());
        }
        itemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,android.R.id.text1,itemNames);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_item.setAdapter(itemAdapter);
    }

    @OnItemSelected(R.id.sp_item)
    public void onItemSelected(AdapterView adapterView,int position){
        Item item = itemList.get(position);
        itemCode = item.getItemCode();
        subItemCode = item.getSubitemCode();
    }
    @OnClick({R.id.btn_upload,R.id.btn_query})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_upload:
                break;
            case R.id.btn_query:
                String input = mBinding.et_input_text.getText().toString().trim();
                mPageNum = 0;
                break;
        }
    }
    private void setStuCount(Object sumCount, Object womenCount, Object mamCount) {
        mBinding.txt_stu_sumNumber.setText(sumCount + "");
        mBinding.txt_stu_manNumber.setText(mamCount + "");
        mBinding.txt_stu_womemNumber.setText(womenCount + "");
    }
}
