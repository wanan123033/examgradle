package com.fairplay.examgradle.viewmodel;

import android.util.Log;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
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
    private int LOAD_DATAS = 100;
    public void selectAll(String itemCode,String subItemCode,int pageNum) {

        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<MqttBean> mqttBeans = DBManager.getInstance().getMQTTBean(itemCode,subItemCode, LOAD_DATAS, pageNum);
                List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
                Item item = DBManager.getInstance().getItemByItemCode(itemCode,itemCode);
                Item subItem = DBManager.getInstance().getItemByItemCode(itemCode,subItemCode);
                for (MqttBean mqttBean : mqttBeans){
                    DataRtiveBean dataRtiveBean = new DataRtiveBean();
                    dataRtiveBean.studentCode = mqttBean.getStudentCode();
                    dataRtiveBean.itemName = item.getItemName()+"-"+subItem.getItemName();
                    dataRtiveBean.examPlaceName = mqttBean.getExamPlaceName();
                    dataRtiveBean.itemCode = itemCode;
                    dataRtiveBean.subItemCode = subItemCode;
                    List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(), mqttBean.getItemCode(), mqttBean.getSubitemCode());

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

    public void searchStuCode(String stuCode) {
        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<MqttBean> mqttBeans = DBManager.getInstance().getMQTTBean(stuCode);
                List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
                for (MqttBean mqttBean : mqttBeans){
                    Item item = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(),mqttBean.getItemCode());
                    Item subItem = DBManager.getInstance().getItemByItemCode(mqttBean.getItemCode(),mqttBean.getSubitemCode());
                    DataRtiveBean dataRtiveBean = new DataRtiveBean();
                    dataRtiveBean.studentCode = mqttBean.getStudentCode();
                    dataRtiveBean.itemName = item.getItemName()+"-"+subItem.getItemName();
                    dataRtiveBean.examPlaceName = mqttBean.getExamPlaceName();
                    dataRtiveBean.itemCode = item.getItemCode();
                    dataRtiveBean.subItemCode = subItem.getSubitemCode();
                    List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(), mqttBean.getItemCode(), mqttBean.getSubitemCode());
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
