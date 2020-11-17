package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.app.layout.activity_login;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.contract.MMKVContract;

import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.utils.CommonUtils;
import com.fairplay.examgradle.viewmodel.LoginViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmActivity;
import com.gwm.retrofit.Observable;
import com.gwm.util.ContextUtil;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

@Layout(R.layout.activity_login)
public class LoginActivity extends BaseMvvmActivity<Object, LoginViewModel, activity_login> {
    private MMKV mmkv;
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        mBinding.et_username.setText(mmkv.getString(MMKVContract.USERNAME,""));
        mBinding.et_password.setText(mmkv.getString(MMKVContract.PASSWORD,""));
    }
    @OnClick({R.id.btn_login,R.id.tv_server})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
                login();
                break;
            case R.id.tv_server:
                Intent intent1 = new Intent(getApplicationContext(),ServerActivity.class);
                startActivity(intent1);
                break;
        }

    }
    private void login() {
        String username = mBinding.et_username.getText().toString().trim();
        String password = mBinding.et_password.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtils.showShort("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtils.showShort("请输入密码");
            return;
        }
        if (mBinding.cb_agin_pass.isChecked()){
            mmkv.putString(MMKVContract.USERNAME,username);
            mmkv.putString(MMKVContract.PASSWORD,password);
        }
        username = username + "@" + CommonUtils.getDeviceId(ContextUtil.get());
        showDialog("登录中...");
        LogUtils.operation("登录中...username="+username+",password="+password);
        viewModel.login(username,password);
    }

    @Override
    public void onChanged(Object o) {
        if (o != null && o instanceof EnvInfoBean){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            dismissDialog();

        }
    }


}
