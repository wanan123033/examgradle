package com.fairplay.examgradle.service;

import android.app.IntentService;
import android.content.Intent;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
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

    private long scheduleId;
    private long itemId;
    private long groupId;
    private long roundId;
    public ScoreUploadServie() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        scheduleId = intent.getLongExtra(SCHEDULEID,0);
        itemId = intent.getLongExtra(ITEMID,0);
        groupId = intent.getLongExtra(GROUPID,0);
        roundId = intent.getLongExtra(ROUNDID,0);

        Item item = DBManager.getInstance().getItemByItemId(itemId);
        StudentGroupItem group = DBManager.getInstance().getGroupById(groupId);
        RoundResult roundResult = DBManager.getInstance().getRoundResultById(roundId);
        Schedule schedule = DBManager.getInstance().getScheduleById(scheduleId);
        MMKV mmkv = BaseApplication.getInstance().getMmkv();
        String examPlaceName = mmkv.getString(MMKVContract.EXAMPLACENAME,"广西民族大学西校区相思湖学院篮球馆");
        String groupNo = mmkv.getString(MMKVContract.GROUPNO,"17");
        String sortName = mmkv.getString(MMKVContract.SORTNAME,"组");
        int groupType = mmkv.getInt(MMKVContract.GROUPTYPE,0);
        String trackNo = mmkv.getString(MMKVContract.TRACKNO,"1");
        group = new StudentGroupItem();
        group.setExamPlaceName(examPlaceName);
        group.setGroupNo(groupNo);
        group.setSortName(sortName);
        group.setGroupType(groupType);
        ScoreUploadPresenter presenter = new ScoreUploadPresenter();
        try {
            presenter.scoreUpload(trackNo,roundResult,group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
