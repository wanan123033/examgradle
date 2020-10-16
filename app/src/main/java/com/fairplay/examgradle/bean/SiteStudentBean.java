package com.fairplay.examgradle.bean;

import java.util.List;

public class SiteStudentBean extends BaseBean<SiteStudentBean.SiteStudent>{
    public static class SiteStudent {
        public int batch;
        public int batchTotal;
        public int examType;
        public List<Student> dataInfo;
    }
    public static class Student{
        public String schoolName;
        public String schoolCode;
        public String sortName;
        public String className;
        public int studentType;  // 考生类型（0.正常，1.择考，2.免考）--高考没有
        public String studentCode;
        public String examNo;
        public String registeredNo;  //报名号
        public String idCard;
        public String studentName;
        public int gender; // 性别(0:男 1:⼥)
        public int examType;  // 考试类型 0.正常 1.缓考，2.补考
        public String icCard;
        public String gradeName;  //年级名称
        public String subject;   // 专业或科目
        public String deptName;  // 院系或部门
        public long downloadTime;
        public String photoData;
        public String faceFeature;
        public List<ExtBody> extBody;
        public List<ItemInfoBean.SubitemTag> itemTag;
        public List<SubItem> subitemList;
    }
    public static class ExtBody{
        public String extName;
        public int belongType;
        public int extType;
        public List<String> extValue;
    }
    public static class SubItem{
        public String subitemCode;
        public int examStatus;   // 考生该项目考试状态（0.正常，1缓考，2.补考）
        public int rollCallStatus;  // 检录状态(0: 未检录，1：已检录)
        public String machineCode;

    }
}
