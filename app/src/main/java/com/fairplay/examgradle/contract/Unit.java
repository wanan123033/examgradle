package com.fairplay.examgradle.contract;

public enum Unit {
    cm("cm","厘米",""),
    kg("kg","千克",""),
    ml("ml","毫升",""),
    s("s","秒","ss.SS"),
    time("once","次",""),
    minutes("minutes","分'秒","mm:ss.SS"),
    m("m","米",""),
    score("score","得分",""),
    DEFAULT("","","");
    private String description;
    private String unit;
    private String desc;

    Unit(String unit, String desc,String description) {
        this.unit = unit;
        this.desc = desc;
        this.description = description;
    }

    public String getDesc() {
        return desc;
    }
    public static Unit getUnit(String unit){
        if (cm.unit.equals(unit)){
            return cm;
        }else if (kg.unit.equals(unit)){
            return kg;
        }else if (ml.unit.equals(unit)){
            return ml;
        }else if (s.unit.equals(unit)){
            return s;
        }else if (time.unit.equals(unit)){
            return time;
        }else if (minutes.unit.equals(unit)){
            return minutes;
        }else if (m.unit.equals(unit)){
            return m;
        }else if (score.unit.equals(unit)){
            return score;
        }
        return DEFAULT;
    }

    public String getDescription() {
        return description;
    }
}
