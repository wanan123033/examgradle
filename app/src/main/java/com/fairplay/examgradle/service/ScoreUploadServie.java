package com.fairplay.examgradle.service;

import android.app.IntentService;
import android.content.Intent;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.httppresenter.ScoreUploadPresenter;
import com.gwm.base.BaseApplication;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;

public class ScoreUploadServie extends IntentService {
    /****************************key值定义***************************************************/
    public static final String SCHEDULEID = "SCHEDULEID";     //日程ID
    public static final String ITEMID = "ITEMID";        //项目表ID
    public static final String GROUPID = "GROUPID";        //分组表ID
    public static final String ROUNDID = "ROUNDID";        //成绩表ID
    public static final String STUDENTCODE = "STUDENTCODE";        //准考证号

    private long roundId;
    public ScoreUploadServie() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        roundId = intent.getLongExtra(ROUNDID,0);

        RoundResult roundResult = DBManager.getInstance().getRoundResultById(roundId);
        MMKV mmkv = BaseApplication.getInstance().getMmkv();
        long mqttId = mmkv.getLong(MMKVContract.MQTT_ID, 0);
        MqttBean mqttBean = DBManager.getInstance().getMQTTBean(mqttId);
        StudentGroupItem group = new StudentGroupItem();
        group.setExamPlaceName(mqttBean.getExamPlaceName());
        group.setGroupNo(mqttBean.getGroupNo());
        group.setSortName(mqttBean.getSortName());
        group.setGroupType(Integer.parseInt(mqttBean.getGroupType()));
        ScoreUploadPresenter presenter = new ScoreUploadPresenter();
        try {
            presenter.scoreUpload(mqttBean.getTrackNo(),roundResult,group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
