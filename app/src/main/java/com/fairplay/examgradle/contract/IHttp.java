package com.fairplay.examgradle.contract;

import com.fairplay.examgradle.bean.BaseBean;
import com.fairplay.examgradle.bean.ItemGroupBean;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.bean.ScheduleInfoBean;
import com.fairplay.examgradle.bean.SiteStudentBean;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.json.JSON;
import com.gwm.retrofit.Observable;

public interface IHttp {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @HTTP(url = "/auth/terminal/token",way = HTTP.WAY.POST)
    Observable<LoginBean> login(@Query("username")String username,@Query("password")String password);

    /**
     * 下载项目
     * @param json
     * @return
     */
    @HTTP(url = "/run/downItemInfo",way = HTTP.WAY.POST)
    Observable<ItemInfoBean> downItemInfo(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载日程
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleInfo",way = HTTP.WAY.POST)
    Observable<ScheduleInfoBean> downSiteScheduleInfo(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载考点学生
     * @param json
     * @return
     */
    @HTTP(url = " /run/downSiteStudent",way = HTTP.WAY.POST)
    Observable<SiteStudentBean> downSiteStudent(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载考点日程项目考生
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleItemStudent",way = HTTP.WAY.POST)
    Observable<SiteStudentBean> downSiteScheduleItemStudent(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载考点日程分组信息
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleItemGroup",way = HTTP.WAY.POST)
    Observable<ItemGroupBean> downSiteScheduleItemGroup(@HeaderString ("Authorization")String token,@JSON String json);
    /**
     * 下载考点日程分组单独⼀组信息
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleItemGroupByGroupNo",way = HTTP.WAY.POST)
    Observable<ItemGroupBean> downSiteScheduleItemGroupByGroupNo(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 成绩上传
     * @param json
     * @return
     */
    @HTTP(url = "/run/uploadStudentResult",way = HTTP.WAY.POST)
    Observable<BaseBean> uploadStudentResult(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 检查成绩是否上传
     * @param json
     * @return
     */
    @HTTP(url = "/run/checkStudentResult",way = HTTP.WAY.POST)
    Observable<BaseBean<Integer>> checkStudentResult(@HeaderString ("Authorization")String token,@JSON String json);
}
