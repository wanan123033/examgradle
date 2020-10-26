package com.fairplay.examgradle.viewmodel;

import android.text.TextUtils;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.adapter.ExamAdapter;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.bean.ScoreBean;
import com.fairplay.examgradle.utils.ToastUtils;
import com.gwm.mvvm.BaseViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamViewModel extends BaseViewModel<Object> {
    public void onScore(ExamScoreBean examScoreBean, Item item, String score, ExamAdapter adapter) {
        if (examScoreBean.isLock && examScoreBean.resultList.size() == 1){
            ToastUtils.showShort("当前成绩已锁定,请解锁");
            return;
        }
        int currentPosition = examScoreBean.currentPosition;  //当前内层Recy的坐标值  当前选中的成绩
        ExamScoreBean.Score currentScore = examScoreBean.resultList.get(currentPosition);  //当前成绩
        if (currentScore.isLock){
            ToastUtils.showShort("当前成绩已锁定,请解锁");
            return;
        }
        if (scoreCheck(item, score, currentScore)) return;
        //输入正确,对结果赋值
        currentScore.result.append(score);
    }

    private boolean scoreCheck(Item item, String score, ExamScoreBean.Score currentScore) {
        if (item.getTestType() != 1 && !TextUtils.isEmpty(currentScore.result.toString())) {   //非计时项目
            //小数位数判断
            int digital = item.getDigital();
            if (currentScore.result.toString().contains(".")) {
                String s = currentScore.result.substring(currentScore.result.indexOf("."));
                if (s.length() + 1 > digital) {
                    ToastUtils.showShort("小数位数超过指定位数");
                    return true;
                }
                if (currentScore.result.toString().split(".").length + 1 > 2) {
                    ToastUtils.showShort("非法的值错误");
                    return true;
                }
            }
            try {
                //最小值判断
                if (Double.parseDouble(currentScore.result.toString()+ score) < item.getMinValue()) {
                    ToastUtils.showShort("当前项目最小成绩为:" + item.getMinValue());
                    return true;
                }
                //最大值判断
                if (Double.parseDouble(currentScore.result.toString() + score) > item.getMaxValue()) {
                    ToastUtils.showShort("当前项目最大成绩为:" + item.getMaxValue());
                    return true;
                }
                //项目最低分判断
                if (Double.parseDouble(currentScore.result.toString() + score) < item.getMinScore()) {
                    ToastUtils.showShort("当前项目最低分:" + item.getMinScore());
                    return true;
                }
            }catch (NumberFormatException e){
                ToastUtils.showShort("数据格式错误");
                return true;
            }

        }else if (!TextUtils.isEmpty(currentScore.result.toString())){  //计时类项目
            if (currentScore.result.toString().contains(":") && score.equals(":")){
                ToastUtils.showShort("非法的时间错误");
                return true;
            }
        }
        return false;
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

    public boolean saveScore(String studentCode, ExamScoreBean examScoreBean, Item item) {
        if (scoreCheck(item, "", examScoreBean.resultList.get(examScoreBean.resultList.size() - 1))) return false;
        RoundResult roundResult = new RoundResult();
        roundResult.setItemCode(item.getItemCode());
        roundResult.setSubitemCode(item.getSubitemCode());
        roundResult.setStudentCode(studentCode);
        roundResult.setRoundNo(examScoreBean.currentScorePosition);
        roundResult.setTestNo(item.getTestNum());
        roundResult.setTestTime(System.currentTimeMillis()+"");
        if (item.getScoreCount() == 1){
            roundResult.setIsMultioleResult(0);
        }else {
            roundResult.setIsMultioleResult(1);
        }
        if (item.getMarkScore() == 0){  //测量项目
            if (item.getTestType() == 1){  //计时类项目
                if (item.getScoreCount() == 1) {
                    ExamScoreBean.Score score = examScoreBean.resultList.get(0);
                    if (isLegalDate(score.result.toString())) {
                        roundResult.setScore(score.result.toString());
                        DBManager.getInstance().insertRoundResult(roundResult);
                    } else {
                        ToastUtils.showShort("时间格式错误");
                        return false;
                    }
                }else {
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    for (int i = 0 ; i < examScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        if (isLegalDate(examScoreBean.resultList.get(i).result.toString())) {
                            result.setScore(examScoreBean.resultList.get(i).result.toString());
                        } else {
                            ToastUtils.showShort("时间格式错误");
                            return false;
                        }
                        result.setDesc(examScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineScore(examScoreBean.resultList.get(i).result.toString());
                        result.setScore(examScoreBean.resultList.get(i).result.toString());
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }
            }else {
                if (item.getScoreCount() == 1) {
                    ExamScoreBean.Score score = examScoreBean.resultList.get(0);
                    roundResult.setMachineResult(Integer.parseInt(score.result.toString()));
                    //乘以比例系数
                    roundResult.setScore(String.valueOf(Integer.parseInt(score.result.toString()) * item.getRatio()));
                    DBManager.getInstance().insertRoundResult(roundResult);
                }else {
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    for (int i = 0 ; i < examScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        result.setDesc(examScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineScore(examScoreBean.resultList.get(i).result.toString());
                        //乘以比例系数
                        result.setScore(String.valueOf(Integer.parseInt(examScoreBean.resultList.get(i).result.toString()) * item.getRatio()));
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }

            }

        }else if (item.getMarkScore() == 1){  //打分项目
            if (item.getTestType() == 1){  //计时类项目
                if (item.getScoreCount() == 1) {
                    ExamScoreBean.Score score = examScoreBean.resultList.get(0);
                    if (isLegalDate(score.result.toString())) {
                        roundResult.setResult(score.result.toString());
                        DBManager.getInstance().insertRoundResult(roundResult);
                    } else {
                        ToastUtils.showShort("时间格式错误");
                        return false;
                    }
                }else {
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    for (int i = 0 ; i < examScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        if (isLegalDate(examScoreBean.resultList.get(i).result.toString())) {
                            result.setResult(examScoreBean.resultList.get(i).result.toString());
                        } else {
                            ToastUtils.showShort("时间格式错误");
                            return false;
                        }
                        result.setDesc(examScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineResult(examScoreBean.resultList.get(i).result.toString());
                        result.setResult(examScoreBean.resultList.get(i).result.toString());
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }
            }else {
                if (item.getScoreCount() == 1) {
                    ExamScoreBean.Score score = examScoreBean.resultList.get(0);
                    roundResult.setMachineResult(Integer.parseInt(score.result.toString()));
                    //乘以比例系数
                    roundResult.setResult(String.valueOf(Integer.parseInt(score.result.toString()) * item.getRatio()));
                    DBManager.getInstance().insertRoundResult(roundResult);
                }else {
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    for (int i = 0 ; i < examScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        result.setDesc(examScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineResult(examScoreBean.resultList.get(i).result.toString());
                        //乘以比例系数
                        result.setResult(String.valueOf(Integer.parseInt(examScoreBean.resultList.get(i).result.toString()) * item.getRatio()));
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }

            }
        }
        return true;
    }

    public void removeScore(ExamScoreBean examScoreBean) {
        int currentPosition = examScoreBean.currentPosition;  //当前内层Recy的坐标值  当前选中的成绩
        ExamScoreBean.Score currentScore = examScoreBean.resultList.get(currentPosition);  //当前成绩
        if(currentScore.result.length() >= 1)
            currentScore.result.deleteCharAt(currentScore.result.length() - 1);
    }
}
