package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_select;
import com.blankj.utilcode.util.CacheMemoryUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.ExamPlace;
import com.fairplay.database.entity.GroupInfo;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.MyApplication;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ArrayAdapter;
import com.fairplay.examgradle.adapter.StudentAdapter;
import com.fairplay.examgradle.adapter.StudentResultAdapter;
import com.fairplay.examgradle.contract.CacheContract;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.service.DataScoreUploadService;
import com.fairplay.examgradle.viewmodel.DataSelectViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.layout.OnItemSelected;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_data_select)
public class DataSelectActivity extends BaseMvvmTitleActivity<Object, DataSelectViewModel, activity_data_select> {
    private MMKV mmkv;
    private StudentResultAdapter adapter;
    private List<DataRtiveBean> mList;
    private String itemCode;
    private String subItemCode;
    private List<Item> bigItems,subItems;
    private List<Schedule> scheduleList;
    private List<GroupInfo> groupInfoList;
    private List<ExamPlace> examPlaceList;
    private int pageNum = 1;
    private Schedule currentSchedule;
    private GroupInfo currentGroupInfo;
    private ExamPlace currentExamPlace;
    @Override
    protected Class<DataSelectViewModel> getViewModelClass() {
        return DataSelectViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("数据查看"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();

        mBinding.rv_results.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        itemCode = mmkv.getString(MMKVContract.CURRENT_ITEM,"");
        subItemCode = mmkv.getString(MMKVContract.CURRENT_SUB_ITEM,"");

        mBinding.refreshview.setOnRefreshListener(refreshlayout -> {
            mBinding.refreshview.finishRefresh();
            pageNum = 1;
            viewModel.selectAll(itemCode,subItemCode,currentSchedule,currentExamPlace,currentGroupInfo,pageNum);
        });
        mBinding.refreshview.setOnLoadmoreListener(refreshlayout -> {
            mBinding.refreshview.finishLoadmore();
            ToastUtils.showShort("没有更多数据了");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleList = DBManager.getInstance().getAllSchedule();
        examPlaceList = DBManager.getInstance().getAllExamplace();

        initSchedule(scheduleList);
        initExamPlaceList(examPlaceList);
        initBigItem();
        initSmoatItem();


    }




    private void initGroupInfo() {
        if (currentSchedule == null){
            return;
        }
        if (currentExamPlace == null){
            groupInfoList = DBManager.getInstance().getAllGroupInfo(currentSchedule.getScheduleNo(),null, itemCode, subItemCode);
        }else {
            groupInfoList = DBManager.getInstance().getAllGroupInfo(currentSchedule.getScheduleNo(), currentExamPlace.getExamplaceName(), itemCode, subItemCode);
        }
        List<String> strings = new ArrayList<>();
        strings.add("所有分组");

        GroupInfo groupInfoCurr = CacheMemoryUtils.getInstance().get(CacheContract.CurrentGroup);
        int currentPos = 0;
        for (int i = 0 ; i < groupInfoList.size() ; i++){
            GroupInfo groupInfo = groupInfoList.get(i);
            int type = groupInfo.getGroupType();
            strings.add((type == 0 ? "男子":type == 1?"女子":"混合")+groupInfo.getGroupNo()+"组");
            if (groupInfo.equals(groupInfoCurr)){
                currentPos = i + 1;
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_spinner_item,android.R.id.text1,strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_group.setAdapter(adapter);
        mBinding.sp_group.setSelection(currentPos);
    }

    private void initExamPlaceList(List<ExamPlace> examPlaceList) {
        List<String> strings = new ArrayList<>();
        strings.add("所有场地");
        for (ExamPlace schedule : examPlaceList){
            strings.add(schedule.getExamplaceName());
        }
        String examPlaceName = CacheMemoryUtils.getInstance().get(CacheContract.EXAM_PLACE_NAME);
        int currentPos = 0;
        for (int i = 0 ; i < examPlaceList.size() ; i++){
            if (examPlaceName == null){
                break;
            }else {
                if (examPlaceName.equals(examPlaceList.get(i).getExamplaceName())){
                    currentPos = i + 1;
                }
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_spinner_item,android.R.id.text1,strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_examplaceName.setAdapter(adapter);
        mBinding.sp_examplaceName.setSelection(currentPos);
    }

    private void initSchedule(List<Schedule> scheduleList) {
        List<String> strings = new ArrayList<>();
        String scheduNo = CacheMemoryUtils.getInstance().get(CacheContract.SCHEDULE_NO);
        int currentPos = 0;
        for (int i = 0 ; i < scheduleList.size() ; i++){
            Schedule schedule = scheduleList.get(i);
            String format = MyApplication.simpleDateFormat.format(new Date(schedule.getBeginTime()));
            strings.add("第"+schedule.getScheduleNo()+"场 "+ format);
            if (schedule.getScheduleNo().equals(scheduNo)){
                currentPos = i;
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_spinner_item,android.R.id.text1,strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_schedule.setAdapter(adapter);
        mBinding.sp_schedule.setSelection(currentPos);
    }

    private void initSmoatItem() {
        if (itemCode == null){
            return;
        }
        subItems = DBManager.getInstance().getSmoatItems(itemCode);
        if (subItems == null || subItems.isEmpty()){
            subItemCode = itemCode;
            mBinding.sp_subitem.setAdapter(null);
            pageNum = 1;
            return;
        }
        List<String> bigItemNames = new ArrayList<>();
        int currentPos = 0;
        String subItemCodecurr = CacheMemoryUtils.getInstance().get(CacheContract.SubItemCode);
        for (int i = 0 ; i < subItems.size() ; i++){
            Item item = subItems.get(i);
            bigItemNames.add(item.getItemName());
            if (TextUtils.isEmpty(subItemCodecurr)) {
                if (item.getSubitemCode().equals(subItemCode)) {
                    currentPos = i;
                }
            }else {
                if (item.getSubitemCode().equals(subItemCodecurr)){
                    currentPos = i;
                }
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
        String currentItemCode = CacheMemoryUtils.getInstance().get(CacheContract.ItemCode);
        List<String> bigItemNames = new ArrayList<>();
        int currentBigPos = 0;
        for (int i = 0 ; i < bigItems.size() ; i++){
            Item item = bigItems.get(i);
            bigItemNames.add(item.getItemName());
            if (TextUtils.isEmpty(currentItemCode)) {
                if (item.getItemCode().equals(itemCode)) {
                    currentBigPos = i;
                }
            }else {
                if (item.getItemCode().equals(currentItemCode)){
                    currentBigPos = i;
                }
            }
        }
        ArrayAdapter bigAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,android.R.id.text1,bigItemNames);
        bigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sp_item.setAdapter(bigAdapter);
        mBinding.sp_item.setSelection(currentBigPos);
        itemCode = bigItems.get(currentBigPos).getItemCode();
    }

    @OnItemSelected({R.id.sp_item,R.id.sp_subitem,R.id.sp_schedule,R.id.sp_group,R.id.sp_examplaceName})
    public void onItemSelected(AdapterView adapterView,int position){
        Log.e("TAG","--------");
        switch (adapterView.getId()){
            case R.id.sp_item:
                itemCode = bigItems.get(position).getItemCode();
                initSmoatItem();
                initGroupInfo();
                break;
            case R.id.sp_subitem:
                subItemCode = subItems.get(position).getSubitemCode();
                pageNum = 1;
                initExamPlaceList();
                initGroupInfo();
                break;
            case R.id.sp_schedule:
                currentSchedule = scheduleList.get(position);
                initGroupInfo();
                break;
            case R.id.sp_group:
                if (position > 0)
                    currentGroupInfo = groupInfoList.get(position - 1);
                else
                    currentGroupInfo = null;
                CacheMemoryUtils.getInstance().put(CacheContract.CurrentGroup,currentGroupInfo);
                viewModel.selectAll(itemCode,subItemCode,currentSchedule,currentExamPlace,currentGroupInfo,pageNum);
                break;
            case R.id.sp_examplaceName:
                if (position > 0){
                    currentExamPlace = examPlaceList.get(position - 1);
                    CacheMemoryUtils.getInstance().put(CacheContract.EXAM_PLACE_NAME,currentExamPlace.getExamplaceName());
                }else {
                    currentExamPlace = null;
                }
                initGroupInfo();
                break;
        }


    }

    private void initExamPlaceList() {
        examPlaceList = DBManager.getInstance().getExamplace(itemCode, subItemCode);
        initExamPlaceList(examPlaceList);
    }

    @OnClick({R.id.btn_upload,R.id.btn_query,R.id.tv_allSelected})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_upload:
                uploadScore();
                break;
            case R.id.btn_query:
                String input = mBinding.et_input_text.getText().toString().trim();
                pageNum = 1;
                viewModel.searchStuCode(input,itemCode,subItemCode,currentSchedule,currentExamPlace,currentGroupInfo);
                break;
            case R.id.tv_allSelected:
                sectedAll();
                break;
        }
    }

    private void sectedAll() {
        if (mList != null && !mList.isEmpty()){
            for (DataRtiveBean bean : mList){
                bean.isSelected = !bean.isSelected;
            }
            adapter = new StudentResultAdapter(mList,getApplicationContext());
            adapter.setOnItemClickListener((parent, view, position, id) -> {
                DataRtiveBean bean = mList.get(position);
                Intent intent = new Intent(getApplicationContext(),DataDisplayActivity.class);
                CacheMemoryUtils utils = CacheMemoryUtils.getInstance();
                utils.put(CacheContract.StudentCode,bean.studentCode);
                utils.put(CacheContract.ItemCode,bean.itemCode);
                utils.put(CacheContract.SubItemCode,bean.subItemCode);
                utils.put(CacheContract.EXAM_PLACE_NAME,bean.examPlaceName);
                utils.put(CacheContract.SCHEDULE_NO,bean.scheduleNo);
                utils.put(CacheContract.EXAMTYPE,bean.examType);
                startActivity(intent);
            });
            mBinding.rv_results.setAdapter(adapter);
        }
    }

    private void uploadScore() {
        List<DataRtiveBean> selectedBean = getSelectedBean();
        if (selectedBean == null || selectedBean.isEmpty()){
            ToastUtils.showShort("没有勾选成绩");
            LogUtils.operation("页面提示:没有勾选成绩");
            return;
        }
        ArrayList<StudentGroupItem> mqttBeans = new ArrayList<>();
        for (DataRtiveBean bean : selectedBean){
            StudentGroupItem mqttBean = DBManager.getInstance().getMQTTBean(bean.itemCode,bean.subItemCode,bean.studentCode);
            mqttBeans.add(mqttBean);
        }
        Intent intent = new Intent(getApplicationContext(), DataScoreUploadService.class);
//        intent.putParcelableArrayListExtra(DataScoreUploadService.MQTT_BEAN,mqttBeans);
        CacheMemoryUtils.getInstance().put(CacheContract.MQTT_BEAN,mqttBeans);
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
            mList.clear();
            mList.addAll(list);
            adapter = new StudentResultAdapter(mList,getApplicationContext());
            adapter.setOnItemClickListener((parent, view, position, id) -> {
                DataRtiveBean bean = mList.get(position);
                Intent intent = new Intent(getApplicationContext(),DataDisplayActivity.class);
                CacheMemoryUtils utils = CacheMemoryUtils.getInstance();
                utils.put(CacheContract.StudentCode,bean.studentCode);
                utils.put(CacheContract.ItemCode,bean.itemCode);
                utils.put(CacheContract.SubItemCode,bean.subItemCode);
                utils.put(CacheContract.EXAM_PLACE_NAME,bean.examPlaceName);
                utils.put(CacheContract.SCHEDULE_NO,bean.scheduleNo);
                utils.put(CacheContract.EXAMTYPE,bean.examType);
                startActivity(intent);
            });
            mBinding.txt_stu_sumNumber.setText(mList.size()+"");
            mBinding.rv_results.setAdapter(adapter);
        }else if (o instanceof Integer){
            if (((Integer)o) != BaseActivity.DIMMSION_PROGREESS) {
                setStuCount(o);
            }
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
