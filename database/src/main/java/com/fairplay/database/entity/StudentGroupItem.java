package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {
        @Index(value = "itemCode ASC,subitemCode ASC,examPlaceName ASC,studentCode ASC", unique = true)
})
public class StudentGroupItem {
    @Id(autoincrement = true)
    private Long id;
    private String itemCode;
    private String subitemCode;
    private String examPlaceName;
    private int examStatus;
    private int groupNo;
    private int groupType;
    private String sortName;
    private String trackNo;
    private String studentCode;
    private String scheduleNo;
@Generated(hash = 1558150412)
public StudentGroupItem(Long id, String itemCode, String subitemCode, String examPlaceName,
        int examStatus, int groupNo, int groupType, String sortName, String trackNo,
        String studentCode, String scheduleNo) {
    this.id = id;
    this.itemCode = itemCode;
    this.subitemCode = subitemCode;
    this.examPlaceName = examPlaceName;
    this.examStatus = examStatus;
    this.groupNo = groupNo;
    this.groupType = groupType;
    this.sortName = sortName;
    this.trackNo = trackNo;
    this.studentCode = studentCode;
    this.scheduleNo = scheduleNo;
}
@Generated(hash = 1800385417)
public StudentGroupItem() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getItemCode() {
    return this.itemCode;
}
public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
}
public String getSubitemCode() {
    return this.subitemCode;
}
public void setSubitemCode(String subitemCode) {
    this.subitemCode = subitemCode;
}
public String getExamPlaceName() {
    return this.examPlaceName;
}
public void setExamPlaceName(String examPlaceName) {
    this.examPlaceName = examPlaceName;
}
public int getExamStatus() {
    return this.examStatus;
}
public void setExamStatus(int examStatus) {
    this.examStatus = examStatus;
}
public int getGroupNo() {
    return this.groupNo;
}
public void setGroupNo(int groupNo) {
    this.groupNo = groupNo;
}
public int getGroupType() {
    return this.groupType;
}
public void setGroupType(int groupType) {
    this.groupType = groupType;
}
public String getSortName() {
    return this.sortName;
}
public void setSortName(String sortName) {
    this.sortName = sortName;
}
public String getTrackNo() {
    return this.trackNo;
}
public void setTrackNo(String trackNo) {
    this.trackNo = trackNo;
}
public String getStudentCode() {
    return this.studentCode;
}
public void setStudentCode(String studentCode) {
    this.studentCode = studentCode;
}
public String getScheduleNo() {
    return this.scheduleNo;
}
public void setScheduleNo(String scheduleNo) {
    this.scheduleNo = scheduleNo;
}

    @Override
    public String toString() {
        return "StudentGroupItem{" +
                "id=" + id +
                ", itemCode='" + itemCode + '\'' +
                ", subitemCode='" + subitemCode + '\'' +
                ", examPlaceName='" + examPlaceName + '\'' +
                ", examStatus=" + examStatus +
                ", groupNo=" + groupNo +
                ", groupType=" + groupType +
                ", sortName='" + sortName + '\'' +
                ", trackNo='" + trackNo + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", scheduleNo='" + scheduleNo + '\'' +
                '}';
    }
}
