package com.fairplay.examgradle.bean;

public class BaseBean<T> {
    public int code;
    public String msg;
    public T data;
    public String sign;
    public int encrypt;
    public long responseTime;
}
