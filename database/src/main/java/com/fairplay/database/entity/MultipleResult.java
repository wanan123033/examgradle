package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MultipleResult {
    @Id(autoincrement = true)
    private Long id;

    private Long roundId;  //成绩表ID

    private String order;
    private String group;
    private String desc;
    private String unit;
    private String scoreMultiple;
    private String score;
    private String machineScore;

    @Generated(hash = 79134762)
    public MultipleResult(Long id, Long roundId, String order, String group,
            String desc, String unit, String scoreMultiple, String score,
            String machineScore) {
        this.id = id;
        this.roundId = roundId;
        this.order = order;
        this.group = group;
        this.desc = desc;
        this.unit = unit;
        this.scoreMultiple = scoreMultiple;
        this.score = score;
        this.machineScore = machineScore;
    }

    @Generated(hash = 1022146704)
    public MultipleResult() {
    }
    
    @Override
    public String toString() {
        return "MultipleResult{" +
                "id=" + id +
                ", roundId=" + roundId +
                ", order='" + order + '\'' +
                ", group='" + group + '\'' +
                ", desc='" + desc + '\'' +
                ", unit='" + unit + '\'' +
                ", scoreMultiple='" + scoreMultiple + '\'' +
                ", score='" + score + '\'' +
                ", machineScore='" + machineScore + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoundId() {
        return this.roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
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
