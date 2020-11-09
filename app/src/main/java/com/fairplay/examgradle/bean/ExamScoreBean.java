package com.fairplay.examgradle.bean;

import com.fairplay.database.entity.Item;

import java.util.List;

public class ExamScoreBean {
    public List<Score> resultList;
    public int currentPosition;        //当前成绩坐标
    public Item item;
    public int roundNo;
    public boolean isSelected = false;   //当前成绩是否是选中状态
    public String studentCode;

    public static class Score{
        public boolean isLock;
        public String desc;
        public StringBuffer result;
        public String unit;
        public String order;
    }
}
