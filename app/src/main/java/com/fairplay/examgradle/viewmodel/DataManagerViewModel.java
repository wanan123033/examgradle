package com.fairplay.examgradle.viewmodel;

import android.content.Intent;

import com.blankj.utilcode.util.CacheMemoryUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.contract.CacheContract;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.httppresenter.DownItemInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownScheduleInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownStudentInfoPresenter;
import com.fairplay.examgradle.service.DataScoreUploadService;
import com.gwm.base.BaseApplication;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.mvvm.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataManagerViewModel extends BaseViewModel<Object> {
    public void rosterDownload() {
        MessageBus.getBus().post(new MessageBusMessage("下载信息中","SHOW_PROGRESS"));
        //1.下载日程
        DownScheduleInfoPresenter scheduleInfoPresenter = new DownScheduleInfoPresenter();
        scheduleInfoPresenter.setViewModel(this);
        scheduleInfoPresenter.downSchedule();
        //2.下载项目
        DownItemInfoPresenter downItemInfoPresenter = new DownItemInfoPresenter();
        downItemInfoPresenter.setViewModel(this);
        downItemInfoPresenter.downItem();
        //3.下载分组信息
//        DownGroupInfoPresenter downGroupInfoPresenter = new DownGroupInfoPresenter();
//        downGroupInfoPresenter.setViewModel(this);
//        int exam = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
//        downGroupInfoPresenter.downGroup(1,exam);
        //下载报名信息
        DownStudentInfoPresenter studentInfoPresenter = new DownStudentInfoPresenter();
        studentInfoPresenter.setViewModel(this);
        int examType = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
        studentInfoPresenter.downStudent(1,examType);
    }

    public void uploadScore() {
        List<RoundResult> roundResults = DBManager.getInstance().getRoundResultUploadState();
        ArrayList<StudentGroupItem> mqttBeans = new ArrayList<>();
        for (RoundResult result : roundResults){
            StudentGroupItem mqttBean = DBManager.getInstance().getMQTTBean(result.getItemCode(), result.getSubitemCode(), result.getStudentCode());
            mqttBeans.add(mqttBean);
        }
        Intent intent = new Intent(BaseApplication.getInstance(), DataScoreUploadService.class);
        //预防数据量过大,使用CacheMemoryUtils
        CacheMemoryUtils.getInstance().put(CacheContract.MQTT_BEAN,mqttBeans);
        BaseApplication.getInstance().startService(intent);
    }
}
