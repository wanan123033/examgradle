package com.fairplay.examgradle.httppresenter;

import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.activity.LoginActivity;
import com.fairplay.examgradle.base.BaseDataPresenter;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.contract.Permission;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;
import com.gwm.util.ContextUtil;

import java.util.List;

public class LoginDataPresenter extends BaseDataPresenter<LoginBean> {

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username,String password){
        String token = "Basic dGVybWluYWw6dGVybWluYWxfc2VjcmV0";
        Observable<LoginBean> loginhttp = getHttpPresenter().login(token,username,password);
        addHttpSubscriber("登录中...",loginhttp,LoginBean.class);
    }
    @Override
    protected void onNextResult(LoginBean response, int id) {
        if (response.code == 0) {
            StringBuffer stringBuffer = new StringBuffer();
            if (response.data.permission != null && !response.data.permission.isEmpty()){
                for (String permission : response.data.permission){
                    stringBuffer.append(permission+",");
                }
            }
            if (!stringBuffer.toString().contains(Permission.hasTestScore)){
                ToastUtils.showShort("该用户无打分权限");
                response.code = 1;
                Intent intent = new Intent(BaseApplication.getInstance(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
                return;
            }
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.PERMISSION,stringBuffer.toString());
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.CHANNEL_CODE,response.data.channelCode);
            BaseApplication.getInstance().getMmkv().putString(MMKVContract.TOKEN, response.data.token);
            DownEnvInfoPresenter envInfoPresenter = new DownEnvInfoPresenter();
            envInfoPresenter.setViewModel(getViewModel());
            envInfoPresenter.downEnv();

            //下载项目信息
            DownItemInfoPresenter presenter = new DownItemInfoPresenter();
            presenter.setViewModel(getViewModel());
            presenter.downItem();
            //下载日程信息
            DownScheduleInfoPresenter scheduleInfoPresenter = new DownScheduleInfoPresenter();
            scheduleInfoPresenter.setViewModel(getViewModel());
            scheduleInfoPresenter.downSchedule();
        }else {
            ToastUtils.showShort(response.msg);
            ((BaseViewModel) getViewModel()).sendLiveData(response);
        }
    }

    @Override
    protected synchronized void onErrorResult(Exception e, int id) {
        super.onErrorResult(e, id);
        Intent intent = new Intent();
        intent.setClass(ContextUtil.get(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextUtil.get().startActivity(intent);
    }
}
