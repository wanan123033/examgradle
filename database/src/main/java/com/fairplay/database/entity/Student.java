package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Student {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private String faceFeature;
    private String studentCode;

    @Id(autoincrement = true)
    private Long id;//学生ID
    private int sex;//性别 0-男  1-女
    private String idCardNo;//身份证号
    private String icCardNo;//IC卡号
    private String className;//班级
    private String schoolName;//学校名称
    private String downloadTime;//下载时间  时间戳
    private String portrait;//头像
    private String sortName;//组别
    private int groupNo;//组号（分组）
    private String remark1;
    private String remark2;
    private String remark3;
    private String studentName;

    @Generated(hash = 1394321061)
    public Student(String faceFeature, String studentCode, Long id, int sex,
            String idCardNo, String icCardNo, String className, String schoolName,
            String downloadTime, String portrait, String sortName, int groupNo,
            String remark1, String remark2, String remark3, String studentName) {
        this.faceFeature = faceFeature;
        this.studentCode = studentCode;
        this.id = id;
        this.sex = sex;
        this.idCardNo = idCardNo;
        this.icCardNo = icCardNo;
        this.className = className;
        this.schoolName = schoolName;
        this.downloadTime = downloadTime;
        this.portrait = portrait;
        this.sortName = sortName;
        this.groupNo = groupNo;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.studentName = studentName;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public String getFaceFeature() {
        return this.faceFeature;
    }
    public void setFaceFeature(String faceFeature) {
        this.faceFeature = faceFeature;
    }
    public String getStudentCode() {
        return this.studentCode;
    }
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getIdCardNo() {
        return this.idCardNo;
    }
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
    public String getIcCardNo() {
        return this.icCardNo;
    }
    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }
    public String getClassName() {
        return this.className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getSchoolName() {
        return this.schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getDownloadTime() {
        return this.downloadTime;
    }
    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
    public String getPortrait() {
        return this.portrait;
    }
    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
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
