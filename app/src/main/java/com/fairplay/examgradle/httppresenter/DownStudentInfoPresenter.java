package com.fairplay.examgradle.httppresenter;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.bean.SiteStudentBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.geek.thread.GeekThreadPools;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.base.BaseActivity;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DownStudentInfoPresenter extends JsonDataPresenter<DownStudentInfoPresenter.DownStudentInfo, SiteStudentBean> {
    private int batch;
    private String scheduleNo;
    private int examType;

    public DownStudentInfoPresenter() {
        super(DownStudentInfo.class);
    }

    @Override
    protected void onNextResult(SiteStudentBean response, int id) {
        GeekThreadPools.executeWithGeekThreadPool(new Runnable() {
            @Override
            public void run() {
                if (response.data != null){
                    List<SiteStudentBean.Student> dataInfo = response.data.dataInfo;
                    if (dataInfo != null && !dataInfo.isEmpty()){
                        for (SiteStudentBean.Student stu : dataInfo){
                            List<SiteStudentBean.SubItem> subitemList = stu.subitemList;
                            if (subitemList != null && !subitemList.isEmpty()) {
                                for (SiteStudentBean.SubItem subItem : subitemList) {
                                    StudentItem studentItem = new StudentItem();
                                    studentItem.setItemCode(stu.examItemCode);
                                    studentItem.setSubitemCode(subItem.subitemCode);
                                    studentItem.setMachineCode(subItem.machineCode);
                                    studentItem.setScheduleNo(stu.scheduleNo);
                                    studentItem.setExamType(stu.studentType);
                                    studentItem.setStudentCode(stu.studentCode);
                                    DBManager.getInstance().insertStudentItem(studentItem);
                                }
                            }
                            Student student = new Student();
                            student.setStudentCode(stu.studentCode);
                            student.setSchoolName(stu.schoolName);
                            student.setStudentName(stu.studentName);
                            student.setClassName(stu.className);
                            student.setIcCardNo(stu.icCard);
                            student.setIdCardNo(stu.idCard);
                            student.setSex(stu.gender);
                            DBManager.getInstance().insertStudent(student);
                        }
                    }
                    if (batch == response.data.batchTotal || response.data.batchTotal == 0){ // 已经到了最后一页了
                        ((BaseViewModel)getViewModel()).sendLiveData(BaseActivity.DIMMSION_PROGREESS);
                        return;
                    }else {
                        downSiteItemStudent(scheduleNo,batch + 1,examType);
                    }
                }
            }
        });
    }


    /**
     * 下载考点日程项目学生
     * @param scheduleNo   日程编号
     * @param batch  分批查询，批号
     * @param examType  考生考试状态
     */
    public void downSiteItemStudent(String scheduleNo,int batch,int examType){
        this.batch = batch;
        this.scheduleNo = scheduleNo;
        this.examType = examType;
        String dataJson = getJsonCreator().downSiteItemStudentInfo(scheduleNo,batch,examType,1);
        String json = genJsonString(100020121,dataJson);
        String token = getToken();
        Observable<SiteStudentBean> siteStudentBeanObservable = getHttpPresenter().downSiteScheduleItemStudent(token, json);
        addHttpSubscriber(siteStudentBeanObservable,SiteStudentBean.class);
    }

    public interface DownStudentInfo extends JsonDataPresenter.HttpBaseBean{

        @JSON
        String downSiteItemStudentInfo(@Param("scheduleNo")String scheduleNo,  // 日程编号 可为空，下发考点项目考生，如果为空将只下发开启的日程项目考生
                                       @Param("batch")int batch,   //分批查询，批号
                                       @Param("examType")int examType,  //考生考试状态
                                       @Param("getInfoType")int getInfoType);  // 0:根据项目代码去查询学生信息 1:只查询学生信息，不返回图片什么的

    }
}
