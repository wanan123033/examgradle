package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MqttBean {
    @Id(autoincrement = true)
    private Long id;
    private String itemCode;
    private String subitemCode;
    private String examPlaceName;
    private int examStatus;
    private String groupNo;
    private String groupType;
    private String sortName;
    private String trackNo;
    private String studentCode;
    @Generated(hash = 1476868834)
    public MqttBean(Long id, String itemCode, String subitemCode,
            String examPlaceName, int examStatus, String groupNo, String groupType,
            String sortName, String trackNo, String studentCode) {
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
    }
    @Generated(hash = 1043871520)
    public MqttBean() {
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
    public String getGroupNo() {
        return this.groupNo;
    }
    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
    public String getGroupType() {
        return this.groupType;
    }
    public void setGroupType(String groupType) {
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

}
