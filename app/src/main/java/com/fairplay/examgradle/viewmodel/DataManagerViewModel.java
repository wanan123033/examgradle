package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.httppresenter.DownGroupInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownItemInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownScheduleInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownStudentInfoPresenter;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;

public class DataManagerViewModel extends BaseViewModel<Object> {
    public void rosterDownload() {
        //1.下载日程
        DownScheduleInfoPresenter scheduleInfoPresenter = new DownScheduleInfoPresenter();
        scheduleInfoPresenter.setViewModel(this);
        scheduleInfoPresenter.downSchedule();
        //2.下载项目
        DownItemInfoPresenter downItemInfoPresenter = new DownItemInfoPresenter();
        downItemInfoPresenter.setViewModel(this);
        downItemInfoPresenter.downItem();
//        //3.下载分组信息
//        DownGroupInfoPresenter downGroupInfoPresenter = new DownGroupInfoPresenter();
//        downGroupInfoPresenter.setViewModel(this);
//        int exam = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
//        downGroupInfoPresenter.downGroup(1,exam);
//        //下载报名信息
//        DownStudentInfoPresenter studentInfoPresenter = new DownStudentInfoPresenter();
//        studentInfoPresenter.setViewModel(this);
//        int examType = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
//        studentInfoPresenter.downSiteItemStudent(null,1,examType);
    }
}
