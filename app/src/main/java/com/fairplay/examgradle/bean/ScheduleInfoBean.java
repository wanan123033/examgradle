package com.fairplay.examgradle.bean;

import java.util.List;

public class ScheduleInfoBean extends BaseBean<ScheduleInfoBean.ScheduleInfo>{

    public static class ScheduleInfo{
        public String siteName;  //考点名称
        public String examType;
        public String singleMachineTest;
        public List<SiteScheduleInfo> siteScheduleInfoVOList;
    }
    public static class SiteScheduleInfo{
        public String scheduleNo;
        public long beginTime;
        public long endTime;
        public List<ExamItem> examItemVOList;
    }
    public static class ExamItem{
        public int ruleBySubitem;
        public String examItemCode;
    }
}
