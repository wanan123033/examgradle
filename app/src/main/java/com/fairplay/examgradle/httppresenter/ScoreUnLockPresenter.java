package com.fairplay.examgradle.httppresenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreUnLockPresenter extends JsonDataPresenter<ScoreUnLockPresenter.ScoreUnLockInfo, BaseBean> {

    public ScoreUnLockPresenter() {
        super(ScoreUnLockInfo.class);
    }

    public void scoreUnLock(StudentGroupItem mqttBean, Item item, int roundNo){
        int examState = BaseApplication.getInstance().getMmkv().getInt(MMKVContract.EXAMTYPE, 0);
        String userInfo = BaseApplication.getInstance().getMmkv().getString(MMKVContract.USERNAME,"");
        String roundResult = getJsonCreator().roundResult(mqttBean.getTrackNo(),
                roundNo + "",
                1,
                examState,
                System.currentTimeMillis() + "",
                userInfo,
                getDeviceInfo(),
                "");
        String getJson = null;
        try {
            getJson = genJsonString(100020173,getJsonCreator().data(mqttBean.getScheduleNo(),
                    mqttBean.getItemCode(),mqttBean.getSubitemCode(),mqttBean.getStudentCode(),
                    item.getTestNum()+"",mqttBean.getExamPlaceName(),mqttBean.getGroupNo()+"",mqttBean.getGroupType()+"",mqttBean.getSortName(),
                    "1",item.getMachineCode(),item.getItemName(),new JSONObject(roundResult)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Observable<BaseBean> observable = getHttpPresenter().unlockStudentResult(getToken(),getJson);
        addHttpSubscriber("",observable,BaseBean.class);
    }

    @Override
    protected void onNextResult(BaseBean response, int id) {
        if (response.code == 1){
            ToastUtils.showShort(response.msg);
        }
    }

    public interface ScoreUnLockInfo extends JsonDataPresenter.HttpBaseBean{
        @JSON
        String roundResult(@Param("trackNo")String trackNo, // 道号
                           @Param("roundNo")String roundNo,  // 轮次号
                           @Param("resultStatus")int resultStatus, // 成绩状态: 0:未检录 1:正常 2:犯规 3:中退 4:弃权 5:测试
                           @Param("examState")int examState,  // 考试状态（0.正常,1.缓考,2.补考）
                           @Param("testTime")String testTime,  // 测试时间
                           @Param("userInfo")String userInfo,   // 测试用户信息
                           @Param("msEquipment")String msEquipment,   // 测试设备信息
                           @Param("mtEquipment")String mtEquipment);   // 监控设备信息

        @JSON
        String data(@Param("siteScheduleNo")String siteScheduleNo, // 日程编号
                    @Param("examItemCode")String examItemCode,  // 项目代码
                    @Param("subitemCode")String subitemCode, // 子项目代码
                    @Param("studentCode")String studentCode, // 准考证号
                    @Param("testNum")String testNum,   // 测试次数
                    @Param("examPlaceName")String examPlaceName,  // 场地名称
                    @Param("groupNo")String groupNo, // 组号
                    @Param("groupType")String groupType,  // 分组类型(0:男子,1：女子,2：混合)
                    @Param("sortName")String sortName,  // 组别
                    @Param("hostNumber")String hostNumber,  // 主机序号
                    @Param("machineCode")String machineCode, // 机器码
                    @Param("examItemName")String examItemName,  // 项目名称
                    @Param("roundResult")JSONObject roundResult);  // 轮次成绩
    }
}
