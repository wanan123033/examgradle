package com.fairplay.examgradle.httppresenter;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.gwm.retrofit.Observable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ScoreUploadPresenter extends JsonDataPresenter<ScoreUploadPresenter.ScoreUploadInfo, BaseBean> {

    private RoundResult result;
    public ScoreUploadPresenter() {
        super(ScoreUploadInfo.class);
    }

    /**
     * 成绩上传
     */
    public void scoreUpload(String trackNo, RoundResult result, MqttBean groupItem, Item item) throws JSONException {
        this.result = result;
        String userInfo = result.getUserInfo();
        JSONObject jsonObject = new JSONObject();
        JSONArray roundJsonArr = new JSONArray();
        if (result.getIsMultioleResult() == 1){
            //TODO 有多个成绩时。
            List<MultipleResult> multipleResults = DBManager.getInstance().getMultioleResult(result.getId());
            JSONArray multipJsonArr = new JSONArray();
            for (int i = 0 ; i < multipleResults.size() ; i++){
                JSONObject multiple = new JSONObject();
                if (item.getMarkScore() == 0 && "m".equals(item.getUnit().trim())){
                    multiple.put("unit","mm");
                    if (!TextUtils.isEmpty(multipleResults.get(i).getScore())) {
                        Double d = Double.parseDouble(multipleResults.get(i).getScore()) * 1000.0;
                        multiple.put("score", d.intValue());
                    }
                    if (!TextUtils.isEmpty(multipleResults.get(i).getMachineScore())) {
                        Double dd = Double.parseDouble(multipleResults.get(i).getMachineScore()) * 1000.0;
                        multiple.put("machineScore", dd.intValue());
                    }
                }else {
                    multiple.put("score",multipleResults.get(i).getScore());
                    multiple.put("machineScore",multipleResults.get(i).getMachineScore());
                    multiple.put("unit",multipleResults.get(i).getUnit());
                }
                multiple.put("order",multipleResults.get(i).getOrder());
                multiple.put("desc",multipleResults.get(i).getDesc());
                multiple.put("scoreMultiple",multipleResults.get(i).getScoreMultiple());
                multipJsonArr.put(multiple);
            }
            JSONObject roundJson = new JSONObject();
            roundJson.put("multipleValue",multipJsonArr);
            roundJson.put("trackNo",trackNo);
            roundJson.put("isFoul",1);
            roundJson.put("roundNo",result.getRoundNo()+"");
            roundJson.put("resultStatus",result.getResultState());
            roundJson.put("testTime",result.getTestTime());


            roundJson.put("examState",result.getExamType());
            roundJson.put("printTime",result.getTestTime());
            roundJson.put("resultType",1);
            roundJson.put("msEquipment",getDeviceInfo());
            roundJson.put("uploadTime",System.currentTimeMillis()+"");
            roundJson.put("userInfo",userInfo);
            roundJsonArr.put(roundJson);
        }else {
            //TODO 没有多个成绩时
            JSONObject roundJson = new JSONObject();
            roundJson.put("trackNo",trackNo);
            roundJson.put("roundNo",result.getRoundNo()+"");
            roundJson.put("isFoul",1);
            roundJson.put("resultStatus",result.getResultState());
            roundJson.put("testTime",result.getTestTime());
            roundJson.put("examState",result.getExamType());
            roundJson.put("printTime",result.getTestTime());
            roundJson.put("uploadTime",System.currentTimeMillis()+"");
            if (item.getMarkScore() == 0 && "m".equals(item.getUnit().trim())){
                if (!TextUtils.isEmpty(result.getResult())) {
                    Double dd = Double.parseDouble(result.getResult()) * 1000.0;
                    roundJson.put("result", dd.intValue() + "");
                }
                if (!TextUtils.isEmpty(result.getMachineResult())) {
                    Double dd = Double.parseDouble(result.getMachineResult()) * 1000.0;
                    roundJson.put("machineResult", dd.intValue() + "");
                }
                if (!TextUtils.isEmpty(result.getScore())) {
                    Double dd = Double.parseDouble(result.getScore()) * 1000.0;
                    roundJson.put("score",dd.intValue() + "");
                }
                if (!TextUtils.isEmpty(result.getMachineScore())) {
                    Double dd = Double.parseDouble(result.getMachineScore()) * 1000.0;
                    roundJson.put("machineScore", dd.intValue() + "");
                }
            }else {
                if (!TextUtils.isEmpty(result.getResult()))
                    roundJson.put("result",result.getResult());
                if (!TextUtils.isEmpty(result.getMachineResult()))
                    roundJson.put("machineResult",result.getMachineResult());
                if (!TextUtils.isEmpty(result.getScore()))
                    roundJson.put("score",result.getScore());
                if (!TextUtils.isEmpty(result.getMachineScore()))
                    roundJson.put("machineScore",result.getMachineScore());
            }
            roundJson.put("msEquipment",getDeviceInfo());
            roundJson.put("userInfo",userInfo);
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
        if (response.code == 0 && response.msg.equals("上传成功")){
            DBManager.getInstance().updateResultStateUnload(result);
            ToastUtils.showLong("上传成绩成功");
        }else {
            ToastUtils.showLong("上传成绩失败");
        }
    }

    public interface ScoreUploadInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
