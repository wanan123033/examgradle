package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MultipleItem {
    @Id(autoincrement = true)
    private Long id;

    private Long itemId;  //项目表ID

    private String order;
    private String group;
    private String desc;
    private String unit;
    private String scoreMultiple;
    private String result;
    private String machineResult;
    private String score;
    private String machineScore;
    @Generated(hash = 507024650)
    public MultipleItem(Long id, Long itemId, String order, String group,
            String desc, String unit, String scoreMultiple, String result,
            String machineResult, String score, String machineScore) {
        this.id = id;
        this.itemId = itemId;
        this.order = order;
        this.group = group;
        this.desc = desc;
        this.unit = unit;
        this.scoreMultiple = scoreMultiple;
        this.result = result;
        this.machineResult = machineResult;
        this.score = score;
        this.machineScore = machineScore;
    }
    @Generated(hash = 1149964109)
    public MultipleItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getItemId() {
        return this.itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getOrder() {
        return this.order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public String getGroup() {
        return this.group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getScoreMultiple() {
        return this.scoreMultiple;
    }
    public void setScoreMultiple(String scoreMultiple) {
        this.scoreMultiple = scoreMultiple;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getMachineResult() {
        return this.machineResult;
    }
    public void setMachineResult(String machineResult) {
        this.machineResult = machineResult;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getMachineScore() {
        return this.machineScore;
    }
    public void setMachineScore(String machineScore) {
        this.machineScore = machineScore;
    }
}
