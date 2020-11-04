package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ExamAdapter;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.examgradle.contract.MBMContract;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.service.ScoreUploadServie;
import com.fairplay.examgradle.viewmodel.ExamViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.messagebus.Subscrition;
import com.gwm.base.BaseApplication;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.tencent.mmkv.MMKV;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamActivity extends BaseMvvmTitleActivity<Object, ExamViewModel, activity_exam_score> {

    private Item item;
    private ExamAdapter adapter;
    private ExamScoreBean examScoreBean;
    private List<ExamScoreBean> examScoreBeanList;

//    private String itemCode;
//    private String subitemCode;
    MqttBean mqttBean;
    private int roundNo;

    public static final String ACTION = "com.fariplay.examgradle.add_stu";
    @Override
    protected Class<ExamViewModel> getViewModelClass() {
        return ExamViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        examScoreBeanList = new ArrayList<>();
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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

    /**
     * {"channelCode":"TEST-BASDF","data":{"examPlaceName":"广西民族大学西校区篮球馆","examStatus":0,"groupNo":"1","groupType":"0","itemCode":"0300","scheduleNo":"1","sortName":"组","studentCode":"052180310","subitemCode":"03001","trackNo":"1"},"id":1604396768022,"matchUser":0,"messageType":1}
     * @param message
     */
    private void packMsg(String message) {
        Log.e("TAG====>",message);
        try {
            if (examScoreBean == null && examScoreBeanList.isEmpty()) {
                JSONObject jsonObject = new JSONObject(message);
                int messageType = jsonObject.getInt("messageType");
                if (messageType == 1) {
                    examScoreBeanList = new ArrayList<>();
                    examScoreBeanList.clear();
                    mqttBean = GsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), MqttBean.class);
                    long mqtt_id = DBManager.getInstance().insertMqttBean(mqttBean);
                    item = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(), mqttBean.getSubitemCode());
                    titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
                    roundNo = 1;
                    initItemScore2();
                    MMKV mmkv = BaseApplication.getInstance().getMmkv();
                    mmkv.putLong(MMKVContract.MQTT_ID, mqtt_id);
                    mBinding.stu_info.tv_studentCode.setText(mqttBean.getStudentCode());
                    initStudent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initItemScore2(){
        List<MultipleItem> multipleItemList = DBManager.getInstance().getMultipleItemList(item.getId());
        ExamScoreBean scoreBean = new ExamScoreBean();
        scoreBean.item = item;
        scoreBean.resultList = new ArrayList<>();
        if ((multipleItemList == null || multipleItemList.isEmpty()) && item.getRoundNum() != 0 ){   //没有多值属性时给一个框框
            ExamScoreBean.Score score = new ExamScoreBean.Score();
            score.desc = "成绩";
            score.result = new StringBuffer();
            score.unit = item.getUnit();
            scoreBean.resultList.add(score);
        }else {
            for (MultipleItem multipleItem : multipleItemList){
                ExamScoreBean.Score score = new ExamScoreBean.Score();
                score.desc = multipleItem.getDesc();
                score.result = new StringBuffer();
                score.unit = item.getUnit();
                scoreBean.resultList.add(score);
                score.order = multipleItem.getOrder();
            }
        }
        examScoreBeanList.add(scoreBean);
        adapter = new ExamAdapter(this,examScoreBeanList);
        adapter.setSelectPosition(0);
        examScoreBean = examScoreBeanList.get(0);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
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
                viewModel.onScore(examScoreBean,item,"0",adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_1:
                viewModel.onScore(examScoreBean,item,"1", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_2:
                viewModel.onScore(examScoreBean,item,"2", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_3:
                viewModel.onScore(examScoreBean,item,"3", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_4:
                viewModel.onScore(examScoreBean,item,"4", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_5:
                viewModel.onScore(examScoreBean,item,"5", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_6:
                viewModel.onScore(examScoreBean,item,"6", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_7:
                viewModel.onScore(examScoreBean,item,"7", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_8:
                viewModel.onScore(examScoreBean,item,"8", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_9:
                viewModel.onScore(examScoreBean,item,"9", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_dian:
                viewModel.onScore(examScoreBean,item,".", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv:
                viewModel.onScore(examScoreBean,item,":", adapter);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tvj:
                break;
            case R.id.tv_enter1:
                quitCommit();
                break;
            case R.id.tv_enter2:
                viewModel.removeScore(examScoreBean);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_send:
                quitScore();
                break;
            case R.id.btn_score1:
                mBinding.stu_info.tv_studentCode.setText(mBinding.et_studentCode.getText().toString());
                break;
        }
    }

    private void quitCommit() {
        examScoreBeanList = new ArrayList<>();
        examScoreBean = null;
        mBinding.stu_info.tv_studentCode.setText("");
        adapter = new ExamAdapter(this,examScoreBeanList);
        adapter.clearData();
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }

    private void quitScore() {
        String studentCode = mBinding.stu_info.tv_studentCode.getText().toString();
        boolean isLast = examScoreBean.currentPosition == (examScoreBean.resultList.size() - 1);
        if (isLast){
            boolean isSave = viewModel.saveScore(studentCode, examScoreBean, item,roundNo);
            if (isSave){
                ToastUtils.showShort("数据保存成功");
                if (item.getRoundNum() > roundNo){
                    roundNo++;
//                    mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    initItemScore2();
                }else {
//                    quitCommit();
                    roundNo = 0;
                }
            }else {
                ToastUtils.showShort("数据保存失败");
            }
        }else {
            if (!viewModel.scoreCheck(item,"",examScoreBean.resultList.get(examScoreBean.currentPosition))) {
                examScoreBean.resultList.get(examScoreBean.currentPosition).isLock = true;  //锁住当前成绩
                examScoreBean.currentPosition = examScoreBean.currentPosition + 1;
                adapter.notifyDataSetChanged();
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
            }
        }
    }

    private void initStudent() {
        //2.添加学生
        Student student = new Student();
        student.setStudentCode(mBinding.et_studentCode.getText().toString());
//        student.setStudentName("学生n");
//        student.setSex(0);
//        student.setSchoolName("sss");
        DBManager.getInstance().insertStudent(student);
        //3.添加报名
        StudentItem studentItem = new StudentItem();
        studentItem.setSubitemCode(mqttBean.getItemCode());
        studentItem.setItemCode(mqttBean.getSubitemCode());
        studentItem.setMachineCode(item.getMachineCode());
        studentItem.setStudentCode(mBinding.et_studentCode.getText().toString());
        DBManager.getInstance().insertStudentItem(studentItem);
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof RoundResult){ //上传成绩
            Intent intent = new Intent(getApplicationContext(), ScoreUploadServie.class);
            intent.putExtra(ScoreUploadServie.SCHEDULEID,4L);
            intent.putExtra(ScoreUploadServie.ITEMID,item.getId().longValue());
            intent.putExtra(ScoreUploadServie.GROUPID,1L);
            intent.putExtra(ScoreUploadServie.ROUNDID,((RoundResult) o).getId().longValue());
            intent.putExtra(ScoreUploadServie.STUDENTCODE,((RoundResult) o).getStudentCode());
            startService(intent);
        }
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        if (item != null){
            Item item1 = DBManager.getInstance().getItemByItemCode(item.getItemCode(), item.getItemCode());
            builder.setTitle(item1.getItemName()+" - "+item.getItemName());
        }
        return super.setTitleBarBuilder(builder);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("确认返回吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscrition(action = MBMContract.UN_LOCK)
    public void onEventMessage(MessageBusMessage mbmessage){
        int postion = (int) mbmessage.getData();
        examScoreBean.currentPosition = postion;
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }
}
