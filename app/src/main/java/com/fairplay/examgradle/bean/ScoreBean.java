package com.fairplay.examgradle.bean;

public class ScoreBean {
    public StringBuffer timeResult = new StringBuffer();
    public StringBuffer wight = new StringBuffer();
    public StringBuffer result1 = new StringBuffer();
    public StringBuffer result2 = new StringBuffer();
    public boolean twoPos;     //两个成绩时第二个成绩选中状态

    public boolean isLook = false;   //锁的状态

    public static final String dataFormat = "mm:ss.SSS";
}
