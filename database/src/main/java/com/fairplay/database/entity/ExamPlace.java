package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class ExamPlace {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String examplaceName;
    @Generated(hash = 1025135628)
    public ExamPlace(Long id, String examplaceName) {
        this.id = id;
        this.examplaceName = examplaceName;
    }
    @Generated(hash = 1279181580)
    public ExamPlace() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExamplaceName() {
        return this.examplaceName;
    }
    public void setExamplaceName(String examplaceName) {
        this.examplaceName = examplaceName;
    }
}
