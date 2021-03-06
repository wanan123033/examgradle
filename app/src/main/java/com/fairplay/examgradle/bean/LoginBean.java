package com.fairplay.examgradle.bean;

import java.util.List;

public class LoginBean extends BaseBean<LoginBean.Login> {
    public static class Login{
        public String examName;
        public String siteId;
        public String typeValue;
        public String type;
        public String channelCode;
        public int multiSchedule;
        public List<String> permission;
        public String token;
        public String equipmentStatus;

        @Override
        public String toString() {
            return "Login{" +
                    "examName='" + examName + '\'' +
                    ", siteId='" + siteId + '\'' +
                    ", typeValue='" + typeValue + '\'' +
                    ", type='" + type + '\'' +
                    ", channelCode='" + channelCode + '\'' +
                    ", multiSchedule=" + multiSchedule +
                    ", permission=" + permission +
                    ", token='" + token + '\'' +
                    ", equipmentStatus='" + equipmentStatus + '\'' +
                    '}';
        }
    }
}
