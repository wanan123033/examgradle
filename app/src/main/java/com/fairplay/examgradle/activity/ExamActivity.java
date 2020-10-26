package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ExamAdapter;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.viewmodel.ExamViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_exam_score)
public class ExamActivity extends BaseMvvmTitleActivity<Object, ExamViewModel, activity_exam_score> {

    private Item item;
    private ExamAdapter adapter;
    private ExamScoreBean examScoreBean;
    private List<ExamScoreBean> examScoreBeanList;

    private String itemCode;
    @Override
    protected Class<ExamViewModel> getViewModelClass() {
        return ExamViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemCode = getIntent().getStringExtra("itemCode");
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_ITEM,itemCode);
        BaseApplication.getInstance().getMmkv().putString(MMKVContract.CURRENT_SUB_ITEM,itemCode);
        item = DBManager.getInstance().getItemByItemCode(itemCode);
        mBinding.stu_info.rv_score_data.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
//        initItemScore();
    }

    private void initItemScore() {
        examScoreBeanList = new ArrayList<>();
        for (int i = 0 ; i < item.getTestNum() ; i++){
            ExamScoreBean scoreBean = new ExamScoreBean();
            scoreBean.item = item;
            scoreBean.currentScorePosition = 0;
            scoreBean.currentPosition = 0;
            scoreBean.resultList = new ArrayList<>();
            for (int j = 0 ; j < item.getScoreCount() ; j++){
                ExamScoreBean.Score score = new ExamScoreBean.Score();
                score.desc = "成绩"+j;
                score.unit = item.getUnit();
                score.result = new StringBuffer();
                scoreBean.resultList.add(score);
            }
            examScoreBeanList.add(scoreBean);
        }
        adapter = new ExamAdapter(this,examScoreBeanList);
        adapter.setSelectPosition(0);
        examScoreBean = examScoreBeanList.get(0);
        mBinding.stu_info.rv_score_data.setAdapter(adapter);
    }

    @OnClick({R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.tv_9,
            R.id.tv_dian,R.id.tv,R.id.tvj,R.id.tv_enter1,R.id.tv_enter2,R.id.tv_send,R.id.btn_score1})
    public void onClick(View view){
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
                break;
            case R.id.tv_enter2:
                viewModel.removeScore(examScoreBean);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                break;
            case R.id.tv_send:
                if(examScoreBean.currentPosition == examScoreBean.resultList.size() - 1) {
                    if (viewModel.saveScore(mBinding.stu_info.tv_studentCode.getText().toString(),examScoreBean,item)) {
                        if (item.getTestNum() == (adapter.getSelectPosition() + 1)) {
                            adapter.setSelectPosition(-1);
                        }
                        examScoreBean = examScoreBeanList.get(adapter.getSelectPosition() + 1);
                        adapter.setSelectPosition(adapter.getSelectPosition() + 1);
                        adapter.notifyDataSetChanged();
                    }
                }else if (examScoreBean.currentPosition < examScoreBean.resultList.size() - 1){
                    examScoreBean.currentPosition++;
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_score1:
                initStudent();
                mBinding.stu_info.tv_studentCode.setText(mBinding.et_studentCode.getText().toString());
                initItemScore();
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
}
