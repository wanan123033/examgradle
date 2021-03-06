package com.fairplay.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Arrays;

@Entity
public class RoundResult {
    @Id(autoincrement = true)
    private Long id;//项目轮次成绩ID
    @NotNull
    private String studentCode;
    @NotNull
    private String itemCode;//默认为default
    private String subitemCode;//默认为default
    private int grouptype;
    @NotNull
    private int machineCode;
    @NotNull
    private int roundNo;//轮次
    @NotNull
    private int testNo;//测试次数
    private String machineResult;// 获取到的机器成绩
    private int penaltyNum;// 判罚值   判罚值有正负
    private String result;//成绩      单位为"毫米(mm)"、"毫秒(ms)"、"克(g)"、"次","毫升"

    private String result2; //成绩2

    private String score;//分数      没有单位  根据成绩和得分比例系数算出分数
    private String machineScore;// 获取到的机器成绩
    @NotNull
    private int resultState;//成绩状态 //是否犯规 0:未检录 1:正常 2:犯规 3:中退 4:弃权 5:测试     体侧系统没有中退和放弃,且犯规均为机器判定的犯规
    @NotNull
    private int isLastResult;//是否为最终成绩 0-不是 1-是     身高体重最后成绩即为最好成绩
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
    private int isMultioleResult; // 是否启用多值属性  0:不启用 1:启用
    private String remark1;
    private String remark2;
    private String remark3;
    private int trackNo;
    private String itemName;
    private String unit;
    private int groundNo;
    private String examPlaceName;
    private String userInfo;
    @Generated(hash = 709942273)
    public RoundResult(Long id, @NotNull String studentCode, @NotNull String itemCode,
            String subitemCode, int grouptype, int machineCode, int roundNo, int testNo,
            String machineResult, int penaltyNum, String result, String result2, String score,
            String machineScore, int resultState, int isLastResult, int examType,
            @NotNull String testTime, String printTime, String endTime, int stumbleCount,
            int updateState, byte[] cycleResult, String scheduleNo, String mtEquipment,
            int isMultioleResult, String remark1, String remark2, String remark3, int trackNo,
            String itemName, String unit, int groundNo, String examPlaceName, String userInfo) {
        this.id = id;
        this.studentCode = studentCode;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.grouptype = grouptype;
        this.machineCode = machineCode;
        this.roundNo = roundNo;
        this.testNo = testNo;
        this.machineResult = machineResult;
        this.penaltyNum = penaltyNum;
        this.result = result;
        this.result2 = result2;
        this.score = score;
        this.machineScore = machineScore;
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
        this.isMultioleResult = isMultioleResult;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.trackNo = trackNo;
        this.itemName = itemName;
        this.unit = unit;
        this.groundNo = groundNo;
        this.examPlaceName = examPlaceName;
        this.userInfo = userInfo;
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
    public String getSubitemCode() {
        return this.subitemCode;
    }
    public void setSubitemCode(String subitemCode) {
        this.subitemCode = subitemCode;
    }
    public int getGrouptype() {
        return this.grouptype;
    }
    public void setGrouptype(int grouptype) {
        this.grouptype = grouptype;
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
    public String getMachineResult() {
        return this.machineResult;
    }
    public void setMachineResult(String machineResult) {
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
    public int getIsMultioleResult() {
        return this.isMultioleResult;
    }
    public void setIsMultioleResult(int isMultioleResult) {
        this.isMultioleResult = isMultioleResult;
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
    public int getTrackNo() {
        return this.trackNo;
    }
    public void setTrackNo(int trackNo) {
        this.trackNo = trackNo;
    }
    public String getItemName() {
        return this.itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public int getGroundNo() {
        return this.groundNo;
    }
    public void setGroundNo(int groundNo) {
        this.groundNo = groundNo;
    }
    public String getExamPlaceName() {
        return this.examPlaceName;
    }
    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }
    public String getUserInfo() {
        return this.userInfo;
    }
    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "RoundResult{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", subitemCode='" + subitemCode + '\'' +
                ", grouptype=" + grouptype +
                ", machineCode=" + machineCode +
                ", roundNo=" + roundNo +
                ", testNo=" + testNo +
                ", machineResult='" + machineResult + '\'' +
                ", penaltyNum=" + penaltyNum +
                ", result='" + result + '\'' +
                ", result2='" + result2 + '\'' +
                ", score='" + score + '\'' +
                ", machineScore='" + machineScore + '\'' +
                ", resultState=" + resultState +
                ", isLastResult=" + isLastResult +
                ", examType=" + examType +
                ", testTime='" + testTime + '\'' +
                ", printTime='" + printTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", stumbleCount=" + stumbleCount +
                ", updateState=" + updateState +
                ", cycleResult=" + Arrays.toString(cycleResult) +
                ", scheduleNo='" + scheduleNo + '\'' +
                ", mtEquipment='" + mtEquipment + '\'' +
                ", isMultioleResult=" + isMultioleResult +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", trackNo=" + trackNo +
                ", itemName='" + itemName + '\'' +
                ", unit='" + unit + '\'' +
                ", groundNo=" + groundNo +
                ", examPlaceName='" + examPlaceName + '\'' +
                ", userInfo='" + userInfo + '\'' +
                '}';
    }
}
