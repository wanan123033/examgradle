package com.fairplay.examgradle.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.MyApplication;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ExamAdapter;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.contract.MBMContract;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.service.ScoreUploadServie;
import com.fairplay.examgradle.viewmodel.ExamResultModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.messagebus.Subscrition;
import com.gwm.base.BaseApplication;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamResultActivity  extends BaseMvvmTitleActivity<Object, ExamResultModel, activity_exam_score> {
    public static final String PACK_JSON = "PACK_JSON";
    private Item item;
    private List<ExamScoreBean> examScoreBeans;
    private ExamAdapter adapter;
    private ExamScoreBean currentScoreBean;
    private int currentRoundNo = 1;
    private StudentGroupItem mqttBean;
    private String username;
    private OnMqttAndroidConnectListener listener;
    private MMKV mmkv;

    private View.OnClickListener leftFinsh = v -> finsh();

    private void finsh() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("确认退出吗?")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    protected Class<ExamResultModel> getViewModelClass() {
        return ExamResultModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        username = BaseApplication.getInstance().getMmkv().getString(MMKVContract.USERNAME,"");
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.tv_current_user.setText("当前用户:"+username);
        String packJson = getIntent().getStringExtra(PACK_JSON);
        if (TextUtils.isEmpty(packJson)) {
            listener = new OnMqttAndroidConnectListener() {
                @Override
                public void connect() {
                    super.connect();

                }

                @Override
                public void disConnect() {
                    super.disConnect();
                    String mqttIp = mmkv.getString(MMKVContract.MQIP, "");
                    String mqttPort = mmkv.getString(MMKVContract.MQPORT, "");
                    MqttManager.getInstance().init(getApplication())
                            .setServerIp(mqttIp)
                            .setServerPort(Integer.parseInt(mqttPort))
                            .connect(ExamResultActivity.this);
                    MqttManager.getInstance().regeisterServerMsg(this);


                }

                @Override
                public void onDataReceive(String message) {
                    packMsg(message,true);
                }
            };
            MqttManager.getInstance().regeisterServerMsg(listener);
        }else {
            packMsg(packJson,false);
            listener = new OnMqttAndroidConnectListener() {
                @Override
                public void connect() {
                    super.connect();

                }

                @Override
                public void disConnect() {
                    super.disConnect();
                    String mqttIp = mmkv.getString(MMKVContract.MQIP, "");
                    String mqttPort = mmkv.getString(MMKVContract.MQPORT, "");
                    MqttManager.getInstance().init(getApplication())
                            .setServerIp(mqttIp)
                            .setServerPort(Integer.parseInt(mqttPort))
                            .connect(ExamResultActivity.this);
                    MqttManager.getInstance().regeisterServerMsg(this);


                }

                @Override
                public void onDataReceive(String message) {
                    packMsg(message,true);
                }
            };
            MqttManager.getInstance().regeisterServerMsg(listener);
        }
    }
    private int flag = 1;
    private void packMsg(String message,boolean isChannalCode) {
        LogUtils.operation("接收到推送信息:"+message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (isChannalCode) {
                String channalCode = mmkv.getString(MMKVContract.CHANNEL_CODE, "");
                //通道号判断
                String code = jsonObject.getString("channelCode");
                if (TextUtils.isEmpty(channalCode) || !channalCode.equals(code)) {
                    return;
                }
            }
            //匹配用户判断
            int messageType = jsonObject.getInt("messageType");
            int matchUser = jsonObject.getInt("matchUser");
            boolean isUser = false;
            if (matchUser == 1){
                JSONArray array = jsonObject.getJSONArray("users");
                for (int i = 0 ; i < array.length() ; i++){
                    String user = array.get(i).toString();
                    if (user.equals(username)){
                        isUser = true;
                    }
                }
                if (!isUser){
                    return;
                }
            }
            if (examScoreBeans == null){
                examScoreBeans = new ArrayList<>();
            }
            if (currentScoreBean == null && examScoreBeans.isEmpty()) {
                if (messageType == 1) {
                    examScoreBeans.clear();
                    mqttBean = GsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), StudentGroupItem.class);
                    LogUtils.operation("接收到推送考生信息:"+mqttBean.toString());
                    item = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(), mqttBean.getSubitemCode());
                    long mqtt_id = initStudentExam2(mqttBean,item);

                    titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
                    MMKV mmkv = BaseApplication.getInstance().getMmkv();
                    mmkv.putLong(MMKVContract.MQTT_ID, mqtt_id);
                    mmkv.putString(MMKVContract.CURRENT_ITEM,mqttBean.getItemCode());
                    mmkv.putString(MMKVContract.CURRENT_SUB_ITEM,mqttBean.getSubitemCode());
                    mBinding.stu_info.tv_studentCode.setText(mqttBean.getStudentCode());
                    titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
                    mBinding.tv_data_format.setText("数据格式:"+item.getRemark1());
                }
            }else if (messageType == 3) {
                dismissDialog();
                int data = jsonObject.getInt("data");
                LogUtils.operation("接收到推送解锁信息:"+jsonObject.toString());
                if (data == 1) {
                    if (currentScoreBean != null) {
                        if (examScoreBeans != null && !examScoreBeans.isEmpty()){
                            for (ExamScoreBean bean : examScoreBeans){
                                for (int i = 0; i < bean.resultList.size(); i++) {
                                    bean.resultList.get(i).isLock = false;
                                }
                            }
                        }

                        if (adapter != null) {
                            mBinding.stu_info.rv_score_data.setAdapter(adapter);
                        }
                    }
                }else {
                    if (examScoreBeans != null && !examScoreBeans.isEmpty()) {
                        for (ExamScoreBean bean : examScoreBeans) {
                            for (int i = 0; i < bean.resultList.size(); i++) {
                                if (!TextUtils.isEmpty(bean.resultList.get(i).result.toString()))
                                    bean.resultList.get(i).isLock = true;
                            }
                        }
                    }
                    if (adapter != null) {
                        mBinding.stu_info.rv_score_data.setAdapter(adapter);
                    }
                    ToastUtils.showShort("服务器拒绝解锁!");
                    LogUtils.operation("页面提示:服务器拒绝解锁");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.tv_9,
            R.id.tv_dian,R.id.tv,R.id.tvj,R.id.tv_enter1,R.id.tv_enter2,R.id.tv_send,R.id.btn_score1})
    public void onClick(View view){
        String text = mBinding.stu_info.tv_studentCode.getText().toString().trim();
        if (TextUtils.isEmpty(text) && view.getId() != R.id.btn_score1){
            ToastUtils.showShort("无考生信息");
            LogUtils.operation("页面提示:无考生信息");
            return;
        }
        switch (view.getId()){
            case R.id.tv_0:
                viewModel.onScore(currentScoreBean,"0",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_1:
                viewModel.onScore(currentScoreBean,"1",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_2:
                viewModel.onScore(currentScoreBean,"2",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_3:
                viewModel.onScore(currentScoreBean,"3",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_4:
                viewModel.onScore(currentScoreBean,"4",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_5:
                viewModel.onScore(currentScoreBean,"5",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_6:
                viewModel.onScore(currentScoreBean,"6",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_7:
                viewModel.onScore(currentScoreBean,"7",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_8:
                viewModel.onScore(currentScoreBean,"8",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_9:
                viewModel.onScore(currentScoreBean,"9",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_dian:
                viewModel.onScore(currentScoreBean,".",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv:
                viewModel.onScore(currentScoreBean,":",item);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tvj:
                showDialog("申请解锁中...");
                LogUtils.operation("用户点击了解锁:mqttBean="+mqttBean.toString());
                LogUtils.operation("用户点击了解锁:item="+item.toString());
                LogUtils.operation("用户点击了解锁:currentRoundNo="+currentRoundNo);
                if (currentRoundNo == 1){
                    viewModel.unLockStuScore(mqttBean,item,currentRoundNo);
                }else if (currentRoundNo == 2){
                    viewModel.unLockStuScore(mqttBean,item,1);
                    viewModel.unLockStuScore(mqttBean,item,2);
                }
                break;
            case R.id.btn_score1:
                String studentCode = mBinding.et_studentCode.getText().toString();
                String json = "{\"channelCode\":\"TEST-BASDF\",\"data\":{\"examPlaceName\":\"广西民族大学相思湖学院田径场西东B\",\"examStatus\":0,\"groupNo\":\"100\",\"groupType\":\"0\",\"itemCode\":\"0400\",\"scheduleNo\":\"3\",\"sortName\":\"组\",\"studentCode\":\""+"050180075"+"\",\"subitemCode\":\"04001\",\"trackNo\":\"1\"},\"id\":1604396768022,\"matchUser\":0,\"messageType\":1}";
                packMsg(json,false);
                break;
            case R.id.tv_send:
                saveAndSendResult();
                break;
            case R.id.tv_enter2:
                viewModel.removeScore(currentScoreBean);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_enter1:
                quitCommit();
                break;

        }
    }

    private void saveAndSendResult() {
        if (TextUtils.isEmpty(currentScoreBean.resultList.get(currentScoreBean.currentPosition).result.toString())){
            return;
        }
        if (currentScoreBean.currentPosition == currentScoreBean.resultList.size() - 1){   //输入的成绩到了某轮的最后一个  触发保存成绩,并发送成绩
            RoundResult currentResult = viewModel.saveResult(mqttBean.getScheduleNo(),currentScoreBean,item,currentRoundNo,mqttBean);
            if (currentResult == null){
                //TODO 上传成绩
                List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(), mqttBean.getItemCode(), mqttBean.getSubitemCode(), mqttBean.getScheduleNo(), mqttBean.getExamPlaceName(), mqttBean.getGroupNo(), mqttBean.getGroupType());
                for(RoundResult result : stuRoundResult){
                    LogUtils.operation("上传成绩信息:"+result.toString());
                    Intent intent = new Intent(getApplicationContext(), ScoreUploadServie.class);
                    intent.putExtra(ScoreUploadServie.ROUNDID,result.getId().longValue());
                    startService(intent);
                }

            }
            if (currentResult != null){
                LogUtils.operation("上锁成绩信息:"+currentResult.toString());
                currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            }
        }

        if(currentScoreBean.resultList.size() > 1 && currentScoreBean.currentPosition < currentScoreBean.resultList.size() - 1){   //输入的成绩没有到某轮的最后一个,移动光标到下一个成绩
            currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            currentScoreBean.currentPosition = currentScoreBean.currentPosition + 1;
            LogUtils.operation("移动光标到下一个成绩:currentPosition="+currentScoreBean.currentPosition+",currentRoundNo="+currentRoundNo);
        }else {   //移动光标到下一个轮次
            currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            if (currentRoundNo < examScoreBeans.size()){
                currentRoundNo++;
                currentScoreBean = examScoreBeans.get(currentRoundNo - 1);
                for (int i = 0 ; i < examScoreBeans.size() ; i++){
                    examScoreBeans.get(i).isSelected = false;
                }
                examScoreBeans.get(currentRoundNo - 1).isSelected = true;
            }
            LogUtils.operation("移动光标到下一个成绩:currentPosition="+currentScoreBean.currentPosition+",currentRoundNo="+currentRoundNo);
        }
        adapter.setSelectPosition(currentRoundNo - 1);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }

    private long initStudentExam(String studentCode, Item item) {
        long mqtt_id = DBManager.getInstance().insertMqttBean(mqttBean);
        mBinding.stu_info.tv_studentCode.setText(studentCode);
        List<MultipleItem> multipleItemList = DBManager.getInstance().getMultipleItemList(item.getId());
        for (int i = 0 ; i < item.getRoundNum() ; i++){
            ExamScoreBean scoreBean = new ExamScoreBean();
            scoreBean.studentCode = studentCode;
            scoreBean.roundNo = i + 1;
            scoreBean.item = item;
            scoreBean.resultList = new ArrayList<>();
            if (i == 0){
                scoreBean.isSelected = true;
                currentScoreBean = scoreBean;
            }else {
                scoreBean.isSelected = false;
            }
            if (multipleItemList == null || multipleItemList.isEmpty()){
                ExamScoreBean.Score score = new ExamScoreBean.Score();
                score.desc = "成绩";
                score.result = new StringBuffer();
                score.unit = item.getUnit();
                scoreBean.resultList.add(score);
            }else{
                for (MultipleItem multipleItem : multipleItemList){
                    ExamScoreBean.Score score = new ExamScoreBean.Score();
                    score.desc = multipleItem.getDesc();
                    score.result = new StringBuffer();
                    score.unit = item.getUnit();
                    score.order = multipleItem.getOrder();
                    scoreBean.resultList.add(score);
                }
            }
            examScoreBeans.add(scoreBean);
        }
        if (adapter == null){
            adapter = new ExamAdapter(this,examScoreBeans);
        }else {
            adapter.setData(examScoreBeans);
        }
        currentRoundNo = 1;
        adapter.setSelectPosition(currentRoundNo -1);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
        return mqtt_id;
    }

    public long initStudentExam2(StudentGroupItem mqttBean, Item item){
        List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(),
                mqttBean.getItemCode(),mqttBean.getSubitemCode(),
                mqttBean.getScheduleNo(),
                mqttBean.getExamPlaceName(),
                mqttBean.getGroupNo(),mqttBean.getGroupType());
        if (stuRoundResult == null || stuRoundResult.isEmpty()){
            return initStudentExam(mqttBean.getStudentCode(),item);

        }
        mBinding.stu_info.tv_studentCode.setText(mqttBean.getStudentCode());
        List<MultipleItem> multipleItemList = DBManager.getInstance().getMultipleItemList(item.getId());
        for (RoundResult result : stuRoundResult){
            ExamScoreBean scoreBean = new ExamScoreBean();
            scoreBean.resultList = new ArrayList<>();
            scoreBean.studentCode = result.getStudentCode();
            scoreBean.roundNo = result.getRoundNo();
            scoreBean.item = item;
            if (result.getIsMultioleResult() == 1){ //有多值
                List<MultipleResult> multioleResult = DBManager.getInstance().getMultioleResult(result.getId());
                for (MultipleResult multipResult : multioleResult){
                    ExamScoreBean.Score score = new ExamScoreBean.Score();
                    score.desc = multipResult.getDesc();
                    score.isLock = true;
                    score.result = new StringBuffer();
                    score.unit = item.getUnit();
                    score.order = multipResult.getOrder();
                    appendScore(score,item.getUnit(),multipResult.getMachineScore());
                    scoreBean.resultList.add(score);
                }
            }else {
                ExamScoreBean.Score score = new ExamScoreBean.Score();
                score.desc = "成绩";
                score.result = new StringBuffer();
                score.unit = item.getUnit();
                score.isLock = true;
                String scoreStr = (!TextUtils.isEmpty(result.getMachineResult()))? result.getMachineResult() : result.getMachineScore();
                appendScore(score,item.getUnit(),scoreStr);
                scoreBean.resultList.add(score);
            }
            if (scoreBean.resultList.size() < multipleItemList.size()){
                for (MultipleItem multipleItem : multipleItemList){
                    ExamScoreBean.Score score = new ExamScoreBean.Score();
                    score.desc = multipleItem.getDesc();
                    score.result = new StringBuffer();
                    score.unit = item.getUnit();
                    score.order = multipleItem.getOrder();
//                    if (!scoreBean.resultList.contains(score))
                        scoreBean.resultList.add(score);
                }
            }
            if (!examScoreBeans.contains(scoreBean))
                examScoreBeans.add(scoreBean);
        }
        if (examScoreBeans.size() < item.getRoundNum()){
            for (int i = examScoreBeans.size() ; i < item.getRoundNum() ; i++){
                ExamScoreBean scoreBean = new ExamScoreBean();
                scoreBean.studentCode = mqttBean.getStudentCode();
                scoreBean.roundNo = i;
                scoreBean.item = item;
                scoreBean.resultList = new ArrayList<>();
                if (i == 0){
                    scoreBean.isSelected = true;
                    currentScoreBean = scoreBean;
                }else {
                    scoreBean.isSelected = false;
                }
                if (multipleItemList == null || multipleItemList.isEmpty()){
                    ExamScoreBean.Score score = new ExamScoreBean.Score();
                    score.desc = "成绩";
                    score.result = new StringBuffer();
                    score.unit = item.getUnit();
                    scoreBean.resultList.add(score);
                }else{
                    for (MultipleItem multipleItem : multipleItemList){
                        ExamScoreBean.Score score = new ExamScoreBean.Score();
                        score.desc = multipleItem.getDesc();
                        score.result = new StringBuffer();
                        score.unit = item.getUnit();
                        score.order = multipleItem.getOrder();
                        scoreBean.resultList.add(score);
                    }
                }
//                if (!examScoreBeans.contains(scoreBean))
                    examScoreBeans.add(scoreBean);
            }
        }
        if (adapter == null) {
            adapter = new ExamAdapter(this, examScoreBeans);
        }else {
            adapter.setData(examScoreBeans);
        }

        currentRoundNo = examScoreBeans.get(examScoreBeans.size() - 1).roundNo;
        currentScoreBean = examScoreBeans.get(currentRoundNo - 1);
        adapter.setSelectPosition(currentRoundNo - 1);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
        Log.e("TAG===>",examScoreBeans.toString());
        StudentGroupItem mqttBean1 = DBManager.getInstance().getMQTTBean(item.getItemCode(), item.getSubitemCode(),mqttBean.getStudentCode());
        return mqttBean1.getId();
    }

    private void appendScore(ExamScoreBean.Score score, String itemUnit, String scoreStr) {
        if ("s".equals(itemUnit)){
            score.result.append(Double.parseDouble(scoreStr) / 1000.0);
            return;
        }else if ("minutes".equals(itemUnit)){
            MyApplication.scoreDateFormat.applyPattern("mm:ss.SS");
            String format = MyApplication.scoreDateFormat.format(new Date(Long.parseLong(scoreStr)));
            score.result.append(format);
            return;
        }
        score.result.append(scoreStr);
    }

    @Override
    public void onChanged(Object o) {
        super.onChanged(o);
        if (o instanceof RoundResult){ //上传成绩
            Intent intent = new Intent(getApplicationContext(), ScoreUploadServie.class);
            intent.putExtra(ScoreUploadServie.ROUNDID,((RoundResult) o).getId().longValue());
            startService(intent);
        }
    }
    private void quitCommit() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("确认结束吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        examScoreBeans.clear();
                        currentScoreBean = null;
                        mBinding.stu_info.tv_studentCode.setText("");
                        adapter = new ExamAdapter(getApplicationContext(),examScoreBeans);
                        adapter.clearData();
                        mBinding.stu_info.rv_score_data.setAdapter(adapter);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Subscrition(action = MBMContract.UN_LOCK,threadMode = Subscrition.ThreadMode.MAIN)
    public void onMessage(MessageBusMessage message){
        int currentRound = (int) message.getData("currentRound");
        int currentPos = (int) message.getData("currentPos");
        currentScoreBean = examScoreBeans.get(currentRound - 1);
        currentScoreBean.currentPosition = currentPos;
        adapter.setSelectPosition(currentRound - 1);
        currentRoundNo = currentRound;
        StringBuffer result = currentScoreBean.resultList.get(currentScoreBean.currentPosition).result;
        result.delete(0,result.length());
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }
    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        if (item != null){
            Item item1 = DBManager.getInstance().getItemByItemCode(item.getItemCode(), item.getItemCode());
            if (item1 != null)
                builder.setTitle(item1.getItemName() + " - " + item.getItemName());
        }
        return super.setTitleBarBuilder(builder).setLeftImageOnClickListener(leftFinsh).setLeftTextOnClickListener(leftFinsh);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            finsh();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MqttManager.getInstance().unRegeisterServerMsg(listener);
        MqttManager.getInstance().disConnect();
    }
}
