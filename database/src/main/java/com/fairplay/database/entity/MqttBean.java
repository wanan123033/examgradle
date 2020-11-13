package com.fairplay.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

@Entity(indexes = {
        @Index(value = "itemCode ASC,subitemCode ASC,examPlaceName ASC,studentCode ASC", unique = true)
})
public class MqttBean implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String itemCode;
    private String subitemCode;
    private String examPlaceName;
    private int examStatus;
    private String groupNo;
    private String groupType;
    private String sortName;
    private String trackNo;
    private String studentCode;
    private String scheduleNo;

    @Generated(hash = 2029487705)
    public MqttBean(Long id, String itemCode, String subitemCode, String examPlaceName, int examStatus,
                    String groupNo, String groupType, String sortName, String trackNo, String studentCode,
                    String scheduleNo) {
        this.id = id;
        this.itemCode = itemCode;
        this.subitemCode = subitemCode;
        this.examPlaceName = examPlaceName;
        this.examStatus = examStatus;
        this.groupNo = groupNo;
        this.groupType = groupType;
        this.sortName = sortName;
        this.trackNo = trackNo;
        this.studentCode = studentCode;
        this.scheduleNo = scheduleNo;
    }

    @Generated(hash = 1043871520)
    public MqttBean() {
    }

    protected MqttBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        itemCode = in.readString();
        subitemCode = in.readString();
        examPlaceName = in.readString();
        examStatus = in.readInt();
        groupNo = in.readString();
        groupType = in.readString();
        sortName = in.readString();
        trackNo = in.readString();
        studentCode = in.readString();
        scheduleNo = in.readString();
    }

    public static final Creator<MqttBean> CREATOR = new Creator<MqttBean>() {
        @Override
        public MqttBean createFromParcel(Parcel in) {
            return new MqttBean(in);
        }

        @Override
        public MqttBean[] newArray(int size) {
            return new MqttBean[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getExamPlaceName() {
        return this.examPlaceName;
    }

    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }

    public int getExamStatus() {
        return this.examStatus;
    }

    public void setExamStatus(int examStatus) {
        this.examStatus = examStatus;
    }

    public String getGroupNo() {
        return this.groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupType() {
        return this.groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getSortName() {
        return this.sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getTrackNo() {
        return this.trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getStudentCode() {
        return this.studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getScheduleNo() {
        return this.scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(itemCode);
        dest.writeString(subitemCode);
        dest.writeString(examPlaceName);
        dest.writeInt(examStatus);
        dest.writeString(groupNo);
        dest.writeString(groupType);
        dest.writeString(sortName);
        dest.writeString(trackNo);
        dest.writeString(studentCode);
        dest.writeString(scheduleNo);
    }
}
