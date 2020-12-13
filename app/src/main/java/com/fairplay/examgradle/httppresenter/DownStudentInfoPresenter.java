package com.fairplay.examgradle.httppresenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.ExamPlace;
import com.fairplay.database.entity.GroupInfo;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.GroupInfoBean;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.messagesendreceive.MessageBusMessage;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

import java.util.List;

public class DownStudentInfoPresenter extends JsonDataPresenter<DownStudentInfoPresenter.DownStudentInfo, GroupInfoBean> {

    private int examType;
    private int batch;

    public DownStudentInfoPresenter() {
        super(DownStudentInfo.class);
    }


    public void downStudent(int batch, int examType){
        this.examType = examType;
        this.batch = batch;
        String data = genJsonString(100020130, getJsonCreator().downStu(batch, examType));
        String token = getToken();
        Observable<GroupInfoBean> observable = getHttpPresenter().downGroup(token, data);
        addHttpSubscriber("",observable,GroupInfoBean.class);
    }

    @Override
    protected void onNextResult(GroupInfoBean response, int id) {
        if (response.code > 0){
            ToastUtils.showShort(response.msg);
            ((BaseViewModel)getViewModel()).sendLiveData("DIMMSION_PROGREESS");
            return;
        }
        if (response.data.dataInfo != null && !response.data.dataInfo.isEmpty()){
            for (GroupInfoBean.Group group : response.data.dataInfo){
                GroupInfo dataGroup = new GroupInfo();
                dataGroup.setGroupNo(Integer.parseInt(group.groupNo));
                dataGroup.setScheduleNo(group.scheduleNo);
                dataGroup.setGroupType(group.groupType);
                dataGroup.setExamPlaceName(group.examPlaceName);
                dataGroup.setSortName(group.sortName);
                dataGroup.setItemCode(group.examItemCode);
                dataGroup.setSubItemCode(group.subitemCode);
                if (group.subitemFlag == 0){
                    dataGroup.setSubItemCode(group.examItemCode);
                }
                DBManager.getInstance().insertGroupInfo(dataGroup);
                ExamPlace place = new ExamPlace();
                place.setExamplaceName(group.examPlaceName);
                place.setItemCode(group.examItemCode);
                place.setSubItemCode(group.subitemCode);
                DBManager.getInstance().insertExamPlace(place);
                List<GroupInfoBean.StudentBean> studentCodeList = group.studentCodeList;
                if (studentCodeList != null && !studentCodeList.isEmpty()){
                    for (GroupInfoBean.StudentBean studentBean : studentCodeList){
                        StudentGroupItem studentGroupItem = new StudentGroupItem();
                        studentGroupItem.setSortName(dataGroup.getSortName());
                        studentGroupItem.setSubitemCode(dataGroup.getSubItemCode());
                        studentGroupItem.setItemCode(dataGroup.getItemCode());
                        studentGroupItem.setGroupNo(dataGroup.groupNo);
                        studentGroupItem.setGroupType(dataGroup.getGroupType());
                        studentGroupItem.setExamPlaceName(dataGroup.examPlaceName);
                        studentGroupItem.setStudentCode(studentBean.studentCode);
                        studentGroupItem.setTrackNo(Integer.parseInt(studentBean.trackNo));
                        studentGroupItem.setExamStatus(studentBean.examStatus);
                        studentGroupItem.setScheduleNo(group.scheduleNo);
                        DBManager.getInstance().insertMqttBean(studentGroupItem);
                    }
                }
            }
            if (response.data.batch != response.data.batchTotal){
                batch++;
                downStudent(batch,examType);
            }else{
                ToastUtils.showShort("数据下载成功");
                MessageBus.getBus().post(new MessageBusMessage("","DIMMSION_PROGREESS"));
                ((BaseViewModel)getViewModel()).sendLiveData("DIMMSION_PROGREESS");
            }
        }
    }

    public interface DownStudentInfo extends JsonDataPresenter.HttpBaseBean{
        @JSON
        String downStu(@Param("batch")int batch,@Param("examType")int examType);
    }
}
