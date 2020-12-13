package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

import java.util.Objects;

@Entity(indexes = {
        @Index(value = "sortName ASC,itemCode ASC,subItemCode ASC," +
                "groupType ASC,scheduleNo ASC,groupNo ASC,examPlaceName ASC", unique = true)
})
public class GroupInfo {
    @Id(autoincrement = true)
    private Long id;
    private String sortName;
    private String itemCode;
    private String subItemCode;
    private int groupType;
    public String scheduleNo;
    public int groupNo;
    public String examPlaceName;
    @Generated(hash = 1721087153)
    public GroupInfo(Long id, String sortName, String itemCode, String subItemCode,
            int groupType, String scheduleNo, int groupNo, String examPlaceName) {
        this.id = id;
        this.sortName = sortName;
        this.itemCode = itemCode;
        this.subItemCode = subItemCode;
        this.groupType = groupType;
        this.scheduleNo = scheduleNo;
        this.groupNo = groupNo;
        this.examPlaceName = examPlaceName;
    }
    @Generated(hash = 1250265142)
    public GroupInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSortName() {
        return this.sortName;
    }
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
    public String getItemCode() {
        return this.itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getSubItemCode() {
        return this.subItemCode;
    }
    public void setSubItemCode(String subItemCode) {
        this.subItemCode = subItemCode;
    }
    public int getGroupType() {
        return this.groupType;
    }
    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }
    public String getScheduleNo() {
        return this.scheduleNo;
    }
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }
    public int getGroupNo() {
        return this.groupNo;
    }
    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }
    public String getExamPlaceName() {
        return this.examPlaceName;
    }
    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "id=" + id +
                ", sortName='" + sortName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", subItemCode='" + subItemCode + '\'' +
                ", groupType=" + groupType +
                ", scheduleNo='" + scheduleNo + '\'' +
                ", groupNo=" + groupNo +
                ", examPlaceName='" + examPlaceName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupInfo groupInfo = (GroupInfo) o;
        return groupType == groupInfo.groupType &&
                groupNo == groupInfo.groupNo &&
                sortName.equals(groupInfo.sortName) &&
                itemCode.equals(groupInfo.itemCode) &&
                subItemCode.equals(groupInfo.subItemCode) &&
                scheduleNo.equals(groupInfo.scheduleNo) &&
                examPlaceName.equals(groupInfo.examPlaceName);
    }
}
