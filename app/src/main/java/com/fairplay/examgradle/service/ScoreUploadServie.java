package com.fairplay.examgradle.service;

import android.app.IntentService;
import android.content.Intent;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.httppresenter.ScoreUploadPresenter;
import com.gwm.base.BaseApplication;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;

public class ScoreUploadServie extends IntentService {
    /****************************key值定义***************************************************/
    public static final String ROUNDID = "ROUNDID";        //成绩表ID


    private long roundId;
    public ScoreUploadServie() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        roundId = intent.getLongExtra(ROUNDID,0);

        RoundResult roundResult = DBManager.getInstance().getRoundResultById(roundId);
        Item item = DBManager.getInstance().getItemByItemCode(roundResult.getItemCode(),roundResult.getSubitemCode());
        MMKV mmkv = BaseApplication.getInstance().getMmkv();
        long mqttId = mmkv.getLong(MMKVContract.MQTT_ID, 0);
        MqttBean mqttBean = DBManager.getInstance().getMQTTBean(mqttId);
        ScoreUploadPresenter presenter = new ScoreUploadPresenter();
        try {
            presenter.scoreUpload(mqttBean.getTrackNo(),roundResult,mqttBean,item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
