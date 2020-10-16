package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ScheduleInfoBean;

public class DownScheduleInfoPresenter extends JsonDataPresenter<DownScheduleInfoPresenter.DownScheduleInfo, ScheduleInfoBean.ScheduleInfo> {
    public DownScheduleInfoPresenter() {
        super(DownScheduleInfo.class);
    }

    /**
     * 下载日程信息
     */
    public void downSchedule(){

    }

    @Override
    protected void onNextResult(ScheduleInfoBean.ScheduleInfo response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    public interface DownScheduleInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
