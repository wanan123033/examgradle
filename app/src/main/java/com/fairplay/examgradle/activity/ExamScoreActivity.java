package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.RoundResult;
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
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamScoreActivity extends BaseMvvmTitleActivity<Object, ExamScoreViewModel, activity_exam_score>{

    private OneScoreAdapter oneScoreAdapter;
    private TwoScoreAdapter twoScoreAdapter;
    private List<ScoreBean> scoreBeans;
    private ScoreBean bean;

    private static final int maxLine = 3;
    private String itemCode = "12";

    @Override
    protected Class<ExamScoreViewModel> getViewModelClass() {
        return ExamScoreViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return builder.setTitle("足球")
                .setLeftText("返回")
                .setLeftImageResource(R.mipmap.icon_white_goback)
                .setLeftImageVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_ITEM,itemCode);
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_SUB_ITEM,itemCode);
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    }

    @OnClick({R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.tv_9,
            R.id.tv_dian,R.id.tv,R.id.tvj,R.id.tv_enter1,R.id.tv_enter2,R.id.tv_send,R.id.btn_score1,R.id.btn_score2})
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
                break;
            case R.id.tvj:
                bean.isLook = false;
                ToastUtils.showLong("已解锁");
                break;
            case R.id.tv_enter1:
                break;
            case R.id.tv_enter2:
                break;
            case R.id.tv_send:
                viewModel.saveResult(bean,mBinding,maxLine,itemCode);
                BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mBinding.stu_info.rv_score_data.getAdapter();
                if (adapter instanceof OneScoreAdapter){
                    bean = scoreBeans.get(((OneScoreAdapter) adapter).getSelectPosition());
                }else {
                    bean = scoreBeans.get(((TwoScoreAdapter) adapter).getSelectPosition());
                }

                break;
            case R.id.btn_score1:
                initStudent();
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
            case R.id.btn_score2:
                initStudent();
                scoreBeans = new ArrayList<>();
                for (int i = 0 ; i < maxLine ; i++){
                    ScoreBean scoreBean = new ScoreBean();
                    scoreBeans.add(scoreBean);
                }
                twoScoreAdapter = new TwoScoreAdapter(this,scoreBeans);
                bean = scoreBeans.get(0);
                mBinding.stu_info.rv_score_data.setAdapter(twoScoreAdapter);
                mBinding.stu_info.tv_studentCode.setText(mBinding.et_studentCode.getText().toString());
                break;
        }
    }

    private void initStudent() {
        //1.添加项目
        Item item = new Item();
        item.setItemCode(itemCode);
        item.setSubitemCode(itemCode);
        item.setItemName("项目1");
        DBManager.getInstance().insertItem(item);

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
}
