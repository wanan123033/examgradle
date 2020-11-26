package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

@Entity(indexes = {
        @Index(value = "itemCode ASC,subItemCode ASC,examplaceName ASC", unique = true)
})
public class ExamPlace {
    @Id(autoincrement = true)
    private Long id;
    private String examplaceName;

    private String itemCode;
    private String subItemCode;
    @Generated(hash = 1030521045)
    public ExamPlace(Long id, String examplaceName, String itemCode,
            String subItemCode) {
        this.id = id;
        this.examplaceName = examplaceName;
        this.itemCode = itemCode;
        this.subItemCode = subItemCode;
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

}
