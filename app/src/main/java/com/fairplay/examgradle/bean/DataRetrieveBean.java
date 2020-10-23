package com.fairplay.examgradle.bean;

public class DataRetrieveBean {
    private String studentCode;
    private String studentName;
    private int sex;
    private String score;
    private String faition;
    private int wight;

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setFaition(String faition) {
        this.faition = faition;
    }

    public String getFaition() {
        return faition;
    }

    public void setWight(int wight) {
        this.wight = wight;
    }

    public int getWight() {
        return wight;
    }

    @Override
    public String toString() {
        return "DataRetrieveBean{" +
                "studentCode='" + studentCode + '\'' +
                ", studentName='" + studentName + '\'' +
                ", sex=" + sex +
                ", score='" + score + '\'' +
                ", faition='" + faition + '\'' +
                ", wight=" + wight +
                '}';
    }
}
