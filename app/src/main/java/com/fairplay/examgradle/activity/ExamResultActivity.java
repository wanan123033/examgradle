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
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
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
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamResultActivity  extends BaseMvvmTitleActivity<Object, ExamResultModel, activity_exam_score> {
    private Item item;
    private List<ExamScoreBean> examScoreBeans;
    private ExamAdapter adapter;
    private ExamScoreBean currentScoreBean;
    private int currentRoundNo;
    private MqttBean mqttBean;
    private String username;
    private OnMqttAndroidConnectListener listener;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        username = BaseApplication.getInstance().getMmkv().getString(MMKVContract.USERNAME,"");
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.tv_current_user.setText("当前用户:"+username);
        listener = new OnMqttAndroidConnectListener() {
            @Override
            public void connect() {
                super.connect();
                Log.e("TAG===>","connect");
            }

            @Override
            public void disConnect() {
                super.disConnect();
                Log.e("TAG===>","disConnect");
            }

            @Override
            public void onDataReceive(String message) {
                packMsg(message);
            }
        };
        MqttManager.getInstance().regeisterServerMsg(listener);
    }

    private void packMsg(String message) {
        Log.e("TAG====>",message);

        try {
            JSONObject jsonObject = new JSONObject(message);
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
                    mqttBean = GsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), MqttBean.class);
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
                if (currentScoreBean != null){
                    for (int i = 0 ; i < currentScoreBean.resultList.size() ; i++){
                        currentScoreBean.resultList.get(i).isLock = false;
                    }
                    if (adapter != null){
                        mBinding.stu_info.rv_score_data.setAdapter(adapter);
                    }
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
            com.blankj.utilcode.util.ToastUtils.showShort("无考生信息");
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
                viewModel.unLockStuScore(mqttBean,item,currentRoundNo);
                break;
            case R.id.btn_score1:
                String studentCode = mBinding.et_studentCode.getText().toString();
                String json = "{\"channelCode\":\"TEST-BASDF\",\"data\":{\"examPlaceName\":\"广西民族大学相思湖学院田径场西东B\",\"examStatus\":0,\"groupNo\":\"100\",\"groupType\":\"0\",\"itemCode\":\"0002\",\"scheduleNo\":\"3\",\"sortName\":\"组\",\"studentCode\":\""+studentCode+"\",\"subitemCode\":\"0002\",\"trackNo\":\"1\"},\"id\":1604396768022,\"matchUser\":0,\"messageType\":1}";
                packMsg(json);
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
        if (currentScoreBean.currentPosition == currentScoreBean.resultList.size() - 1){   //输入的成绩到了某轮的最后一个  触发保存成绩,并发送成绩
            RoundResult result = viewModel.saveResult(currentScoreBean,item,currentRoundNo);
            currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            if (result == null){
                return;
            }
        }
        if(currentScoreBean.resultList.size() > 1 && currentScoreBean.currentPosition < currentScoreBean.resultList.size() - 1){   //输入的成绩没有到某轮的最后一个,移动光标到下一个成绩
            currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            currentScoreBean.currentPosition = currentScoreBean.currentPosition + 1;
        }else {   //移动光标到下一个轮次
            currentScoreBean.resultList.get(currentScoreBean.currentPosition).isLock = true;
            if (currentRoundNo < examScoreBeans.size() - 1){
                currentRoundNo++;
                currentScoreBean = examScoreBeans.get(currentRoundNo);
                for (int i = 0 ; i < examScoreBeans.size() ; i++){
                    examScoreBeans.get(i).isSelected = false;
                }
                examScoreBeans.get(currentRoundNo).isSelected = true;
            }
        }
        adapter.setSelectPosition(currentRoundNo);
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
        currentRoundNo = 0;
        adapter.setSelectPosition(currentRoundNo);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
        return mqtt_id;
    }

    public long initStudentExam2(MqttBean mqttBean, Item item){
        List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(), item.getItemCode(), item.getSubitemCode());
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
                    if (!scoreBean.resultList.contains(score))
                        scoreBean.resultList.add(score);
                }
            }
            examScoreBeans.add(scoreBean);
        }
        if (examScoreBeans.size() < item.getRoundNum()){
            for (int i = examScoreBeans.size() ; i < item.getRoundNum() ; i++){
                ExamScoreBean scoreBean = new ExamScoreBean();
                scoreBean.studentCode = mqttBean.getStudentCode();
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
        }
        if (adapter == null) {
            adapter = new ExamAdapter(this, examScoreBeans);
        }else {
            adapter.setData(examScoreBeans);
        }
        currentRoundNo = stuRoundResult.size();
        currentScoreBean = examScoreBeans.get(currentRoundNo - 1);
        adapter.setSelectPosition(currentRoundNo);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
        Log.e("TAG===>",examScoreBeans.toString());
        MqttBean mqttBean1 = DBManager.getInstance().getMQTTBean(item.getItemCode(), item.getSubitemCode(),mqttBean.getStudentCode());
        return mqttBean1.getId();
    }

    private void appendScore(ExamScoreBean.Score score, String itemUnit, String scoreStr) {
        if (itemUnit.equals("s")){
            score.result.append(Double.parseDouble(scoreStr) / 1000.0);
            return;
        }else if (itemUnit.equals("minutes")){
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
        currentScoreBean = examScoreBeans.get(currentRound);
        currentScoreBean.currentPosition = currentPos;
        adapter.setSelectPosition(currentRound);
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
    }
}
