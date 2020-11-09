package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ExamAdapter;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.service.ScoreUploadServie;
import com.fairplay.examgradle.viewmodel.ExamResultModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
        MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
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
        });
    }

    private void packMsg(String message) {
        Log.e("TAG====>",message);
        try {
            if (examScoreBeans == null){
                examScoreBeans = new ArrayList<>();
            }
            if (currentScoreBean == null && examScoreBeans.isEmpty()) {
                boolean isUser = false;
                JSONObject jsonObject = new JSONObject(message);
                int messageType = jsonObject.getInt("messageType");
                int matchUser = jsonObject.getInt("matchUser");
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
                if (messageType == 1) {
                    examScoreBeans.clear();
                    mqttBean = GsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), MqttBean.class);
                    long mqtt_id = DBManager.getInstance().insertMqttBean(mqttBean);
                    item = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(), mqttBean.getSubitemCode());
                    titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
                    initStudentExam(mqttBean.getStudentCode(),item);
                    MMKV mmkv = BaseApplication.getInstance().getMmkv();
                    mmkv.putLong(MMKVContract.MQTT_ID, mqtt_id);
                    mmkv.putString(MMKVContract.CURRENT_ITEM,mqttBean.getItemCode());
                    mmkv.putString(MMKVContract.CURRENT_SUB_ITEM,mqttBean.getSubitemCode());
                    mBinding.stu_info.tv_studentCode.setText(mqttBean.getStudentCode());
                    titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
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

                break;
            case R.id.btn_score1:
                String studentCode = mBinding.et_studentCode.getText().toString();
                String json = "{\"channelCode\":\"TEST-BASDF\",\"data\":{\"examPlaceName\":\"广西民族大学西校区篮球馆\",\"examStatus\":0,\"groupNo\":\"1\",\"groupType\":\"0\",\"itemCode\":\"10052\",\"scheduleNo\":\"1\",\"sortName\":\"组\",\"studentCode\":\""+studentCode+"\",\"subitemCode\":\"100521\",\"trackNo\":\"1\"},\"id\":1604396768022,\"matchUser\":0,\"messageType\":1}";
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
//        ExamScoreBean.Score lastScore = currentScoreBean.resultList.get(currentScoreBean.resultList.size() - 1);
//        if (viewModel.scoreCheck(item, "", lastScore)){
//            return;
//        }
//        if (item.getTestType() == 1 && !lastScore.result.toString().contains(":")){
//            ToastUtils.showShort("时间格式错误");
//            return;
//        }
        if (currentScoreBean.currentPosition == currentScoreBean.resultList.size() - 1){   //输入的成绩到了某轮的最后一个  触发保存成绩,并发送成绩
            viewModel.saveResult(currentScoreBean,item,currentRoundNo);
        }
        if(currentScoreBean.resultList.size() > 1 && currentScoreBean.currentPosition < currentScoreBean.resultList.size() - 1){   //输入的成绩没有到某轮的最后一个,移动光标到下一个成绩
            currentScoreBean.currentPosition = currentScoreBean.currentPosition + 1;
        }else {   //移动光标到下一个轮次
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

    private void initStudentExam(String studentCode, Item item) {
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
                    scoreBean.resultList.add(score);
                    score.order = multipleItem.getOrder();
                }
            }
            examScoreBeans.add(scoreBean);
        }
        if (adapter == null){
            adapter = new ExamAdapter(this,examScoreBeans);
        }
        currentRoundNo = 0;
        adapter.setSelectPosition(currentRoundNo);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }
    @Override
    public void onChanged(Object o) {
        if (o instanceof RoundResult){ //上传成绩
            Intent intent = new Intent(getApplicationContext(), ScoreUploadServie.class);
            intent.putExtra(ScoreUploadServie.ROUNDID,((RoundResult) o).getId().longValue());
            startService(intent);
        }
    }
    private void quitCommit() {
        examScoreBeans.clear();
        currentScoreBean = null;
        mBinding.stu_info.tv_studentCode.setText("");
        adapter = new ExamAdapter(this,examScoreBeans);
        adapter.clearData();
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }
    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
//        if (item != null){
//            Item item1 = DBManager.getInstance().getItemByItemCode(item.getItemCode(), item.getItemCode());
//            builder.setTitle(item1.getItemName()+" - "+item.getItemName());
//        }
        return super.setTitleBarBuilder(builder);
    }

}
