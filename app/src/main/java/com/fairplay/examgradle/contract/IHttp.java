package com.fairplay.examgradle.contract;

import com.fairplay.examgradle.bean.LoginBean;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.Query;
import com.gwm.retrofit.Observable;

public interface IHttp {

    @HTTP(url = "/app/login",way = HTTP.WAY.POST)
    Observable<LoginBean> login(@Query("username")String username,@Query("password")String password);
}
