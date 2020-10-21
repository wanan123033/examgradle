package com.fairplay.examgradle.httppresenter;

import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;

import org.json.JSONArray;

public class ScoreUploadPresenter extends JsonDataPresenter<ScoreUploadPresenter.ScoreUploadInfo, BaseBean<Object>> {

    public ScoreUploadPresenter() {
        super(ScoreUploadInfo.class);
    }

    /**
     * 成绩上传
     * @param item  学生报名信息,项目信息,分组信息
     *              这里利用StudentItem 获取各项信息
     */
    public void scoreUpload(StudentItem item){

    }

    @Override
    protected void onNextResult(BaseBean<Object> response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    public interface ScoreUploadInfo extends JsonDataPresenter.HttpBaseBean{

        @JSON
        String createScoreJson(@Param("siteScheduleNo") String siteScheduleNo,  // 日程编号
                               @Param("examItemCode") String examItemCode,  // 项目代码
                               @Param("subitemCode") String subitemCode,    //专项代码
                               @Param("studentCode") String studentCode,   //准考证号
                               @Param("testNum")String testNum,             //测试分数
                               @Param("examPlaceName")String examPlaceName,  //
                               @Param("groupNo")String groupNo,        //组号
                               @Param("groupType")String groupType,
                               @Param("sortName")String sortName,
                               @Param("hostNumber")String hostNumber,
                               @Param("machineCode")String machineCode,
                               @Param("examItemName")String examItemName,
                               @Param("result")String result,
                               @Param("result2")String result2,
                               @Param("score")String score,
                               @Param("testTime")String testTime,
                               @Param("resultStatus")int resultStatus,
                               @Param("extBody")JSONArray extBody,
                               @Param("roundResultList")JSONArray roundResultList);

        @JSON
        String createExtBody(@Param("extName")String extName,
                             @Param("belongType")String belongType,
                             @Param("extType")String extType,
                             @Param("extValue")JSONArray extValue);

        @JSON
        String roundResult(@Param("trackNo")String trackNo,
                           @Param("roundNo")String roundNo,
                           @Param("penalty")String penalty,
                           @Param("isFoul")int isFoul,
                           @Param("resultStatus")int resultStatus,
                           @Param("result")String result,
                           @Param("result2")String result2,
                           @Param("machineResult")String machineResult,
                           @Param("score")String score,
                           @Param("scorePenalty")String scorePenalty,
                           @Param("machineScore")String machineScore,
                           @Param("resultType")int resultType,
                           @Param("examState")int examState,
                           @Param("testTime")String testTime,
                           @Param("printTime")String printTime,
                           @Param("userInfo")String userInfo,
                           @Param("msEquipment")String msEquipment,
                           @Param("mtEquipment")String mtEquipment,
                           @Param("uploadTime")String uploadTime,
                           @Param("stumbleCount")int stumbleCount,
                           @Param("leftLens")int leftLens,
                           @Param("rightLens")int rightLens,
                           @Param("leftRefractive")int leftRefractive,
                           @Param("rightRefractive")int rightRefractive,
                           @Param("remark")String remark,
                           @Param("multipleValue")JSONArray multipleValue);

        @JSON
        String createMultipleValue(@Param("order")String order,
                                   @Param("group")String group,
                                   @Param("desc")String desc,
                                   @Param("unit")String unit,
                                   @Param("scoreMultiple")String scoreMultiple,
                                   @Param("result")String result,
                                   @Param("machineResult")String machineResult,
                                   @Param("score")String score,
                                   @Param("machineScore")String machineScore);


    }
}
