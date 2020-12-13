package com.fairplay.database.entity;

import java.util.Objects;

public class DataRtiveBean {
    public String studentCode;
    public String itemName;
    public String examPlaceName;
    public String result;
    public String score;
    public String itemCode;
    public String subItemCode;
    public boolean isSelected = false;
    public String scheduleNo;
    public int examType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRtiveBean bean = (DataRtiveBean) o;
        return studentCode.equals(bean.studentCode) &&
                itemName.equals(bean.itemName) &&
                examPlaceName.equals(bean.examPlaceName) &&
                itemCode.equals(bean.itemCode) &&
                subItemCode.equals(bean.subItemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentCode, itemName, examPlaceName, itemCode, subItemCode);
    }

    @Override
    public String toString() {
        return "DataRtiveBean{" +
                "studentCode='" + studentCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", examPlaceName='" + examPlaceName + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
