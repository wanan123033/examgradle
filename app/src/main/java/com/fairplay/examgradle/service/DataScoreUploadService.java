package com.fairplay.examgradle.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.CacheMemoryUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.httppresenter.ScoreUploadPresenter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DataScoreUploadService extends IntentService {
    public static final String MQTT_BEAN = "MQTT_BEAN";

    public DataScoreUploadService() {
        super("DataScoreUploadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<MqttBean> extra = CacheMemoryUtils.getInstance().get(MQTT_BEAN);
//        ArrayList<MqttBean> extra = intent.getParcelableArrayListExtra(MQTT_BEAN);

        for (MqttBean bean : extra){
            List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(bean.getStudentCode(), bean.getItemCode(), bean.getSubitemCode());
            for (RoundResult result : stuRoundResult){
                ScoreUploadPresenter presenter = new ScoreUploadPresenter();
                Item item = DBManager.getInstance().getItemByItemCode(bean.getItemCode(),bean.getSubitemCode());
                try {
                    presenter.scoreUpload(bean.getTrackNo(),result,bean,item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        CacheMemoryUtils.getInstance().clear();
    }
}
