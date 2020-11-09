package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class StudentGroupItem {
    @Id(autoincrement = true)
    private Long id;
    private String itemCode;//项目素质代码
    private String subitemCode;//项目专项代码
    @NotNull
    private int groupType;//分组性别（0.男子 1.女子 2.混合）
    @NotNull
    private String sortName;//组别
    @NotNull
    private String groupNo;//组号（分组）
    @NotNull
    private String scheduleNo;//考点日程编号	日程编号就是场次
    @NotNull
    private int identityMark;//身份验证标示 0 无验证 1 有验证
    public String examPlaceName;
    public String rollCallPlaceName;

    private String remark1;
    private String remark2;
    private String remark3;

    @Generated(hash = 1134002601)
    public StudentGroupItem(Long id, String itemCode, String subitemCode,
            int groupType, @NotNull String sortName, @NotNull String groupNo,
            @NotNull String scheduleNo, int identityMark, String examPlaceName,
            String rollCallPlaceName, String remark1, String remark2,
            String remark3) {
        this.id = id;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.groupType = groupType;
        this.sortName = sortName;
        this.groupNo = groupNo;
        this.scheduleNo = scheduleNo;
        this.identityMark = identityMark;
        this.examPlaceName = examPlaceName;
        this.rollCallPlaceName = rollCallPlaceName;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
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
    public String getGroupNo() {
        return this.groupNo;
    }
    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
    public String getScheduleNo() {
        return this.scheduleNo;
    }
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }
    public int getIdentityMark() {
        return this.identityMark;
    }
    public void setIdentityMark(int identityMark) {
        this.identityMark = identityMark;
    }
    public String getExamPlaceName() {
        return this.examPlaceName;
    }
    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }
    public String getRollCallPlaceName() {
        return this.rollCallPlaceName;
    }
    public void setRollCallPlaceName(String rollCallPlaceName) {
        this.rollCallPlaceName = rollCallPlaceName;
    }
    public String getRemark1() {
        return this.remark1;
    }
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    public String getRemark2() {
        return this.remark2;
    }
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    public String getRemark3() {
        return this.remark3;
    }
    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }
}