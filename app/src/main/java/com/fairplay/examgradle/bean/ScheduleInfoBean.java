package com.fairplay.examgradle.bean;

import java.util.List;

public class ScheduleInfoBean extends BaseBean<ScheduleInfoBean.ScheduleInfo>{

    public static class ScheduleInfo{
        public String siteName;  //考点名称
        public String examType;   // 考试模式 (0:个人模式 1:分组模式)
        public String singleMachineTest;  // 单机测试(0:否 1:是，默认为0)
        public List<SiteScheduleInfo> siteScheduleInfoVOList;

        @Override
        public String toString() {
            return "ScheduleInfo{" +
                    "siteName='" + siteName + '\'' +
                    ", examType='" + examType + '\'' +
                    ", singleMachineTest='" + singleMachineTest + '\'' +
                    ", siteScheduleInfoVOList=" + siteScheduleInfoVOList +
                    '}';
        }
    }
    public static class SiteScheduleInfo{
        public String scheduleNo;  //考点日程编号     日程编号就是场次
        public long beginTime;  //开始时间 long类型 时间戳
        public long endTime;  //结束时间 long类型 时间戳
        public List<ExamItem> examItemVOList;

        @Override
        public String toString() {
            return "SiteScheduleInfo{" +
                    "scheduleNo='" + scheduleNo + '\'' +
                    ", beginTime=" + beginTime +
                    ", endTime=" + endTime +
                    ", examItemVOList=" + examItemVOList +
                    '}';
        }
    }
    public static class ExamItem{
        public int ruleBySubitem;   // 是否按小项目编排（0.否，1.是）
        public String examItemCode;   // 考试项目代码

        @Override
        public String toString() {
            return "ExamItem{" +
                    "ruleBySubitem=" + ruleBySubitem +
                    ", examItemCode='" + examItemCode + '\'' +
                    '}';
        }
    }
}
