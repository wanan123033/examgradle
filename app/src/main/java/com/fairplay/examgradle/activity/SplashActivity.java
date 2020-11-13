package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.app.layout.activity_splash;
import com.blankj.utilcode.constant.PermissionConstants;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.httppresenter.DownGroupInfoPresenter;
import com.fairplay.examgradle.httppresenter.DownGroupPresenter;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.viewmodel.SplashViewModel;
import com.gwm.android.Handler;
import com.gwm.annotation.Permission;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmActivity;
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
//        DownGroupInfoPresenter presenter = new DownGroupInfoPresenter();
//        presenter.downGroup(2,0);
//        initItem();
    }

    private void initItem() {
        Item item = new Item();
        item.setItemName("测试多轮多值");
        item.setItemCode("10052");
        item.setSubitemCode("100521");
        item.setDigital(2);
        item.setTestType(1);
        item.setRoundNum(3);
        item.setMinValue("1");
        item.setMaxValue("10");
        long id = DBManager.getInstance().insertItem(item);
        MultipleItem multipleItem = new MultipleItem();
        multipleItem.setItemId(id);
        multipleItem.setDesc("左");
        multipleItem.setOrder("1");
        DBManager.getInstance().insertMultipItem(multipleItem);
        MultipleItem multipleItem1 = new MultipleItem();
        multipleItem1.setItemId(id);
        multipleItem1.setDesc("右");
        multipleItem1.setOrder("2");
        DBManager.getInstance().insertMultipItem(multipleItem1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Handler.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String username = mmkv.getString(MMKVContract.USERNAME,"");
                    String passsword = mmkv.getString(MMKVContract.PASSWORD,"");
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
                }
            },1000);
        }
    }

    @Override
    public void onChanged(Object o) {
        if (o != null && o instanceof EnvInfoBean){
            //TODO 启动mqtt
            Log.e("TAG=====+++++++",o.toString());
            startMqttService((EnvInfoBean) o);
            dismissDialog();

        }else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
    private void startMqttService(EnvInfoBean o) {
//        MqttService.getInstance(getApplication()).start(o);
        if(!MqttManager.getInstance().isConnected()){
            MqttManager.getInstance().init(getApplication())
                    .setServerIp(o.data.mq.ip)
                    .setServerPort(Integer.parseInt(o.data.mq.port))
                    .connect(this);
            MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
                @Override
                public void connect() {
                    super.connect();
                    Log.e("TAG===>","connect");
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void disConnect() {
                    super.disConnect();
                    Log.e("TAG===>","disConnect");
                }

                @Override
                public void onDataReceive(String message) {
                    Log.e("TAG===>",message);
                }
            });
        }
    }

    @Override
    public void onPermissionGranted() {
        super.onPermissionGranted();
        Handler.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = mmkv.getString(MMKVContract.USERNAME,"");
                String passsword = mmkv.getString(MMKVContract.PASSWORD,"");
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
            }
        },1000);
    }

    @Override
    public void onPermissionDenied() {
        super.onPermissionDenied();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("permission")
                    .setMessage("点击允许才可以使用我们的app哦")
                    .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                            intent.setData(uri);
                            startActivityForResult(intent, NOT_NOTICE);
                        }
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
