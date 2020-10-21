package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Item {
    @Id(autoincrement = true)
    private Long id;//
    @Unique
    private String itemName;  //项目名称
    private String itemCode;  //项目素质代码
    private String subitemCode;  //项目专项代码
    private String machineCode;  //机器码
    private String unit;     //单位   厘米  毫升  次
    private int testType;  //测量方式  计时 远度 计数 力量
    private int testNum;   //测试次数
    private int digital;   //小数位数
    private int carryMode; //进位方式 1.四舍五入 2.舍位 3.非零进取 0 不舍位
    private int minValue;  //有效成绩最小值
    private int maxValue;  //有效成绩最大值
    private int lastResultMode; //最终成绩的选择模式 1.最后成绩，2.补考成绩，3.最好
    private int scoreCountMode; //算分模式 0:精确算分1:固定算分
    private int scoreCountRule; //得分规则 0:往低分靠1:往高分靠
    private int scoreCarryMode; //得分进位方式
    private int scoreCarryByHundredth;   //得分根据后几位进位
    private int itemProperty;     //项目属性   1: 必考, 2: 选考
    private int exmItemType;      //考试项目类型 0:素质，1:专项
    private int limitGender;      //项目限制性别
    private int minScore;         //项目最低分
    private int ratio;            //分值比例系数
    private int isLowestPoint;     //是否给最低分  0:否，1:是
    private int markScore;  // 打分类型  (0: 测量项目，1: 打分项目)
    private int scoreCount;  //打分个数
    private int calScoreType;  //算分方式  (0: 取最好, 1.去掉最高最低取平均分, 2: 取平均分)
    private String calScoreExpression;  //算分表达式
    private int enableTempGroup;   //启用临时分组  (0: 不启用, 1: 启用)
    private int enableTempGroupRight;  // 启用临时分组后设置测量用户给临时分组的打分权限(0: 不启用, 1: 启用)
    private String remark1;
    private String remark2;
    private String remark3;
    @Generated(hash = 486754733)
    public Item(Long id, String itemName, String itemCode, String subitemCode,
            String machineCode, String unit, int testType, int testNum, int digital,
            int carryMode, int minValue, int maxValue, int lastResultMode,
            int scoreCountMode, int scoreCountRule, int scoreCarryMode,
            int scoreCarryByHundredth, int itemProperty, int exmItemType,
            int limitGender, int minScore, int ratio, int isLowestPoint,
            int markScore, int scoreCount, int calScoreType,
            String calScoreExpression, int enableTempGroup,
            int enableTempGroupRight, String remark1, String remark2,
            String remark3) {
        this.id = id;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.machineCode = machineCode;
        this.unit = unit;
        this.testType = testType;
        this.testNum = testNum;
        this.digital = digital;
        this.carryMode = carryMode;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.lastResultMode = lastResultMode;
        this.scoreCountMode = scoreCountMode;
        this.scoreCountRule = scoreCountRule;
        this.scoreCarryMode = scoreCarryMode;
        this.scoreCarryByHundredth = scoreCarryByHundredth;
        this.itemProperty = itemProperty;
        this.exmItemType = exmItemType;
        this.limitGender = limitGender;
        this.minScore = minScore;
        this.ratio = ratio;
        this.isLowestPoint = isLowestPoint;
        this.markScore = markScore;
        this.scoreCount = scoreCount;
        this.calScoreType = calScoreType;
        this.calScoreExpression = calScoreExpression;
        this.enableTempGroup = enableTempGroup;
        this.enableTempGroupRight = enableTempGroupRight;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
    }
    @Generated(hash = 1470900980)
    public Item() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getItemName() {
        return this.itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
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
    public String getMachineCode() {
        return this.machineCode;
    }
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public int getTestType() {
        return this.testType;
    }
    public void setTestType(int testType) {
        this.testType = testType;
    }
    public int getTestNum() {
        return this.testNum;
    }
    public void setTestNum(int testNum) {
        this.testNum = testNum;
    }
    public int getDigital() {
        return this.digital;
    }
    public void setDigital(int digital) {
        this.digital = digital;
    }
    public int getCarryMode() {
        return this.carryMode;
    }
    public void setCarryMode(int carryMode) {
        this.carryMode = carryMode;
    }
    public int getMinValue() {
        return this.minValue;
    }
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
    public int getMaxValue() {
        return this.maxValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public int getLastResultMode() {
        return this.lastResultMode;
    }
    public void setLastResultMode(int lastResultMode) {
        this.lastResultMode = lastResultMode;
    }
    public int getScoreCountMode() {
        return this.scoreCountMode;
    }
    public void setScoreCountMode(int scoreCountMode) {
        this.scoreCountMode = scoreCountMode;
    }
    public int getScoreCountRule() {
        return this.scoreCountRule;
    }
    public void setScoreCountRule(int scoreCountRule) {
        this.scoreCountRule = scoreCountRule;
    }
    public int getScoreCarryMode() {
        return this.scoreCarryMode;
    }
    public void setScoreCarryMode(int scoreCarryMode) {
        this.scoreCarryMode = scoreCarryMode;
    }
    public int getScoreCarryByHundredth() {
        return this.scoreCarryByHundredth;
    }
    public void setScoreCarryByHundredth(int scoreCarryByHundredth) {
        this.scoreCarryByHundredth = scoreCarryByHundredth;
    }
    public int getItemProperty() {
        return this.itemProperty;
    }
    public void setItemProperty(int itemProperty) {
        this.itemProperty = itemProperty;
    }
    public int getExmItemType() {
        return this.exmItemType;
    }
    public void setExmItemType(int exmItemType) {
        this.exmItemType = exmItemType;
    }
    public int getLimitGender() {
        return this.limitGender;
    }
    public void setLimitGender(int limitGender) {
        this.limitGender = limitGender;
    }
    public int getMinScore() {
        return this.minScore;
    }
    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }
    public int getRatio() {
        return this.ratio;
    }
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
    public int getIsLowestPoint() {
        return this.isLowestPoint;
    }
    public void setIsLowestPoint(int isLowestPoint) {
        this.isLowestPoint = isLowestPoint;
    }
    public int getMarkScore() {
        return this.markScore;
    }
    public void setMarkScore(int markScore) {
        this.markScore = markScore;
    }
    public int getScoreCount() {
        return this.scoreCount;
    }
    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }
    public int getCalScoreType() {
        return this.calScoreType;
    }
    public void setCalScoreType(int calScoreType) {
        this.calScoreType = calScoreType;
    }
    public String getCalScoreExpression() {
        return this.calScoreExpression;
    }
    public void setCalScoreExpression(String calScoreExpression) {
        this.calScoreExpression = calScoreExpression;
    }
    public int getEnableTempGroup() {
        return this.enableTempGroup;
    }
    public void setEnableTempGroup(int enableTempGroup) {
        this.enableTempGroup = enableTempGroup;
    }
    public int getEnableTempGroupRight() {
        return this.enableTempGroupRight;
    }
    public void setEnableTempGroupRight(int enableTempGroupRight) {
        this.enableTempGroupRight = enableTempGroupRight;
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
