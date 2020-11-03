package com.fairplay.examgradle.httppresenter;

import android.util.Log;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.utils.ToastUtils;
import com.google.gson.JsonArray;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.base.BaseApplication;
import com.gwm.retrofit.Observable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ScoreUploadPresenter extends JsonDataPresenter<ScoreUploadPresenter.ScoreUploadInfo, BaseBean> {

    public ScoreUploadPresenter() {
        super(ScoreUploadInfo.class);
    }

    /**
     * 成绩上传
     */
    public void scoreUpload(String trackNo,Item item, Schedule schedule, RoundResult roundResult, StudentGroupItem studentGroupItem,String studentCode,String hostNumber){
        int isMultioleResult = roundResult.getIsMultioleResult();
        JSONArray roundResultString = new JSONArray();
        JSONObject roundResultjsonObject;
        int examType = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE, 0);
        if (isMultioleResult == 0){   //没有多值表
            String s = getJsonCreator().roundResult(trackNo, roundResult.getRoundNo() + "", roundResult.getPenaltyNum() + "", 0, 1,
                    roundResult.getResult(), roundResult.getResult2(), roundResult.getMachineResult() + "", roundResult.getScore(), roundResult.getPenaltyNum() + "",
                    roundResult.getMachineScore() + "", 1, examType, roundResult.getTestTime(), roundResult.getTestTime(), null, null, null, System.currentTimeMillis() + "",
                    roundResult.getStumbleCount(), 0, 0, 0, 0, null, null);
            try {
                roundResultjsonObject = new JSONObject(s);
                roundResultString.put(roundResultjsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

            List<MultipleResult> multioleResult = DBManager.getInstance().getMultioleResult(roundResult.getId());
            JSONArray multioleResultjsonArray = new JSONArray();
            for (MultipleResult result : multioleResult){
                String multipleStr = getJsonCreator().createMultipleValue(result.getOrder(),
                        result.getGroup(),
                        result.getDesc(),
                        result.getUnit(),
                        result.getScoreMultiple(),
                        result.getScore(),
                        result.getMachineScore(),
                        result.getScore(),
                        result.getMachineScore());
                try {
                    JSONObject jsonObject = new JSONObject(multipleStr);
                    multioleResultjsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String s = getJsonCreator().roundResult(trackNo, roundResult.getRoundNo() + "", roundResult.getPenaltyNum() + "", 0, 1,
                    roundResult.getResult(), roundResult.getResult2(), roundResult.getMachineResult() + "", roundResult.getScore(), roundResult.getPenaltyNum() + "",
                    roundResult.getMachineScore() + "", 1, examType, roundResult.getTestTime(), roundResult.getTestTime(), null, null, null, System.currentTimeMillis() + "",
                    roundResult.getStumbleCount(), 0, 0, 0, 0, null, multioleResultjsonArray);
            try {
                roundResultjsonObject = new JSONObject(s);
                roundResultString.put(roundResultjsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String scoreJson = getJsonCreator().createScoreJson(schedule.getScheduleNo(), item.getItemCode(), item.getSubitemCode(), studentCode, item.getTestNum() + "", "广西民族大学西校区相思湖学院篮球馆",
                "1", "1", "组", hostNumber, item.getMachineCode(), item.getItemName(),
                roundResult.getResult(), roundResult.getResult2(), roundResult.getScore(), roundResult.getTestTime(), roundResult.getResultState(), null, roundResultString.toString());

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(new JSONObject(scoreJson));
            String token = getToken();
            Observable<BaseBean> observable = getHttpPresenter().uploadStudentResult(token,genJsonString(100020170,jsonArray.toString()));
            addHttpSubscriber(observable,BaseBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void scoreUpload(String trackNo,RoundResult result,StudentGroupItem groupItem) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray roundJsonArr = new JSONArray();
        if (result.getIsMultioleResult() == 1){
            //TODO 有多个成绩时。
            List<MultipleResult> multipleResults = DBManager.getInstance().getMultioleResult(result.getId());
            JSONArray multipJsonArr = new JSONArray();
            for (int i = 0 ; i < multipleResults.size() ; i++){
                JSONObject multiple = new JSONObject();
                multiple.put("order",multipleResults.get(i).getOrder());
                multiple.put("desc",multipleResults.get(i).getDesc());
                multiple.put("score",multipleResults.get(i).getScore());
                multiple.put("unit",multipleResults.get(i).getUnit());
                multiple.put("scoreMultiple",multipleResults.get(i).getScoreMultiple());
                multiple.put("machineScore",multipleResults.get(i).getMachineScore());
                multipJsonArr.put(multiple);
            }
            JSONObject roundJson = new JSONObject();
            roundJson.put("multipleValue",multipJsonArr);
            roundJson.put("trackNo",trackNo);
            roundJson.put("isFoul",1);
            roundJson.put("roundNo","1");
            roundJson.put("resultStatus",result.getResultState());
            roundJson.put("testTime",result.getTestTime());
            int examType = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
            roundJson.put("examState",examType);
            roundJson.put("printTime",result.getTestTime());
            roundJson.put("resultType",1);
            roundJson.put("msEquipment",getDeviceInfo());
            roundJson.put("uploadTime",System.currentTimeMillis()+"");
            roundJsonArr.put(roundJson);
        }else {
            //TODO 没有多个成绩时
            JSONObject roundJson = new JSONObject();
            roundJson.put("trackNo",trackNo);
            roundJson.put("roundNo","1");
            roundJson.put("isFoul",1);
            roundJson.put("resultStatus",result.getResultState());
            roundJson.put("testTime",result.getTestTime());
            int examType = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE,0);
            roundJson.put("examState",examType);
            roundJson.put("printTime",result.getTestTime());
            roundJson.put("uploadTime",System.currentTimeMillis()+"");
            roundJson.put("result",result.getResult());
            roundJson.put("machineResult",result.getMachineResult());
            roundJson.put("score",result.getScore());
            roundJson.put("machineScore",result.getMachineScore());
            roundJson.put("msEquipment",getDeviceInfo());
            Log.e("result",""+result.getScore());
            roundJsonArr.put(roundJson);
        }
        jsonObject.put("roundResultList",roundJsonArr);
        jsonObject.put("siteScheduleNo",result.getScheduleNo());
        jsonObject.put("examItemCode",result.getItemCode());
        if (!result.getSubitemCode().equals(result.getItemCode()))
            jsonObject.put("subitemCode",result.getSubitemCode());
        jsonObject.put("studentCode",result.getStudentCode());
        jsonObject.put("testNum",result.getTestNo()+"");
        jsonObject.put("examPlaceName",groupItem.getExamPlaceName());
        jsonObject.put("groupNo",groupItem.getGroupNo());
        jsonObject.put("groupType",groupItem.getGroupType()+"");
        jsonObject.put("sortName",groupItem.getSortName());
        jsonObject.put("testTime",result.getTestTime());
        jsonObject.put("hostNumber","1");
        jsonObject.put("examItemName",result.getItemName());
        jsonObject.put("resultStatus",result.getResultState());
        jsonObject.put("machineCode",result.getMachineCode());
        Log.e("TAG==>",jsonObject.toString());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        String token = getToken();
        String string = genJsonString(100020170, jsonArray.toString());
        Observable<BaseBean> observable = getHttpPresenter().uploadStudentResult(token,string);
        addHttpSubscriber(observable,BaseBean.class);
    }

    @Override
    protected void onNextResult(BaseBean response, int id) {
        if (response.code == 0){
            ToastUtils.showLong(response.msg);
        }
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
                               @Param("extBody")String extBody,           //扩展属性   对应createExtBody
                               @Param("roundResultList")String roundResultList);    //轮次成绩  roundResult

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
                           @Param("multipleValue")JSONArray multipleValue);   //多值成绩对应 createMultipleValue

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
