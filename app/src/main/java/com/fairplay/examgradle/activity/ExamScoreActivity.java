package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.OneScoreAdapter;
import com.fairplay.examgradle.adapter.TwoScoreAdapter;
import com.fairplay.examgradle.bean.ScoreBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.viewmodel.ExamScoreViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.base.BaseRecyclerViewAdapter;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamScoreActivity extends BaseMvvmTitleActivity<Object, ExamScoreViewModel, activity_exam_score>{

    private OneScoreAdapter oneScoreAdapter;
    private TwoScoreAdapter twoScoreAdapter;
    private List<ScoreBean> scoreBeans;
    private ScoreBean bean;
    private boolean isFlag = false;

    private static final int maxLine = 3;
    private String itemCode = "22";

    private boolean isTime = false;

    @Override
    protected Class<ExamScoreViewModel> getViewModelClass() {
        return ExamScoreViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("足球"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_ITEM,itemCode);
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_SUB_ITEM,itemCode);
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    }

    @OnClick({R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.tv_9,
            R.id.tv_dian,R.id.tv,R.id.tvj,R.id.tv_enter1,R.id.tv_enter2,R.id.tv_send,R.id.btn_score1})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_0:
                viewModel.onScore(bean,mBinding,"0");
                break;
            case R.id.tv_1:
                viewModel.onScore(bean,mBinding,"1");
                break;
            case R.id.tv_2:
                viewModel.onScore(bean,mBinding,"2");
                break;
            case R.id.tv_3:
                viewModel.onScore(bean,mBinding,"3");
                break;
            case R.id.tv_4:
                viewModel.onScore(bean,mBinding,"4");
                break;
            case R.id.tv_5:
                viewModel.onScore(bean,mBinding,"5");
                break;
            case R.id.tv_6:
                viewModel.onScore(bean,mBinding,"6");
                break;
            case R.id.tv_7:
                viewModel.onScore(bean,mBinding,"7");
                break;
            case R.id.tv_8:
                viewModel.onScore(bean,mBinding,"8");
                break;
            case R.id.tv_9:
                viewModel.onScore(bean,mBinding,"9");
                break;
            case R.id.tv_dian:
                viewModel.onScore(bean,mBinding,".");
                break;
            case R.id.tv:
                viewModel.onScore(bean,mBinding,":");
                break;
            case R.id.tvj:
                new AlertDialog.Builder(this).setTitle("提示信息")
                        .setMessage("是否申请解锁")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bean.isLook = false;
                                ToastUtils.showLong("已解锁");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
            case R.id.tv_enter1:
                break;
            case R.id.tv_enter2:
                viewModel.removeScore(mBinding);
                break;
            case R.id.tv_send:
                if (isTime){
                    if (!bean.twoPos){
                        String time = bean.result1.toString();
                        boolean isdd = isLegalDate(time);
                        if (!isdd){
                            ToastUtils.showShort("时间格式不对,mm:ss.SSS");
                            break;
                        }
                    }else {
                        String time1 = bean.result2.toString();
                        boolean isdd1 = isLegalDate(time1);
                        if (!isdd1){
                            ToastUtils.showShort("时间格式不对,mm:ss.SSS");
                            break;
                        }
                    }

                }
                viewModel.saveResult(bean,mBinding,maxLine,itemCode,isFlag);
                BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mBinding.stu_info.rv_score_data.getAdapter();
                if (adapter instanceof OneScoreAdapter){
                    if (adapter.getItemCount() <= ((OneScoreAdapter) adapter).getSelectPosition()){
                        ((OneScoreAdapter) adapter).setSelectPosition(0);
                    }
                    bean = scoreBeans.get(((OneScoreAdapter) adapter).getSelectPosition());
                }else if (adapter instanceof TwoScoreAdapter){
                    if (adapter.getItemCount() <= ((TwoScoreAdapter) adapter).getSelectPosition()){
                        ((TwoScoreAdapter) adapter).setSelectPosition(0);
                    }
                    bean = scoreBeans.get(((TwoScoreAdapter) adapter).getSelectPosition());
                }

                break;
            case R.id.btn_score1:
                initStudent();
                isFlag = false;
                scoreBeans = new ArrayList<>();
                for (int i = 0 ; i < maxLine ; i++){
                    ScoreBean scoreBean = new ScoreBean();
                    scoreBeans.add(scoreBean);
                }
                bean = scoreBeans.get(0);
                oneScoreAdapter = new OneScoreAdapter(this,scoreBeans);
                mBinding.stu_info.rv_score_data.setAdapter(oneScoreAdapter);
                mBinding.stu_info.tv_studentCode.setText(mBinding.et_studentCode.getText().toString());
                break;
        }
    }

    private void initStudent() {
        //2.添加学生
        Student student = new Student();
        student.setStudentCode(mBinding.et_studentCode.getText().toString());
        student.setStudentName("学生n");
        student.setSex(0);
        student.setSchoolName("sss");
        DBManager.getInstance().insertStudent(student);
        //3.添加报名
        StudentItem studentItem = new StudentItem();
        studentItem.setSubitemCode(itemCode);
        studentItem.setItemCode(itemCode);
        studentItem.setStudentCode(mBinding.et_studentCode.getText().toString());
        DBManager.getInstance().insertStudentItem(studentItem);
    }


    public void scoreOne(int position, ScoreBean scoreBean) {
        for (int i = 0 ; i < scoreBeans.size() ; i++){
            scoreBeans.get(i).twoPos = false;
        }
        scoreBeans.get(position).twoPos = false;
        twoScoreAdapter.setSelectPosition(position);
        bean = scoreBeans.get(position);
        mBinding.stu_info.rv_score_data.setAdapter(twoScoreAdapter);
    }

    public void scoreTwo(int position, ScoreBean scoreBean) {
        for (int i = 0 ; i < scoreBeans.size() ; i++){
            scoreBeans.get(i).twoPos = false;
        }
        scoreBeans.get(position).twoPos = true;
        twoScoreAdapter.setSelectPosition(position);
        bean = scoreBeans.get(position);
        mBinding.stu_info.rv_score_data.setAdapter(twoScoreAdapter);
    }
    private static boolean isLegalDate(String sDate) {
        DateFormat formatter = new SimpleDateFormat(ScoreBean.dataFormat);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            ToastUtils.showLong("正在打分中...");
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
