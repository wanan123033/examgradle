package com.fairplay.examgradle.httppresenter;


import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Schedule;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ScheduleInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.geek.thread.GeekThreadPools;
import com.gwm.base.BaseApplication;
import com.gwm.retrofit.Observable;
import com.orhanobut.logger.Logger;

public class DownScheduleInfoPresenter extends JsonDataPresenter<DownScheduleInfoPresenter.DownScheduleInfo, ScheduleInfoBean> {
    public DownScheduleInfoPresenter() {
        super(DownScheduleInfo.class);
    }

    /**
     * 下载日程信息
     */
    public void downSchedule(){
        String json = genJsonString(100020112,"");
        String token = getToken();
        Observable<ScheduleInfoBean> itemInfoBeanObservable = getHttpPresenter().downSiteScheduleInfo(token, json);
        addHttpSubscriber("",itemInfoBeanObservable,ScheduleInfoBean.class);
    }

    @Override
    protected void onNextResult(ScheduleInfoBean response, int id) {
        GeekThreadPools.executeWithGeekThreadPool(new Runnable() {
            @Override
            public void run() {
                if (response.data != null) {
                    if (response.data.siteScheduleInfoVOList != null && !response.data.siteScheduleInfoVOList.isEmpty()) {
                        for (ScheduleInfoBean.SiteScheduleInfo info : response.data.siteScheduleInfoVOList) {
                            Schedule schedule = new Schedule();
                            schedule.setBeginTime(info.beginTime);
                            schedule.setEndTime(info.endTime);
                            schedule.setSiteName(response.data.siteName);
                            schedule.setScheduleNo(info.scheduleNo);
                            DBManager.getInstance().insertSchedule(schedule);

                        }
                    }
                }else {
                    ToastUtils.showShort(response.msg);
                }
            }
        });

    }


    public interface DownScheduleInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
