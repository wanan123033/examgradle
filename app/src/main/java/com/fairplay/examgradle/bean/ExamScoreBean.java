package com.fairplay.examgradle.bean;

import com.fairplay.database.entity.Item;

import java.util.List;

public class ExamScoreBean {
    public boolean isLock;
    public List<Score> resultList;
    public int currentPosition;        //内层Recy item坐标值
    public Item item;
    public int currentScorePosition;   //当前轮次号

    public static class Score{
        public boolean isLock;
        public String desc;
        public StringBuffer result;
        public String unit;
        public String order;
    }
}
