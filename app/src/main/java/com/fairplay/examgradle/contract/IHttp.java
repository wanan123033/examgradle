package com.fairplay.examgradle.contract;

import com.fairplay.examgradle.bean.BaseBean;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.bean.GroupInfoBean;
import com.fairplay.examgradle.bean.ItemGroupBean;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.bean.ScheduleInfoBean;
import com.fairplay.examgradle.bean.SiteStudentBean;
import com.fairplay.examgradle.bean.TopicBean;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.json.JSON;
import com.gwm.retrofit.Observable;

import java.net.URI;

public interface IHttp {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @HTTP(url = "/auth/terminal/token")
    Observable<LoginBean> login(@HeaderString ("Authorization")String token,@Query("username")String username,@Query("password")String password);

    /**
     * 下载项目
     * @param json
     * @return
     */
    @HTTP(url = "/run/downItemInfo")
    Observable<ItemInfoBean> downItemInfo(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载日程
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleInfo")
    Observable<ScheduleInfoBean> downSiteScheduleInfo(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载考点学生
     * @param json
     * @return
     */
    @HTTP(url = " /run/downSiteStudent")
    Observable<SiteStudentBean> downSiteStudent(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 下载考点日程项目考生
     * @param json
     * @return
     */
    @HTTP(url = "/run/downSiteScheduleItemStudent")
    Observable<SiteStudentBean> downSiteScheduleItemStudent(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 成绩上传
     * @param json
     * @return
     */
    @HTTP(url = "/run/uploadStudentResult")
    Observable<BaseBean> uploadStudentResult(@HeaderString ("Authorization")String token,@JSON String json);

    /**
     * 检查成绩是否上传
     * @param json
     * @return
     */
    @HTTP(url = "/run/checkStudentResult")
    Observable<BaseBean<Integer>> checkStudentResult(@HeaderString ("Authorization")String token,@JSON String json);

    @HTTP(url = "/run/downEnvInfo")
    Observable<EnvInfoBean> downEnvInfo(@HeaderString ("Authorization")String token,@JSON String json);

    @HTTP(url = "/run/downSiteScheduleItemGroup")
    Observable<GroupInfoBean> downGroup(@HeaderString ("Authorization")String token,@JSON String json);
    @HTTP(url = "/run/downSiteScheduleItemGroup")
    Observable<BaseBean> upload(@HeaderString ("Authorization")String token,@JSON String json);
    @HTTP(url = "/run/scanQrResp")
    Observable<TopicBean> scanQrResp(@HeaderString ("Authorization")String token, @JSON String json);

    @HTTP(url = "/run/unlockStudentResult")
    Observable<BaseBean> unlockStudentResult(@HeaderString ("Authorization")String token, @JSON String json);
}
