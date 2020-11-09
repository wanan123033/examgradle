package com.fairplay.database.entity;

public class DataRtiveBean {
    public String studentCode;
    public String itemName;
    public String examPlaceName;
    public String result;
    public String score;

    @Override
    public String toString() {
        return "DataRtiveBean{" +
                "studentCode='" + studentCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", examPlaceName='" + examPlaceName + '\'' +
                ", result='" + result + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
