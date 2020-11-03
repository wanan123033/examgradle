package com.fairplay.examgradle.httppresenter;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.GroupInfoBean;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.geek.thread.GeekThreadPools;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.retrofit.Observable;

public class DownGroupInfoPresenter extends JsonDataPresenter<DownGroupInfoPresenter.DownGroupInfo, GroupInfoBean> {
    private int examType;

    public DownGroupInfoPresenter() {
        super(DownGroupInfo.class);
    }

    @Override
    protected void onNextResult(GroupInfoBean response, int id) {
        GeekThreadPools.executeWithGeekThreadPool(new Runnable() {
            @Override
            public void run() {
                if (response.data != null){
                    if (response.data.dataInfo != null && !response.data.dataInfo.isEmpty()){
                        for (GroupInfoBean.Group group : response.data.dataInfo){
                            StudentGroupItem studentGroupItem = new StudentGroupItem();
                            studentGroupItem.setScheduleNo(group.scheduleNo);
                            studentGroupItem.setGroupNo(group.groupNo);
                            studentGroupItem.setItemCode(group.examItemCode);
                            studentGroupItem.setSubitemCode(group.subitemCode);
                            studentGroupItem.setExamPlaceName(group.examPlaceName);
                            studentGroupItem.setRollCallPlaceName(group.rollCallPlaceName);
                            studentGroupItem.setGroupType(group.groupType);
                            studentGroupItem.setSortName(group.sortName);
                            DBManager.getInstance().insertGroup(studentGroupItem);
                        }
                        if (response.data.batch != response.data.batchTotal && response.data.batch != 0){
                            downGroup(response.data.batch + 1,examType);
                        }
                    }
                }
            }
        });

    }

    public void downGroup(int batch,int examType){
        this.examType = examType;
        String getJson = genJsonString(100020130,getJsonCreator().downGroup(batch,examType,0));
        String token = getToken();
        Observable<GroupInfoBean> observable = getHttpPresenter().downGroup(token,getJson);
        addHttpSubscriber(observable,GroupInfoBean.class);
    }

    public interface DownGroupInfo extends JsonDataPresenter.HttpBaseBean{
        /**
         *     "batch": 1, //分批查询，批号
         *     "batchTotal": 1, //分批查询，总批数
         *     "examType": 0, //考生考试状态
         *     "downStudent": 1,  // 下载考生(空或0 下载， 1：不下载)
         * @return
         */
        @JSON
        String downGroup(@Param("batch")int batch,
                         @Param("examType")int examType,
                         @Param("downStudent")int downStudent);
    }
}
