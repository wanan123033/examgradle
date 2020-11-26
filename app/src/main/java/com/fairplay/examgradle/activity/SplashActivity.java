package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

import com.app.layout.activity_splash;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.utils.CommonUtils;
import com.fairplay.examgradle.viewmodel.SplashViewModel;
import com.gwm.android.Handler;
import com.gwm.annotation.Permission;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmActivity;
import com.gwm.mvvm.NetworkLiveData;
import com.gwm.util.ContextUtil;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

@Permission({"android.permission.READ_PHONE_STATE",
        "android.permission.CAMERA",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE"})
@Layout(R.layout.activity_splash)
public class SplashActivity extends BaseMvvmActivity<Object, SplashViewModel,activity_splash> {
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog mDialog;
    private MMKV mmkv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mmkv = BaseApplication.getInstance().getMmkv();
        super.onCreate(savedInstanceState);
        LogUtils.operation("----------+++++++++++++++");

        NetworkLiveData.observer(this, networkInfo -> {
            if (networkInfo == null){
                ToastUtils.showShort("网络未连接!");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Handler.getHandler().postDelayed(() -> {
                String username = mmkv.getString(MMKVContract.USERNAME,"");
                String passsword = mmkv.getString(MMKVContract.PASSWORD,"");
                username = username + "@" + CommonUtils.getDeviceId(ContextUtil.get());
                LogUtils.operation("自动登录:username="+username+",password="+passsword);
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passsword)){
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    viewModel.login(username,passsword);
                }
            },1000);
        }
    }

    @Override
    public void onChanged(Object o) {
        if (o != null && o instanceof EnvInfoBean){
            if (((EnvInfoBean) o).code >= 1){
                ToastUtils.showShort(((EnvInfoBean) o).msg);
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        super.onPermissionGranted();
        Handler.getHandler().postDelayed(() -> {
            String username = mmkv.getString(MMKVContract.USERNAME,"");
            String passsword = mmkv.getString(MMKVContract.PASSWORD,"");
            username = username + "@" + CommonUtils.getDeviceId(ContextUtil.get());
            LogUtils.operation("自动登录:username="+username+",password="+passsword);
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passsword)){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else {
                viewModel.login(username,passsword);
            }
        },1000);
    }

    @Override
    public void onPermissionDenied() {
        super.onPermissionDenied();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("permission")
                    .setMessage("点击允许才可以使用我们的app哦")
                    .setPositiveButton("去允许", (dialog, id) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                        intent.setData(uri);
                        startActivityForResult(intent, NOT_NOTICE);
                    }).create();
            mDialog = builder.create();
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
    }
    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }
}
