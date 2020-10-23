package com.fairplay.examgradle.viewmodel;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_exam_score;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.MultipleResult;
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

    public void saveResult(final ScoreBean bean, activity_exam_score mBinding, int maxLine, String itemCode, boolean isFlag){
        int selPos = 0;
        if (mBinding.stu_info.rv_score_data.getAdapter() instanceof OneScoreAdapter){
            selPos = ((OneScoreAdapter) mBinding.stu_info.rv_score_data.getAdapter()).getSelectPosition();
        }else if (mBinding.stu_info.rv_score_data.getAdapter() instanceof TwoScoreAdapter){
            selPos = ((TwoScoreAdapter) mBinding.stu_info.rv_score_data.getAdapter()).getSelectPosition();
        }
        if (mBinding.stu_info.rv_score_data.getAdapter() instanceof OneScoreAdapter){
            bean.isLook = true;
            bean.twoPos = false;
            OneScoreAdapter adapter = (OneScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
            if (adapter.getSelectPosition() >= maxLine){
                adapter.setSelectPosition(-1);
            }
            adapter.setSelectPosition(adapter.getSelectPosition() + 1);
            selPos = adapter.getSelectPosition();
            mBinding.stu_info.rv_score_data.setAdapter(adapter);
            saveScore(mBinding.et_studentCode.getText().toString(),bean,selPos ,itemCode,isFlag);
        }else if (mBinding.stu_info.rv_score_data.getAdapter() instanceof TwoScoreAdapter){
            if (bean.twoPos){
                bean.isLook = true;
                TwoScoreAdapter adapter = (TwoScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
                if (adapter.getSelectPosition() >= maxLine){
                    adapter.setSelectPosition(-1);
                }
                adapter.setSelectPosition(adapter.getSelectPosition() + 1);
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
                selPos = adapter.getSelectPosition();
                saveScore2(mBinding.et_studentCode.getText().toString(),bean,selPos,itemCode,isFlag);

            }else {
                bean.isLook = false;
                bean.twoPos = true;
                TwoScoreAdapter adapter = (TwoScoreAdapter)mBinding.stu_info.rv_score_data.getAdapter();
                mBinding.stu_info.rv_score_data.setAdapter(adapter);
            }
        }
    }

    private void saveScore2(String studentCode, ScoreBean bean, int round, String itemCode, boolean isFlag) {
        RoundResult result = new RoundResult();
        result.setStudentCode(studentCode);
        result.setRoundNo(round);
        result.setItemCode(itemCode);
        result.setTestTime(System.currentTimeMillis()+"");
        result.setIsMultioleResult(1);
        result.setIsLastResult(1);
        Long roundResultId = DBManager.getInstance().insertRoundResult(result);
        MultipleResult multipleResult1 = new MultipleResult();
        multipleResult1.setRoundId(roundResultId);
        multipleResult1.setDesc("左");
        multipleResult1.setOrder("1");
        if (!isFlag) {
            multipleResult1.setResult(bean.result1.toString());
        }else {
            multipleResult1.setScore(bean.result1.toString());
        }
        Log.e("TAG",multipleResult1.toString());
        DBManager.getInstance().insertMultipResult(multipleResult1);
        MultipleResult multipleResult2 = new MultipleResult();
        multipleResult2.setRoundId(roundResultId);
        multipleResult2.setDesc("右");
        multipleResult2.setOrder("2");
        if (!isFlag) {
            multipleResult2.setResult(bean.result2.toString());
        }else {
            multipleResult2.setScore(bean.result2.toString());
        }
        DBManager.getInstance().insertMultipResult(multipleResult2);
        Log.e("TAG",multipleResult2.toString());
        ToastUtils.showLong("数据已保存到数据库");
    }

    private void saveScore(String studentCode, ScoreBean bean, int round, String itemCode, boolean isFlag){
        RoundResult result = new RoundResult();
        if (!isFlag) {
            result.setResult(bean.result1.toString());
        }else {
            result.setScore(bean.result1.toString());
        }
        result.setStudentCode(studentCode);
        result.setRoundNo(round);
        result.setItemCode(itemCode);
        result.setIsLastResult(1);
        result.setTestTime(System.currentTimeMillis()+"");
        DBManager.getInstance().insertRoundResult(result);

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
