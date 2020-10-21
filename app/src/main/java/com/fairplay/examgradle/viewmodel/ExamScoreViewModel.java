package com.fairplay.examgradle.viewmodel;

import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.adapter.OneScoreAdapter;
import com.fairplay.examgradle.adapter.TwoScoreAdapter;
import com.fairplay.examgradle.bean.ScoreBean;
import com.gwm.base.BaseRecyclerViewAdapter;
import com.gwm.mvvm.BaseViewModel;

import java.util.List;

public class ExamScoreViewModel extends BaseViewModel<Object> {

    public void onScore(ScoreBean bean, activity_exam_score mBinding, String score){
        if (bean == null){
            return;
        }
        if (bean.isLook){
            ToastUtils.showLong("请先解锁");
            return;
        }
        if (mBinding.stu_info.rv_score_data.getAdapter() instanceof OneScoreAdapter){
            bean.result1.append(score);
            mBinding.stu_info.rv_score_data.setAdapter(mBinding.stu_info.rv_score_data.getAdapter());
        }else {
            if (bean.twoPos){
                bean.result2.append(score);
            }else {
                bean.result1.append(score);
            }
            mBinding.stu_info.rv_score_data.setAdapter(mBinding.stu_info.rv_score_data.getAdapter());
        }
    }

    public void saveResult(ScoreBean bean, activity_exam_score mBinding,int maxLine,String itemCode){
        List<ScoreBean> scoreBeans = ((BaseRecyclerViewAdapter)mBinding.stu_info.rv_score_data.getAdapter()).getData();

        int selPos = 0;
        if (mBinding.stu_info.rv_score_data.getAdapter() instanceof OneScoreAdapter){
            selPos = ((OneScoreAdapter) mBinding.stu_info.rv_score_data.getAdapter()).getSelectPosition();
        }else {
            selPos = ((TwoScoreAdapter) mBinding.stu_info.rv_score_data.getAdapter()).getSelectPosition();
        }
        if (mBinding.stu_info.rv_score_data.getAdapter() instanceof OneScoreAdapter){
            bean.isLook = true;
            OneScoreAdapter adapter = (OneScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
            if (adapter.getSelectPosition() >= maxLine){
                adapter.setSelectPosition(-1);
            }
            adapter.setSelectPosition(adapter.getSelectPosition() + 1);
            selPos = adapter.getSelectPosition();
            mBinding.stu_info.rv_score_data.setAdapter(adapter);
            saveScore(mBinding.et_studentCode.getText().toString(),bean,selPos ,itemCode);
        }else {
            if (bean.twoPos){
                bean.isLook = true;
                TwoScoreAdapter adapter = (TwoScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
                if (adapter.getSelectPosition() >= maxLine){
                    adapter.setSelectPosition(-1);
                }
                adapter.setSelectPosition(adapter.getSelectPosition() + 1);
                bean = scoreBeans.get(adapter.getSelectPosition());
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                selPos = adapter.getSelectPosition();
                saveScore(mBinding.et_studentCode.getText().toString(),bean,selPos,itemCode);

            }else {
                bean.isLook = false;
                bean.twoPos = true;
                TwoScoreAdapter adapter = (TwoScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
            }
        }
    }
    private void saveScore(String studentCode, ScoreBean bean,int round,String itemCode){
        DBManager.getInstance().getRoundResult(studentCode,round);
        RoundResult result = new RoundResult();
        result.setResult(bean.result1.toString());
        result.setResult2(bean.result2.toString());
        result.setStudentCode(studentCode);
        result.setRoundNo(round);
        result.setItemCode(itemCode);
        result.setTestTime(System.currentTimeMillis()+"");
        DBManager.getInstance().insertRoundResult(result);
        ToastUtils.showLong("数据已保存到数据库");
    }

    public void removeScore(activity_exam_score mBinding) {
        RecyclerView.Adapter adapter = mBinding.stu_info.rv_score_data.getAdapter();
        if (adapter instanceof BaseRecyclerViewAdapter){
            List<ScoreBean> data = ((BaseRecyclerViewAdapter) adapter).getData();
            ScoreBean bean = null;
            if (adapter instanceof OneScoreAdapter){
                bean = data.get(((OneScoreAdapter) adapter).getSelectPosition());
            }else if (adapter instanceof TwoScoreAdapter){
                bean = data.get(((TwoScoreAdapter) adapter).getSelectPosition());
            }
            if (bean.twoPos && bean.result2.length() > 0){
                bean.result2.deleteCharAt(bean.result2.length() - 1);
            }else if (bean.result1.length() > 0){
                bean.result1.deleteCharAt(bean.result1.length() - 1);
            }
            mBinding.stu_info.rv_score_data.setAdapter(adapter);
        }
    }
}
