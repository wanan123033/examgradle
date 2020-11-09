package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.retrofit.Observable;

public class DownGroupPresenter extends JsonDataPresenter<DownGroupPresenter.DownGroupInfo, BaseBean> {

    public DownGroupPresenter() {
        super(DownGroupInfo.class);
    }

    /**
     * "scheduleNo": "", //日程编号 可为空
     *                 "examItemCode": "", // 考试项目编码
     *                 "subitemCode": "", // 子项代码
     *                 "sortName": "", //组别
     *                 "groupNo": "", //分组
     *                 "groupType": "", //分组类别（0：男子，1：女子，2：混合）
     *                 "examStatus": 0, // 考试状态,0:正常 1.缓考，2.补考
     *                 "downStudent": 1,  // 下载考生(空或0 下载， 1：不下载)
     *                 "examPlaceName": ""  // 场地名
     * @param scheduleNo
     */
    public void downStudent(String scheduleNo,String examItemCode,String subitemCode,String sortName,String groupNo,String groupType,String examPlaceName){
        String json = getJsonCreator().createJson(scheduleNo,examItemCode,subitemCode,sortName,groupNo,groupType,0,0,examPlaceName);
        String s = genJsonString(100020131, json);
        String token = getToken();
        Observable<BaseBean> observable = getHttpPresenter().upload(token,s);
        addHttpSubscriber(observable,BaseBean.class);
    }

    @Override
    protected void onNextResult(BaseBean response, int id) {

    }

    public interface DownGroupInfo extends JsonDataPresenter.HttpBaseBean{
        @JSON
        String createJson(@Param("scheduleNo")String scheduleNo,
                          @Param("examItemCode")String examItemCode,
                          @Param("subitemCode")String subitemCode,
                          @Param("sortName")String sortName,
                          @Param("groupNo")String groupNo,
                          @Param("groupType")String groupType,
                          @Param("examStatus")int examStatus,
                          @Param("downStudent")int downStudent,
                          @Param("examPlaceName")String examPlaceName);
    }
}
