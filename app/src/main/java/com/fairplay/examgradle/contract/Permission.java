package com.fairplay.examgradle.contract;

public interface Permission {
    String hasSyncScreen = "hasSyncScreen";
    String hasFaceID = "hasFaceID";
    String hasAddTempGroup = "hasAddTempGroup";
    String hasGetTempGroup = "hasGetTempGroup";
    String hasTestScore = "hasTestScore";     //测量打分权限
    String hasHeadJudge = "hasHeadJudge";
    String hasManulInputScore = "hasManulInputScore";  //手工录入权限
    String hasConfirmGroup = "hasConfirmGroup";
}
