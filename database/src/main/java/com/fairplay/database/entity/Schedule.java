package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Schedule {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    @NotNull
    private String scheduleNo; //日程序号 （场次）
    @NotNull
    private String beginTime;
    private String endTime;
    private String itemCode;  //项目素质代码
    private String subitemCode; //项目专项代码
    @Generated(hash = 721857544)
    public Schedule(Long id, @NotNull String scheduleNo, @NotNull String beginTime,
            String endTime, String itemCode, String subitemCode) {
        this.id = id;
        this.scheduleNo = scheduleNo;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
    }
    @Generated(hash = 729319394)
    public Schedule() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getScheduleNo() {
        return this.scheduleNo;
    }
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }
    public String getBeginTime() {
        return this.beginTime;
    }
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
}
