package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_display;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.MyApplication;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ResultAdapter;
import com.fairplay.examgradle.bean.ScoreBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.contract.Permission;
import com.fairplay.examgradle.contract.Unit;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.viewmodel.DataDisplayViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_data_display)
public class DataDisplayActivity extends BaseMvvmTitleActivity<Object, DataDisplayViewModel, activity_data_display> {
    public static final String StudentCode = "StudentCode";
    public static final String ItemCode = "ItemCode";
    public static final String SubItemCode = "SubItemCode";
    public static final String EXAM_PLACE_NAME = "ExamPlaceName";
    public static final String SCHEDULE_NO = "scheduleNo";
    private String studentCode;
    private String itemCode;
    private String subItemCode;
    private String examplaceName;
    private String scheduleNo;
    private List<ScoreBean> scoreBeans;
    private MMKV mmkv;
    private StudentGroupItem studentGroupItem;
    private int examType;
    private Item subItem;

    @Override
    protected Class<DataDisplayViewModel> getViewModelClass() {
        return DataDisplayViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("数据详情"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        studentCode = getIntent().getStringExtra(StudentCode);
        itemCode = getIntent().getStringExtra(ItemCode);
        examplaceName = getIntent().getStringExtra(EXAM_PLACE_NAME);
        subItemCode = getIntent().getStringExtra(SubItemCode);
        scheduleNo = getIntent().getStringExtra(SCHEDULE_NO);
        Item item = DBManager.getInstance().getItemByItemCode(itemCode,itemCode);

        subItem = DBManager.getInstance().getItemByItemCode(itemCode,subItemCode);
        mBinding.tv_sex.setText(item.getItemName()+"-"+subItem.getItemName());
        mBinding.tv_stuCode.setText(studentCode);
        mBinding.tv_stuName.setText(examplaceName);

        examType = mmkv.getInt(MMKVContract.EXAMTYPE,0);
        studentGroupItem = DBManager.getInstance().getMQTTBean(itemCode,subItemCode,studentCode,examType,examplaceName,scheduleNo);

        String permission = mmkv.getString(MMKVContract.PERMISSION,"");
        if (permission.contains(Permission.hasManulInputScore)){
            mBinding.btn_upload.setVisibility(View.VISIBLE);
        }else {
            mBinding.btn_upload.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(studentCode, itemCode, subItemCode,examType,examplaceName,scheduleNo);
        scoreBeans = new ArrayList<>();

        for (RoundResult result : stuRoundResult){
            if (result.getIsMultioleResult() == 0){  //单值类项目
                ScoreBean scoreBean = new ScoreBean();
                scoreBean.roundNo = result.getRoundNo();

                if (subItem.getMarkScore() == 0) {  //测量项目
                    if(subItem.getTestType() != 1) {
                        scoreBean.result = result.getResult() + Unit.getUnit(subItem.getUnit()).getDesc();
                    }else {
                        MyApplication.scoreDateFormat.applyPattern(subItem.getRemark1());
                        scoreBean.result = MyApplication.scoreDateFormat.format(new Date(Long.parseLong(result.getResult())));
                    }
                }else {
                    scoreBean.score = result.getScore();
                }

                scoreBean.examStatus = result.getExamType();
                scoreBean.testTime = MyApplication.simpleDateFormat.format(new Date(Long.parseLong(result.getTestTime())));
                scoreBeans.add(scoreBean);
            }else {
                List<MultipleResult> multipleResults = DBManager.getInstance().getMultioleResult(result.getId());
                for (MultipleResult multipleResult : multipleResults){
                    ScoreBean scoreBean = new ScoreBean();

                    scoreBean.testTime = MyApplication.simpleDateFormat.format(new Date(Long.parseLong(result.getTestTime())));
                    if (subItem.getMarkScore() == 0){  //测量项目
                        if(subItem.getTestType() != 1) {
                            scoreBean.result = multipleResult.getDesc() + "--" + multipleResult.getScore() + "  " + Unit.getUnit(subItem.getUnit()).getDesc();
                        }else {
                            MyApplication.scoreDateFormat.applyPattern(subItem.getRemark1());
                            scoreBean.result = MyApplication.scoreDateFormat.format(new Date(Long.parseLong(result.getScore())));
                        }
                    }else { //打分项目
                        scoreBean.score = multipleResult.getDesc()+"--"+multipleResult.getScore();
                    }
                    scoreBean.roundNo = result.getRoundNo();
                    scoreBean.examStatus = result.getExamType();
                    scoreBeans.add(scoreBean);
                }
            }
            ResultAdapter resultAdapter = new ResultAdapter(this,scoreBeans);
            mBinding.rv_result.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
            mBinding.rv_result.setAdapter(resultAdapter);
        }
    }

    /**
     * String json = "{\"channelCode\":\"TEST-BASDF\",\"data\":{\"examPlaceName\":\"广西民族大学相思湖学院田径场西东B\",\"examStatus\":0,\"groupNo\":\"100\",\"groupType\":\"0\",\"itemCode\":\"0400\",\"scheduleNo\":\"3\",\"sortName\":\"组\",\"studentCode\":\""+"050180075"+"\",\"subitemCode\":\"04001\",\"trackNo\":\"1\"},\"id\":1604396768022,\"matchUser\":0,\"messageType\":1}";
     * @param view
     */
    @OnClick(R.id.btn_upload)
    public void onClick(View view){
        try {
            JSONObject object = new JSONObject();
            String channelCode = mmkv.getString(MMKVContract.CHANNEL_CODE, "");
            object.put("channelCode",channelCode);
            JSONObject data = new JSONObject();
            data.put("examPlaceName",examplaceName);
            data.put("examStatus",studentGroupItem.getExamStatus());
            data.put("groupNo",studentGroupItem.getGroupNo());
            data.put("groupType",studentGroupItem.getGroupType());
            data.put("itemCode",studentGroupItem.getItemCode());
            data.put("scheduleNo",studentGroupItem.getScheduleNo());
            data.put("sortName",studentGroupItem.getSortName());
            data.put("studentCode",studentGroupItem.getStudentCode());
            data.put("subitemCode",studentGroupItem.getSubitemCode());
            data.put("trackNo",studentGroupItem.getTrackNo());
            object.put("data",data);
            object.put("id",1255836);
            object.put("matchUser",0);
            object.put("messageType",1);
            LogUtils.operation("成绩手工录入:"+object.toString());
            String ip = mmkv.getString(MMKVContract.MQIP, "");
            String port = mmkv.getString(MMKVContract.MQPORT, "");
            startMqttService(ip, port,object.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startMqttService(String ip, String port, String message) {
        showDialog("连接中...");
        if(!MqttManager.getInstance().isConnected()){
            MqttManager.getInstance().init(getApplication())
                    .setServerIp(ip)
                    .setServerPort(Integer.parseInt(port))
                    .connect(this);
            LogUtils.operation("MQTT 连接信息:ip="+ip+",port="+port);
            MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
                @Override
                public void connect() {
                    super.connect();
                    dismissDialog();
                    LogUtils.operation("MQTT 连接成功");
                    Intent intent = new Intent(getApplicationContext(),ExamResultActivity.class);
                    intent.putExtra(ExamResultActivity.PACK_JSON,message);
                    startActivity(intent);
                }

                @Override
                public void disConnect() {
                    super.disConnect();
                    LogUtils.operation("MQTT 连接断开");
                    dismissDialog();
                }

                @Override
                public void onDataReceive(String message) {
                    Log.e("TAG===>",message);
                }
            });
        }
    }
}
