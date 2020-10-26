package com.fairplay.examgradle.bean;

import com.fairplay.database.entity.Item;

import java.util.List;

public class ExamScoreBean {
    public boolean isLock;
    public List<Score> resultList;
    public int currentScorePosition;   //外层Recy item坐标值
    public int currentPosition;        //内层Recy item坐标值
    public Item item;

    public static class Score{
        public boolean isLock;
        public String desc;
        public StringBuffer result;
        public String unit;
    }
}
