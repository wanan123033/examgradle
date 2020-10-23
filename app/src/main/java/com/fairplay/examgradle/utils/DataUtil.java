package com.fairplay.examgradle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getDataFormat(String time){
        long timee = Long.parseLong(time);
        return sdf.format(new Date(timee));
    }
}
