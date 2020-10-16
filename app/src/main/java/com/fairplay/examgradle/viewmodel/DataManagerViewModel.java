package com.fairplay.examgradle.viewmodel;

import com.fairplay.examgradle.httppresenter.DownItemInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownScheduleInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownStudentInfoPresenter;
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

        //3.下载学生
        DownStudentInfoPresenter downStudentInfoPresenter = new DownStudentInfoPresenter();
        downStudentInfoPresenter.setViewModel(this);
//        downStudentInfoPresenter.downSiteItemStudent();

        //TODO 4.下载临时分组  下载分组信息   由项目确定
    }
}
