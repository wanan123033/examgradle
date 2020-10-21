package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.bean.SiteStudentBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

public class DownStudentInfoPresenter extends JsonDataPresenter<DownStudentInfoPresenter.DownStudentInfo, SiteStudentBean> {
    public DownStudentInfoPresenter() {
        super(DownStudentInfo.class);
    }

    @Override
    protected void onNextResult(SiteStudentBean response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    /**
     * 下载考点学生
     * @param batch  分批查询，批号
     * @param batchTotal  分批查询，总批数（总页数）--可为空
     * @param examType  考生考试状态
     */
    public void downStudent(int batch,int batchTotal,int examType) {
        String dataJson = getJsonCreator().downStudentInfo(batch,batchTotal,examType,0,null);
        String json = genJsonString(100020120,dataJson);
        String token = getToken();
        Observable<SiteStudentBean> siteStudentBeanObservable = getHttpPresenter().downSiteStudent(token, json);
        addHttpSubscriber(siteStudentBeanObservable,SiteStudentBean.class);
    }

    /**
     * 下载考点日程项目学生
     * @param scheduleNo   日程编号
     * @param examItemCode  考试项目编号
     * @param subitemCode   考试子项代码
     * @param batch  分批查询，批号
     * @param batchTotal  分批查询，总批数（总页数）--可为空
     * @param examType  考生考试状态
     */
    public void downSiteItemStudent(String scheduleNo,String examItemCode,String subitemCode,int batch,int batchTotal,int examType){
        String dataJson = getJsonCreator().downSiteItemStudentInfo(scheduleNo,examItemCode,subitemCode,batch,batchTotal,examType,0,null);
        String json = genJsonString(100020121,dataJson);
        String token = getToken();
        Observable<SiteStudentBean> siteStudentBeanObservable = getHttpPresenter().downSiteScheduleItemStudent(token, json);
        addHttpSubscriber(siteStudentBeanObservable,SiteStudentBean.class);
    }

    public interface DownStudentInfo extends JsonDataPresenter.HttpBaseBean{
        @JSON
        String downStudentInfo(@Param("batch")int batch,   //分批查询，批号
                                   @Param("batchTotal")int batchTotal,  //分批查询，总批数（总页数）--可为空
                                   @Param("examType")int examType,  //考生考试状态
                                   @Param("getInfoType")int getInfoType,  // 0:根据项目代码去查询学生信息 1:只查询学生信息，不返回图片什么的
                                   @Param("studentCoeList")JSONArray studentCoeList);   // 学生列表

        @JSON
        String downSiteItemStudentInfo(@Param("scheduleNo")String scheduleNo,  // 日程编号 可为空，下发考点项目考生，如果为空将只下发开启的日程项目考生
                                       @Param("examItemCode")String examItemCode,   // 考试项目代码 可为空
                                       @Param("subitemCode")String subitemCode,  // ⼦项代码
                                       @Param("batch")int batch,   //分批查询，批号
                                       @Param("batchTotal")int batchTotal,  //分批查询，总批数（总页数）--可为空
                                       @Param("examType")int examType,  //考生考试状态
                                       @Param("getInfoType")int getInfoType,  // 0:根据项目代码去查询学生信息 1:只查询学生信息，不返回图片什么的
                                       @Param("studentCoeList")JSONArray studentCoeList);   // 学生列表
    }
}
