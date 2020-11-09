package com.fairplay.examgradle.viewmodel;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.RoundResult;
import com.feipulai.common.db.DataBaseExecutor;
import com.feipulai.common.db.DataBaseRespon;
import com.feipulai.common.db.DataBaseTask;
import com.gwm.mvvm.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataSelectViewModel extends BaseViewModel<Object> {
    private int mPageNum = 0;
    private int LOAD_DATAS = 100;
    private void selectAll(String itemCode,String subItemCode) {

        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<MqttBean> mqttBeans = DBManager.getInstance().getMQTTBean(itemCode,subItemCode,LOAD_DATAS,LOAD_DATAS*mPageNum);
                List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
                Item item = DBManager.getInstance().getItemByItemCode(itemCode,itemCode);
                Item subItem = DBManager.getInstance().getItemByItemCode(itemCode,subItemCode);
                for (MqttBean mqttBean : mqttBeans){
                    DataRtiveBean dataRtiveBean = new DataRtiveBean();
                    dataRtiveBean.studentCode = mqttBean.getStudentCode();
                    dataRtiveBean.itemName = item.getItemName()+"-"+subItem.getItemName();
                    dataRtiveBean.examPlaceName = mqttBean.getExamPlaceName();
                    List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(mqttBean.getStudentCode(), mqttBean.getItemCode(), mqttBean.getSubitemCode());
                    for (RoundResult result : stuRoundResult){
                        if (result.getIsMultioleResult() == 0){

                        }else {

                        }
                    }
                }
                return new DataBaseRespon(true,"",dataRtiveBeans);
            }
            @Override
            public void onExecuteSuccess(DataBaseRespon respon) {
                List<DataRtiveBean> mqttBeans = (List<DataRtiveBean>) respon.getObject();
            }

            @Override
            public void onExecuteFail(DataBaseRespon respon) {

            }
        });

    }
}
