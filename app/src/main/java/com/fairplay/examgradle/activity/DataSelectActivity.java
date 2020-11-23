package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_select;
import com.blankj.utilcode.util.CacheMemoryUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ArrayAdapter;
import com.fairplay.examgradle.adapter.StudentAdapter;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.service.DataScoreUploadService;
import com.fairplay.examgradle.viewmodel.DataSelectViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.layout.OnItemSelected;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.orhanobut.logger.utils.LogUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_data_select)
public class DataSelectActivity extends BaseMvvmTitleActivity<Object, DataSelectViewModel, activity_data_select> {
    private MMKV mmkv;
    private StudentAdapter adapter;
    private List<DataRtiveBean> mList;
    private String itemCode;
    private String subItemCode;
    private List<Item> bigItems,subItems;
    private int pageNum = 0;
    @Override
    protected Class<DataSelectViewModel> getViewModelClass() {
        return DataSelectViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();

        mBinding.rv_results.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        itemCode = mmkv.getString(MMKVContract.CURRENT_ITEM,"");
        subItemCode = mmkv.getString(MMKVContract.CURRENT_SUB_ITEM,"");
        viewModel.selectAll(itemCode,subItemCode,pageNum);
        initBigItem();
        initSmoatItem();

        mBinding.refreshview.setOnRefreshListener(refreshlayout -> {
            pageNum = 0;
            viewModel.selectAll(itemCode,subItemCode,pageNum);
        });
        mBinding.refreshview.setOnLoadmoreListener(refreshlayout -> {
            pageNum++;
            viewModel.selectAll(itemCode,subItemCode,pageNum);
        });
    }

    private void initSmoatItem() {
        if (itemCode == null){
            return;
        }
        subItems = DBManager.getInstance().getSmoatItems(itemCode);
        if (subItems == null || subItems.isEmpty()){
            subItemCode = itemCode;
            mBinding.sp_subitem.setAdapter(null);
            pageNum = 0;
            viewModel.selectAll(subItemCode,subItemCode,pageNum);
            return;
        }
        List<String> bigItemNames = new ArrayList<>();
        int currentPos = 0;
        for (int i = 0 ; i < subItems.size() ; i++){
            Item item = subItems.get(i);
            bigItemNames.add(item.getItemName());
            if (item.getItemCode().equals(subItemCode)){
                currentPos = i;
            }
        }
        ArrayAdapter bigAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,android.R.id.text1,bigItemNames);
        bigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_subitem.setAdapter(bigAdapter);
        mBinding.sp_subitem.setSelection(currentPos);
        subItemCode = subItems.get(currentPos).getSubitemCode();
    }

    private void initBigItem() {

        bigItems = DBManager.getInstance().getBigItems();
        if(bigItems == null || bigItems.isEmpty()){
            return;
        }
        List<String> bigItemNames = new ArrayList<>();
        int currentBigPos = 0;
        for (int i = 0 ; i < bigItems.size() ; i++){
            Item item = bigItems.get(i);
            bigItemNames.add(item.getItemName());
            if (item.getItemCode().equals(itemCode)){
                currentBigPos = i;
            }
        }
        ArrayAdapter bigAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,android.R.id.text1,bigItemNames);
        bigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_item.setAdapter(bigAdapter);
        mBinding.sp_item.setSelection(currentBigPos);
        itemCode = bigItems.get(currentBigPos).getItemCode();
    }

    @OnItemSelected({R.id.sp_item,R.id.sp_subitem})
    public void onItemSelected(AdapterView adapterView,int position){
        switch (adapterView.getId()){
            case R.id.sp_item:
                itemCode = bigItems.get(position).getItemCode();
                initSmoatItem();
                break;
            case R.id.sp_subitem:
                subItemCode = subItems.get(position).getSubitemCode();
                pageNum = 0;
                viewModel.selectAll(itemCode,subItemCode,pageNum);
                break;
        }
    }
    @OnClick({R.id.btn_upload,R.id.btn_query})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_upload:
                uploadScore();
                break;
            case R.id.btn_query:
                String input = mBinding.et_input_text.getText().toString().trim();
                pageNum = 0;
                viewModel.searchStuCode(input);
                break;
        }
    }

    private void uploadScore() {
        List<DataRtiveBean> selectedBean = getSelectedBean();
        if (selectedBean == null || selectedBean.isEmpty()){
            ToastUtils.showShort("没有勾选成绩");
            LogUtils.operation("页面提示:没有勾选成绩");
            return;
        }
        ArrayList<MqttBean> mqttBeans = new ArrayList<>();
        for (DataRtiveBean bean : selectedBean){
            MqttBean mqttBean = DBManager.getInstance().getMQTTBean(bean.itemCode,bean.subItemCode,bean.studentCode);
            mqttBeans.add(mqttBean);
        }
        Intent intent = new Intent(getApplicationContext(), DataScoreUploadService.class);
//        intent.putParcelableArrayListExtra(DataScoreUploadService.MQTT_BEAN,mqttBeans);
        CacheMemoryUtils.getInstance().put(DataScoreUploadService.MQTT_BEAN,mqttBeans);
        startService(intent);
    }

    public void setStuCount(Object sumCount) {
        mBinding.txt_stu_sumNumber.setText(sumCount + "");
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof List){
            List<DataRtiveBean> list = (List<DataRtiveBean>) o;
            if (mList == null) {
                mList = new ArrayList<>();
            }
            if (pageNum == 0){
                mList.clear();
            }
            mList.addAll(list);
            adapter = new StudentAdapter(this,mList);
            adapter.setOnItemClickListener((parent, view, position, id) -> {
                DataRtiveBean bean = adapter.getData().get(position);
                Intent intent = new Intent(getApplicationContext(),DataDisplayActivity.class);
                intent.putExtra(DataDisplayActivity.StudentCode,bean.studentCode);
                intent.putExtra(DataDisplayActivity.ItemCode,bean.itemCode);
                intent.putExtra(DataDisplayActivity.SubItemCode,bean.subItemCode);
                intent.putExtra(DataDisplayActivity.EXAM_PLACE_NAME,bean.examPlaceName);
                startActivity(intent);
            });
            mBinding.rv_results.setAdapter(adapter);
        }else if (o instanceof Integer){
            setStuCount(o);
        }
    }

    public List<DataRtiveBean> getSelectedBean(){
        List<DataRtiveBean> rtiveBeans = new ArrayList<>();
        for (DataRtiveBean bean : mList){
            if (bean.isSelected){
                rtiveBeans.add(bean);
            }
        }
        return rtiveBeans;
    }
}
