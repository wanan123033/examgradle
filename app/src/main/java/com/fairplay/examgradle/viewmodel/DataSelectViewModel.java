package com.fairplay.examgradle.viewmodel;

import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.ExamPlace;
import com.fairplay.database.entity.GroupInfo;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.contract.Unit;
import com.feipulai.common.db.DataBaseExecutor;
import com.feipulai.common.db.DataBaseRespon;
import com.feipulai.common.db.DataBaseTask;
import com.gwm.mvvm.BaseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSelectViewModel extends BaseViewModel<Object> {
    public void selectAll(String itemCode, String subItemCode, Schedule schedule, ExamPlace examPlace, GroupInfo groupInfo, int pageNum) {

        if (schedule == null){
            ToastUtils.showShort("请选择日程");
            return;
        }
        if (examPlace == null){
            ToastUtils.showShort("请选择场地");
            return;
        }
        if (itemCode == null || subItemCode == null){
            ToastUtils.showShort("请选择项目");
            return;
        }

        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<StudentGroupItem> mqttBeans = null;
                if (groupInfo == null) {
                    mqttBeans = DBManager.getInstance().getMQTTBean(itemCode, subItemCode, schedule.getScheduleNo(), examPlace.getExamplaceName());
                }else {
                    mqttBeans = DBManager.getInstance().getMQTTBean(itemCode, subItemCode, schedule.getScheduleNo(), examPlace.getExamplaceName(),groupInfo);
                }
                List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
                Item item = DBManager.getInstance().getItemByItemCode(itemCode,itemCode);
                Item subItem = DBManager.getInstance().getItemByItemCode(itemCode,subItemCode);
                for (StudentGroupItem mqttBean : mqttBeans){
                    DataRtiveBean dataRtiveBean = new DataRtiveBean();
                    dataRtiveBean.studentCode = mqttBean.getStudentCode();
                    dataRtiveBean.itemName = item.getItemName()+"-"+subItem.getItemName();
                    dataRtiveBean.examPlaceName = mqttBean.getExamPlaceName();
                    dataRtiveBean.itemCode = itemCode;
                    dataRtiveBean.subItemCode = subItemCode;
                    dataRtiveBean.scheduleNo = schedule.getScheduleNo();
                    List<RoundResult> stuRoundResult = null;
                    if (groupInfo == null) {
                        stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(),
                                mqttBean.getItemCode(), mqttBean.getSubitemCode(),
                                schedule.getScheduleNo(),
                                examPlace.getExamplaceName());
                    }else {
                        stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(),
                                mqttBean.getItemCode(), mqttBean.getSubitemCode(),
                                schedule.getScheduleNo(),
                                examPlace.getExamplaceName(),groupInfo);

                    }
                    for (RoundResult result : stuRoundResult){
                        if (result.getIsMultioleResult() == 0){   //单值项目
                            if (subItem.getMarkScore() == 1){  //打分项目
                                dataRtiveBean.score = result.getScore();
                            }else {  //测量项目
                                dataRtiveBean.result = result.getResult() + Unit.getUnit(subItem.getUnit()).getDesc();
                            }
                        }else {  //多值项目
                            StringBuffer stringBuffer = new StringBuffer();
                            List<MultipleResult> results = DBManager.getInstance().getMultioleResult(result.getId());
                            for (MultipleResult multipleResult : results){
                                stringBuffer.append(multipleResult.getDesc() + ":" + multipleResult.getScore()+" ");
                            }
                            if (subItem.getMarkScore() == 1){  //打分项目
                                dataRtiveBean.score = stringBuffer.toString();
                            }else {  //测量项目
                                dataRtiveBean.result = stringBuffer.toString();
                            }
                        }
                    }
                    if (!dataRtiveBeans.contains(dataRtiveBean))
                        dataRtiveBeans.add(dataRtiveBean);
                }
                Map<String, Object> itemStudenCount = DBManager.getInstance().getItemStudenCount(itemCode, subItemCode);
                Integer count = (int) itemStudenCount.get("count");
                postValue(count);
                return new DataBaseRespon(true,"",dataRtiveBeans);
            }
            @Override
            public void onExecuteSuccess(DataBaseRespon respon) {
                List<DataRtiveBean> mqttBeans = (List<DataRtiveBean>) respon.getObject();
                Log.e("TAG====>",mqttBeans.toString());
                postValue(mqttBeans);
            }

            @Override
            public void onExecuteFail(DataBaseRespon respon) {

            }
        });

    }

    public void searchStuCode(String stuCode,String itemCode, String subItemCode, Schedule schedule, ExamPlace examPlace, GroupInfo groupInfo) {
        if (schedule == null){
            ToastUtils.showShort("请选择日程");
            return;
        }
        if (examPlace == null){
            ToastUtils.showShort("请选择场地");
            return;
        }
        if (itemCode == null || subItemCode == null){
            ToastUtils.showShort("请选择项目");
            return;
        }

        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<StudentGroupItem> mqttBeans = null;
                if (groupInfo == null) {
                    mqttBeans = DBManager.getInstance().getMQTTBean(stuCode,itemCode, subItemCode, schedule.getScheduleNo(), examPlace.getExamplaceName());
                }else {
                    mqttBeans = DBManager.getInstance().getMQTTBean(stuCode,itemCode, subItemCode, schedule.getScheduleNo(), examPlace.getExamplaceName(),groupInfo);
                }
                List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
                for (StudentGroupItem mqttBean : mqttBeans){
                    Item item = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(),mqttBean.getItemCode());
                    Item subItem = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(),mqttBean.getSubitemCode());
                    DataRtiveBean dataRtiveBean = new DataRtiveBean();
                    dataRtiveBean.studentCode = mqttBean.getStudentCode();
                    dataRtiveBean.itemName = item.getItemName()+"-"+subItem.getItemName();
                    dataRtiveBean.examPlaceName = mqttBean.getExamPlaceName();
                    dataRtiveBean.itemCode = item.getItemCode();
                    dataRtiveBean.subItemCode = subItem.getSubitemCode();
                    dataRtiveBean.scheduleNo = schedule.getScheduleNo();
                    List<RoundResult> stuRoundResult = null;
                    if (groupInfo == null) {
                        stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(),
                                mqttBean.getItemCode(), mqttBean.getSubitemCode(),
                                schedule.getScheduleNo(),
                                examPlace.getExamplaceName());
                    }else {
                        stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(),
                                mqttBean.getItemCode(), mqttBean.getSubitemCode(),
                                schedule.getScheduleNo(),
                                examPlace.getExamplaceName(),groupInfo);

                    }
                    for (RoundResult result : stuRoundResult){
                        if (result.getIsMultioleResult() == 0){   //单值项目
                            if (subItem.getMarkScore() == 1){  //打分项目
                                dataRtiveBean.score = result.getScore();
                            }else {  //测量项目
                                dataRtiveBean.result = result.getResult();
                            }
                        }else {  //多值项目
                            StringBuffer stringBuffer = new StringBuffer();
                            List<MultipleResult> results = DBManager.getInstance().getMultioleResult(result.getId());
                            for (MultipleResult multipleResult : results){
                                stringBuffer.append(multipleResult.getDesc() + ":" + multipleResult.getScore()+" ");
                            }
                            if (subItem.getMarkScore() == 1){  //打分项目
                                dataRtiveBean.score = stringBuffer.toString();
                            }else {  //测量项目
                                dataRtiveBean.result = stringBuffer.toString();
                            }
                        }
                    }
                    if (!dataRtiveBeans.contains(dataRtiveBean))
                        dataRtiveBeans.add(dataRtiveBean);
                }
                return new DataBaseRespon(true,"",dataRtiveBeans);
            }

            @Override
            public void onExecuteSuccess(DataBaseRespon respon) {
                List<DataRtiveBean> mqttBeans = (List<DataRtiveBean>) respon.getObject();
//                Log.e("TAG====>",mqttBeans.toString());
                postValue(mqttBeans);
            }

            @Override
            public void onExecuteFail(DataBaseRespon respon) {

            }
        });
    }


}
