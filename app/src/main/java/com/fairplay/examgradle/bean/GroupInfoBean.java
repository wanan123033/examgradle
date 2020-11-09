package com.fairplay.examgradle.bean;

import java.util.List;

public class GroupInfoBean extends BaseBean<GroupInfoBean.GroupInfo>{

    public static class GroupInfo{
        public int batch;
        public int batchTotal;
        public int examStatus;
        public List<Group> dataInfo;
    }
    public static class Group{
        public String sortName;  // 组别
        public String groupNo;  // 分组
        public String examItemCode;  // 项目代码
        public int exmItemType; // 考试项目类型(0:素质，1:专项)
        public int subitemFlag; // 子项标志(0:否，1:是)
        public String subitemCode;  // 项目代码
        public int groupType; // 分组类别 （0.男子 1.女子 2.混合）
        public String scheduleNo;
        public int examType;
        public String examPlaceName;   //场地名称
        public String rollCallPlaceName;
    }
}
