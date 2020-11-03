package com.gwm.http;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pengjf on 2018/10/9.
 * 深圳市菲普莱体育发展有限公司   秘密级别:绝密
 * <p>
 * 结果集的处理
 */

public class HttpResult<T> implements Serializable {
    public int code;
    public String msg;
    public T data;
    public String sign;
    public int encrypt = ENCRYPT_FALSE;
    public static int ENCRYPT_TRUE = 1;
    public static int ENCRYPT_FALSE = 0;


}
