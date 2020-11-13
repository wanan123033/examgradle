package com.fairplay.examgradle.bean;

import com.fairplay.database.entity.Item;

import java.util.List;
import java.util.Objects;

public class ExamScoreBean {
    public List<Score> resultList;
    public int currentPosition;        //当前成绩坐标
    public Item item;
    public int roundNo;
    public boolean isSelected = false;   //当前成绩是否是选中状态
    public String studentCode;

    @Override
    public String toString() {
        return "ExamScoreBean{" +
                "resultList=" + resultList +
                ", currentPosition=" + currentPosition +
                ", item=" + item +
                ", roundNo=" + roundNo +
                ", isSelected=" + isSelected +
                ", studentCode='" + studentCode + '\'' +
                '}';
    }

    public static class Score{
        public boolean isLock = false;
        public String desc;
        public StringBuffer result;
        public String unit;
        public String order;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Score score = (Score) o;
            return desc.equals(score.desc) &&
                    order.equals(score.order);
        }

        @Override
        public int hashCode() {
            return Objects.hash(desc, order);
        }

        @Override
        public String toString() {
            return "Score{" +
                    "isLock=" + isLock +
                    ", desc='" + desc + '\'' +
                    ", result=" + result +
                    ", unit='" + unit + '\'' +
                    ", order='" + order + '\'' +
                    '}';
        }
    }
}
