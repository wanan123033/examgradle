package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class StudentItem {
    @Id(autoincrement = true)
    private Long id;//学生项目ID
    @NotNull
    private String studentCode;//考号
    @NotNull
    private String itemCode;//项目素质代码
    private String subitemCode; //项目专项代码
    @NotNull
    private int machineCode;
    private int studentType;//考生类型（0.正常，1.缓考，2.补考） 暂不使用
    @NotNull
    private int examType;//考试类型 0.正常，1.缓考，2.补考
    private String scheduleNo;  //日程编号

    private String sortName;//组别
    private int groupNo;//组号（分组）
    private String remark1;
    private String remark2;
    private String remark3;
    @Generated(hash = 613654219)
    public StudentItem(Long id, @NotNull String studentCode,
            @NotNull String itemCode, String subitemCode, int machineCode,
            int studentType, int examType, String scheduleNo, String sortName,
            int groupNo, String remark1, String remark2, String remark3) {
        this.id = id;
        this.studentCode = studentCode;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.machineCode = machineCode;
        this.studentType = studentType;
        this.examType = examType;
        this.scheduleNo = scheduleNo;
        this.sortName = sortName;
        this.groupNo = groupNo;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
    }
    @Generated(hash = 383807586)
    public StudentItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStudentCode() {
        return this.studentCode;
    }
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
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
    public int getMachineCode() {
        return this.machineCode;
    }
    public void setMachineCode(int machineCode) {
        this.machineCode = machineCode;
    }
    public int getStudentType() {
        return this.studentType;
    }
    public void setStudentType(int studentType) {
        this.studentType = studentType;
    }
    public int getExamType() {
        return this.examType;
    }
    public void setExamType(int examType) {
        this.examType = examType;
    }
    public String getScheduleNo() {
        return this.scheduleNo;
    }
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
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
    public String getSortName() {
        return this.sortName;
    }
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
    public int getGroupNo() {
        return this.groupNo;
    }
    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }
}
