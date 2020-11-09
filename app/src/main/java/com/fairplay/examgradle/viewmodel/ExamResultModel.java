package com.fairplay.examgradle.viewmodel;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.bean.ScoreBean;
import com.gwm.mvvm.BaseViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamResultModel extends BaseViewModel<Object> {
    public void onScore(ExamScoreBean currentScoreBean, String score, Item item) {
        int currentPosition = currentScoreBean.currentPosition;  //当前内层Recy的坐标值  当前选中的成绩
        ExamScoreBean.Score currentScore = currentScoreBean.resultList.get(currentPosition);  //当前成绩
        if (currentScore.isLock){
            ToastUtils.showShort("当前成绩已锁定,请解锁");
            return;
        }
        if (scoreCheck(item, score, currentScore)) return;
        //输入正确,对结果赋值
        currentScore.result.append(score);
    }

    /**
     * 检查输入的准确性
     * @param item  项目
     * @param score  输入的值
     * @param currentScore  当前成绩
     * @return
     */
    public boolean scoreCheck(Item item, String score, ExamScoreBean.Score currentScore) {
        if ("score".equals(item.getUnit()) && item.getMarkScore() == 0){  //测量的得分项目不做处理  输多少就是多少
            return false;
        }
        if (item.getTestType() != 1 && !TextUtils.isEmpty(currentScore.result.toString())) {   //非计时项目
            //小数位数判断
            int digital = item.getDigital();
            if (currentScore.result.toString().contains(".")) {
                String s = currentScore.result.substring(currentScore.result.indexOf(".") + 1);
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
                if (item.getMinValue() != null)
                    if (Double.parseDouble(currentScore.result.toString()+ score) < Double.parseDouble(item.getMinValue())) {
                        ToastUtils.showShort("当前项目最小成绩为:" + item.getMinValue());
                        return true;
                    }
                //最大值判断
                if (item.getMaxValue() != null)
                    if (Double.parseDouble(currentScore.result.toString() + score) > Double.parseDouble(item.getMaxValue())) {
                        ToastUtils.showShort("当前项目最大成绩为:" + item.getMaxValue());
                        return true;
                    }
                    else
                    if (Double.parseDouble(currentScore.result.toString() + score) > Double.parseDouble("9999")) {
                        ToastUtils.showShort("当前项目最大成绩为:9999");
                        return true;
                    }
                //项目最低分判断
                if (item.getMinScore() != null)
                    if (Double.parseDouble(currentScore.result.toString() + score) < Double.parseDouble(item.getMinScore())) {
                        ToastUtils.showShort("当前项目最低分:" + item.getMinScore());
                        return true;
                    }
            }catch (NumberFormatException e){
                ToastUtils.showShort("数据格式错误");
                return true;
            }catch (NullPointerException e){
                e.printStackTrace();
                return true;
            }

        }else if (!TextUtils.isEmpty(currentScore.result.toString())){  //计时类项目
            if (currentScore.result.toString().contains(":") && score.equals(":") || score.equals(".")){
                ToastUtils.showShort("非法的时间错误");
                return true;
            }
        }
        return false;
    }

    /**
     * 保存成绩并发送成绩
     * @param currentScoreBean  成绩bean
     * @param item  项目
     * @param currentRoundNo  当前轮次
     */
    public RoundResult saveResult(ExamScoreBean currentScoreBean, Item item, int currentRoundNo) {
//        if (scoreCheck(item, "", currentScoreBean.resultList.get(currentScoreBean.resultList.size() - 1))) return null;
        RoundResult roundResult = new RoundResult();
        roundResult.setScheduleNo("1");
        roundResult.setItemName(item.getItemName());
        roundResult.setTestNo(item.getRoundNum());
        roundResult.setItemCode(item.getItemCode());
        roundResult.setSubitemCode(item.getSubitemCode());
        roundResult.setStudentCode(currentScoreBean.studentCode);
        roundResult.setRoundNo(currentRoundNo + 1);
        roundResult.setResultState(1);
        if (item.getMachineCode() != null)
            roundResult.setMachineCode(Integer.parseInt(item.getMachineCode()));
//        roundResult.setRoundNo(examScoreBean.currentScorePosition);
        roundResult.setTestNo(item.getTestNum());
        roundResult.setTestTime(System.currentTimeMillis()+"");
        //根据界面上的打分个数判断是否需要启用多值属性
        if (currentScoreBean.resultList.size() == 1){
            roundResult.setIsMultioleResult(0);
        }else {
            roundResult.setIsMultioleResult(1);
        }
        if (item.getMarkScore() == 0){  //测量项目
            if (item.getTestType() == 1){  //计时类项目
                if (roundResult.getIsMultioleResult() == 0) {     //单值类
                    ExamScoreBean.Score score = currentScoreBean.resultList.get(0);
                    if (isLegalDate(score.result.toString())) {

                        roundResult.setResult(getScoend(score.result.toString())+"");
                        roundResult.setMachineResult(getScoend(score.result.toString())+"");
                        Long aLong = DBManager.getInstance().insertRoundResult(roundResult);
                        roundResult.setId(aLong);
                        getLiveData().postValue(roundResult);
                        return roundResult;
                    } else {
                        ToastUtils.showShort("时间格式错误");
                        return null;
                    }
                }else {      //多值类
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(roundResultId);
                    for (int i = 0 ; i < currentScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        if (isLegalDate(currentScoreBean.resultList.get(i).result.toString())) {

                        } else {
                            ToastUtils.showShort("时间格式错误");
                            return null;
                        }
                        result.setDesc(currentScoreBean.resultList.get(i).desc);
                        result.setOrder(currentScoreBean.resultList.get(i).order);
                        result.setUnit(item.getUnit());
                        result.setMachineScore(getScoend(currentScoreBean.resultList.get(i).result.toString())+"");
                        result.setScore(getScoend(currentScoreBean.resultList.get(i).result.toString())+"");
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }
            }else {     //测量项目非计时类
                if (roundResult.getIsMultioleResult() == 0) {   //单值类项目
                    ExamScoreBean.Score score = currentScoreBean.resultList.get(0);
                    roundResult.setMachineResult(score.result.toString());
                    //乘以比例系数
                    if (item.getRatio() != 0.0)
                        roundResult.setResult(String.valueOf(Double.parseDouble(score.result.toString()) * item.getRatio()));
                    else
                        roundResult.setResult(String.valueOf(Double.parseDouble(score.result.toString())));
                    Long aLong = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(aLong);
                }else {//多值类项目
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(roundResultId);
                    for (int i = 0 ; i < currentScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        result.setDesc(currentScoreBean.resultList.get(i).desc);
                        result.setOrder(currentScoreBean.resultList.get(i).order);
                        result.setUnit(item.getUnit());
                        result.setMachineScore(currentScoreBean.resultList.get(i).result.toString());
                        //乘以比例系数
                        if (item.getRatio() != 0.0)
                            result.setScore(String.valueOf(Double.parseDouble(currentScoreBean.resultList.get(i).result.toString()) * item.getRatio()));
                        else
                            result.setScore(String.valueOf(Double.parseDouble(currentScoreBean.resultList.get(i).result.toString())));
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }

            }

        }else if (item.getMarkScore() == 1){  //打分项目
            if (item.getTestType() == 1){  //计时类项目
                if (roundResult.getIsMultioleResult() == 0) { //单值类项目
                    ExamScoreBean.Score score = currentScoreBean.resultList.get(0);
                    if (isLegalDate(score.result.toString())) {
                        roundResult.setScore(getScoend(score.result.toString())+"");
                        roundResult.setMachineScore(getScoend(score.result.toString())+"");
                        Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                        roundResult.setId(roundResultId);
                    } else {
                        ToastUtils.showShort("时间格式错误");
                        return null;
                    }
                }else {        //多值类项目
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(roundResultId);
                    for (int i = 0 ; i < currentScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        if (isLegalDate(currentScoreBean.resultList.get(i).result.toString())) {

                        } else {
                            ToastUtils.showShort("时间格式错误");
                            return null;
                        }
                        result.setDesc(currentScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineScore(getScoend(currentScoreBean.resultList.get(i).result.toString())+"");
                        result.setScore(getScoend(currentScoreBean.resultList.get(i).result.toString())+"");
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }
            }else {       //非计时类项目
                if (roundResult.getIsMultioleResult() == 0) {      //单值类
                    ExamScoreBean.Score score = currentScoreBean.resultList.get(0);
                    roundResult.setMachineScore(score.result.toString());
                    //乘以比例系数
                    if (item.getRatio() != 0.0)
                        roundResult.setScore(String.valueOf(Double.parseDouble(score.result.toString()) * item.getRatio()));
                    else
                        roundResult.setScore(score.result.toString());
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(roundResultId);
                }else {   //多值类
                    Long roundResultId = DBManager.getInstance().insertRoundResult(roundResult);
                    roundResult.setId(roundResultId);
                    for (int i = 0 ; i < currentScoreBean.resultList.size() ; i++){
                        MultipleResult result = new MultipleResult();
                        result.setRoundId(roundResultId);
                        result.setDesc(currentScoreBean.resultList.get(i).desc);
                        result.setOrder(i+"");
                        result.setMachineScore(currentScoreBean.resultList.get(i).result.toString());
                        //乘以比例系数
                        if (item.getRatio() != 0.0)
                            result.setScore(String.valueOf(Double.parseDouble(currentScoreBean.resultList.get(i).result.toString()) * item.getRatio()));
                        else
                            result.setScore(currentScoreBean.resultList.get(i).result.toString());
                        DBManager.getInstance().insertMultipResult(result);
                    }
                }

            }
        }
        getLiveData().postValue(roundResult);
        return roundResult;
    }
    public void removeScore(ExamScoreBean examScoreBean) {
        int currentPosition = examScoreBean.currentPosition;  //当前内层Recy的坐标值  当前选中的成绩
        ExamScoreBean.Score currentScore = examScoreBean.resultList.get(currentPosition);  //当前成绩
        if(currentScore.result.length() >= 1)
            currentScore.result.deleteCharAt(currentScore.result.length() - 1);
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
    private static long getScoend(String sDate) {
        String[] split = sDate.split(":");
        return Long.parseLong(split[0])*60L + Long.parseLong(split[1]);
    }

}
