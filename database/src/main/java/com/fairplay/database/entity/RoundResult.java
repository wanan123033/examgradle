package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RoundResult {
    @Id(autoincrement = true)
    private Long id;//项目轮次成绩ID
    @NotNull
    private String studentCode;
    @NotNull
    private String itemCode;//默认为default
    private String subitemCode;//默认为default
    @NotNull
    private int machineCode;
    @NotNull
    private int roundNo;//轮次
    @NotNull
    private int testNo;//测试次数
    private int machineResult;// 获取到的机器成绩
    private int penaltyNum;// 判罚值   判罚值有正负
    @NotNull
    private String result;//成绩      单位为"毫米(mm)"、"毫秒(ms)"、"克(g)"、"次","毫升"

    private String result2; //成绩2
    private int wight; //体重

    private int score;//分数      没有单位  根据成绩和得分比例系数算出分数
    @NotNull
    private int resultState;//成绩状态 //是否犯规 0:未检录 1:正常 2:犯规 3:中退 4:弃权 5:测试     体侧系统没有中退和放弃,且犯规均为机器判定的犯规
    @NotNull
    private int isLastResult;//是否为最好成绩 0-不是 1-是     身高体重最后成绩即为最好成绩
    @NotNull
    private int examType;//考试类型 0.正常 1.补考，(2.缓考,现没有这功能)
    @NotNull
    private String testTime;//测试时间  时间戳
    private String printTime="";//w打印时间 时间戳
    private String endTime;//结束时间 时间戳
    private int stumbleCount;// 绊绳次数
    @NotNull
    private int updateState;//上传状态 0未上传 1上传
    private byte[] cycleResult;//中长跑每一圈成绩
    private String scheduleNo;  //日程编号
    private String mtEquipment;//监控设备
    private String remark1;
    private String remark2;
    private String remark3;
    @Generated(hash = 1443236540)
    public RoundResult(Long id, @NotNull String studentCode, @NotNull String itemCode,
            String subitemCode, int machineCode, int roundNo, int testNo, int machineResult,
            int penaltyNum, @NotNull String result, String result2, int wight, int score,
            int resultState, int isLastResult, int examType, @NotNull String testTime, String printTime,
            String endTime, int stumbleCount, int updateState, byte[] cycleResult, String scheduleNo,
            String mtEquipment, String remark1, String remark2, String remark3) {
        this.id = id;
        this.studentCode = studentCode;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.machineCode = machineCode;
        this.roundNo = roundNo;
        this.testNo = testNo;
        this.machineResult = machineResult;
        this.penaltyNum = penaltyNum;
        this.result = result;
        this.result2 = result2;
        this.wight = wight;
        this.score = score;
        this.resultState = resultState;
        this.isLastResult = isLastResult;
        this.examType = examType;
        this.testTime = testTime;
        this.printTime = printTime;
        this.endTime = endTime;
        this.stumbleCount = stumbleCount;
        this.updateState = updateState;
        this.cycleResult = cycleResult;
        this.scheduleNo = scheduleNo;
        this.mtEquipment = mtEquipment;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
    }
    @Generated(hash = 1393632943)
    public RoundResult() {
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
    public int getMachineCode() {
        return this.machineCode;
    }
    public void setMachineCode(int machineCode) {
        this.machineCode = machineCode;
    }
    public int getRoundNo() {
        return this.roundNo;
    }
    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }
    public int getTestNo() {
        return this.testNo;
    }
    public void setTestNo(int testNo) {
        this.testNo = testNo;
    }
    public int getMachineResult() {
        return this.machineResult;
    }
    public void setMachineResult(int machineResult) {
        this.machineResult = machineResult;
    }
    public int getPenaltyNum() {
        return this.penaltyNum;
    }
    public void setPenaltyNum(int penaltyNum) {
        this.penaltyNum = penaltyNum;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getResult2() {
        return this.result2;
    }
    public void setResult2(String result2) {
        this.result2 = result2;
    }
    public int getWight() {
        return this.wight;
    }
    public void setWight(int wight) {
        this.wight = wight;
    }
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getResultState() {
        return this.resultState;
    }
    public void setResultState(int resultState) {
        this.resultState = resultState;
    }
    public int getIsLastResult() {
        return this.isLastResult;
    }
    public void setIsLastResult(int isLastResult) {
        this.isLastResult = isLastResult;
    }
    public int getExamType() {
        return this.examType;
    }
    public void setExamType(int examType) {
        this.examType = examType;
    }
    public String getTestTime() {
        return this.testTime;
    }
    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
    public String getPrintTime() {
        return this.printTime;
    }
    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getStumbleCount() {
        return this.stumbleCount;
    }
    public void setStumbleCount(int stumbleCount) {
        this.stumbleCount = stumbleCount;
    }
    public int getUpdateState() {
        return this.updateState;
    }
    public void setUpdateState(int updateState) {
        this.updateState = updateState;
    }
    public byte[] getCycleResult() {
        return this.cycleResult;
    }
    public void setCycleResult(byte[] cycleResult) {
        this.cycleResult = cycleResult;
    }
    public String getScheduleNo() {
        return this.scheduleNo;
    }
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }
    public String getMtEquipment() {
        return this.mtEquipment;
    }
    public void setMtEquipment(String mtEquipment) {
        this.mtEquipment = mtEquipment;
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
    public String getSubitemCode() {
        return this.subitemCode;
    }
    public void setSubitemCode(String subitemCode) {
        this.subitemCode = subitemCode;
    }

}
